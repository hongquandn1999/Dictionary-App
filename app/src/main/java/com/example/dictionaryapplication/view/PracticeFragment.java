package com.example.dictionaryapplication.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dictionaryapplication.R;
import com.example.dictionaryapplication.databinding.FragmentDetailsBinding;
import com.example.dictionaryapplication.model.Word;

import java.util.ArrayList;
import java.util.Random;


public class PracticeFragment extends Fragment {
    private ArrayList<Word> wordArrayList;
    private TextView wordToPractice,yourans;
    private FragmentDetailsBinding binding;
    private ImageButton prtbtn,btnnext;
    private ImageView imginc,imgc;
    protected static final int RESULT_SPEECH = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wordArrayList =(ArrayList<Word>) getArguments().getSerializable("wordArray");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_practice, container, false);
        wordToPractice = view.findViewById(R.id.wordToPrt);
        Word w = getRandomElement(wordArrayList);
        wordToPractice.setText(w.getWord());
        prtbtn = view.findViewById(R.id.prtbtn);
        yourans = view.findViewById(R.id.yourans);
        imgc = view.findViewById(R.id.imgcorrect);
        imginc = view.findViewById(R.id.imgincorrect);
        btnnext = view.findViewById(R.id.btnnext);
        prtbtn.setOnClickListener(new View.OnClickListener() {
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
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Word wo = getRandomElement(wordArrayList);
                wordToPractice.setText(wo.getWord());
            }
        });
        return view;
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SPEECH:
                if(resultCode == -1 && data!=null){
                    final ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    yourans.setText(text.get(0));
                    String s = wordToPractice.getText().toString().replaceAll("\"","").trim();
                    s = s.replaceAll("-","").trim();
                    if(s.equalsIgnoreCase(text.get(0))){
                        imgc.setColorFilter(Color.GREEN);
                        imginc.setColorFilter(Color.DKGRAY);
                    }else{
                        imginc.setColorFilter(Color.RED);
                        imgc.setColorFilter(Color.DKGRAY);
                    }
                }
                break;
        }
    }
    public Word getRandomElement(ArrayList<Word> list)
    {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }
}