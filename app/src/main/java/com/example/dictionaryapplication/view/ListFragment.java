package com.example.dictionaryapplication.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.dictionaryapplication.R;
import com.example.dictionaryapplication.model.MyAdapter;
import com.example.dictionaryapplication.model.Word;
import com.example.dictionaryapplication.util.DatabaseAccess;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private RecyclerView rvWords;
    private ArrayList<String> words;
    private ArrayList<Word> mWords, mFavoWord;
    private MyAdapter myAdapter;
    private FloatingActionButton btAdd;
    private SearchView searchView;
    private ImageButton imageButton;
    protected static final int RESULT_SPEECH = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rvWords = (RecyclerView) view.findViewById(R.id.rv_words);
        mWords = new ArrayList<Word>();
        this.rvWords.setLayoutManager(new LinearLayoutManager(getActivity()));
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());
        databaseAccess.open();
        words = (ArrayList<String>) databaseAccess.getWords();

        for (String s :  words){
            String d = databaseAccess.getDefinition(s);
            int id = databaseAccess.getIdByWord(s);
            Word w = new Word(id ,s, d);
            mWords.add(w);
        }
        databaseAccess.close();
        myAdapter = new MyAdapter(mWords, getActivity());
        rvWords.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        searchView = view.findViewById(R.id.sv_word);
        btAdd = view.findViewById(R.id.addButton);
        imageButton = view.findViewById(R.id.imgbtn);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                Log.d("test",newText);
                return false;
            }
        });
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_listFragment_to_addWordFragment);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(getContext(),"Your device dosent support Speech to Text",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SPEECH:
                if(resultCode == -1 && data!=null){
                    final ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    System.out.println(text.get(0));
                    searchView.setQuery(text.get(0),false);
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.drawermenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.etov:
                myAdapter = new MyAdapter(mWords, getActivity());
                rvWords.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
                break;
            case R.id.favo:
                Word word = null;
                mFavoWord = new ArrayList<>();
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());
                databaseAccess.open();
                ArrayList<Integer> li = (ArrayList<Integer>) databaseAccess.getFavos();
                for(Integer i : li){
                    word = databaseAccess.getWordById(i);
                    mFavoWord.add(word);
                }
                myAdapter = new MyAdapter(mFavoWord, getActivity());
                rvWords.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
                break;
            case R.id.prt:
                Bundle bundle = new Bundle();
                bundle.putSerializable("wordArray",mWords);
                Navigation.findNavController(getView()).navigate(R.id.action_listFragment_to_practiceFragment,bundle);
                break;
        }

        return super.onOptionsItemSelected(item);

    }
}