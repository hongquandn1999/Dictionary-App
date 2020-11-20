package com.example.dictionaryapplication.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionaryapplication.R;
import com.example.dictionaryapplication.util.DatabaseAccess;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable{
    private static ArrayList<Word> mWords;
    private ArrayList<Word> mWordsAll;
    private static Context mConText;


    public MyAdapter(ArrayList<Word> mWords, Context mConText){
        this.mWords = mWords;
        this.mWordsAll = new ArrayList<Word>(mWords);
        this.mConText = mConText;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word,parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Word word = mWords.get(position);
        holder.tvWord.setText(word.getWord());
        holder.tvDescript.setText(Html.fromHtml(word.getDef()));
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(mConText);
        databaseAccess.open();
        ArrayList<Integer> li = (ArrayList<Integer>) databaseAccess.getFavos();
        databaseAccess.close();
        for(Integer i : li){
            if(i == word.getId()){
                System.out.println(word.getId()+"");
                holder.favoBtn.setColorFilter(Color.RED);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mWords.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Word> filtered = new ArrayList<Word>();
            if (charSequence.toString().isEmpty()){
                filtered.addAll(mWordsAll);
            } else {
                for (Word w : mWordsAll){
                    if (w.getWord().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filtered.add(w);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filtered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (mWordsAll != null) {
                mWords.clear();
                mWords.addAll((Collection<? extends Word>) filterResults.values);
                notifyDataSetChanged();
            }
        }
    };
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public View view;
        private TextView tvWord, tvDescript;
        private ImageButton favoBtn;

        public MyViewHolder(@NonNull final View view) {
            super(view);
            this.view = view;
            tvWord = view.findViewById(R.id.tv_word);
            tvDescript = view.findViewById(R.id.tv_descript);
            favoBtn = view.findViewById(R.id.favoBtn);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("word", mWords.get(getAdapterPosition()));
                    Navigation.findNavController(v).navigate(R.id.action_listFragment_to_detailsFragment, bundle);
                }
            });
            favoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean check = false;
                    String w = mWords.get(getAdapterPosition()).getWord();
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(mConText);
                    databaseAccess.open();
                    int id = databaseAccess.getIdByWord(w);
                    ArrayList<Integer> li = (ArrayList<Integer>) databaseAccess.getFavos();
                    for(Integer i : li){
                        if(i == id){
                            check = true;
                        }
                    }
                    if(check){
                        databaseAccess.deleteFavo(id);
                        favoBtn.setColorFilter(Color.DKGRAY);
                    }else{
                        databaseAccess.addFavo(id);
                        favoBtn.setColorFilter(Color.rgb(252, 163, 204));
                    }

                    databaseAccess.close();
                }
            });
        }
    }
}
