package com.example.dictionaryapplication.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dictionaryapplication.R;
import com.example.dictionaryapplication.util.DatabaseAccess;

public class AddWordFragment extends Fragment {

    private EditText edtWord, edtDef;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_done, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mi_done:
                if(edtWord.getText().toString().isEmpty()){
                    edtWord.requestFocus();
                    edtWord.setError("This field can not be empty");
                    return false;
                }
                if(edtDef.getText().toString().isEmpty()){
                    edtDef.requestFocus();
                    edtDef.setError("This field can not be empty");
                    return false;
                }

                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());
                databaseAccess.open();
                databaseAccess.addNewWord(edtWord.getText().toString(), edtDef.getText().toString());
                databaseAccess.close();
                Toast.makeText(getContext(),"Add Word Successfully", Toast.LENGTH_LONG).show();
                Navigation.findNavController(getView()).navigate(R.id.action_addWordFragment_to_listFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtWord = view.findViewById(R.id.edt_word);
        edtDef = view.findViewById(R.id.edt_def);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_word, container, false);
    }
}