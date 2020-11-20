package com.example.dictionaryapplication.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dictionaryapplication.R;
import com.example.dictionaryapplication.databinding.FragmentDetailsBinding;
import com.example.dictionaryapplication.model.Word;

import java.util.Locale;

public class DetailsFragment extends Fragment {

    private Word word;
    private WebView wv;
    private TextView tvW;
    private FragmentDetailsBinding binding;
    private ImageButton b1;
    private TextToSpeech t1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments()!=null){
            word = (Word) getArguments().getSerializable("word");
        }
    }
    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        t1=new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });
        b1 = view.findViewById(R.id.bt_sp);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Toast.makeText(getContext(), word.getWord(),Toast.LENGTH_SHORT).show();
                t1.speak(word.getWord(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_details, null,false);
        View viewRoot = binding.getRoot();
        binding.setWordfor(word);
        wv = viewRoot.findViewById(R.id.webview);
        tvW = viewRoot.findViewById(R.id.tv_w);
        wv.loadData(word.getDef(), "text/html", "UTF-8");
        tvW.setText(word.getWord());
        return viewRoot;
        //return inflater.inflate(R.layout.fragment_details, container, false);
    }
}