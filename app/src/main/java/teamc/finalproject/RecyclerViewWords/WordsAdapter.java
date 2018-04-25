package teamc.finalproject.RecyclerViewWords;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import teamc.finalproject.R;
import teamc.finalproject.Word;

public class WordsAdapter extends RecyclerView.Adapter<WordViewHolder> {
    private List<Word> wordsList;
    private Context context;

    public WordsAdapter(List<Word> wordsList, Context context){
        this.wordsList = wordsList;
        this.context = context;
    }
    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_word_item, null);
        WordViewHolder rcv = new WordViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final WordViewHolder holder, int position) {
        holder.mWordTextView.setText(wordsList.get(position).getForeignTranslation());
    }

    @Override
    public int getItemCount() {
        return this.wordsList.size();
    }
}
