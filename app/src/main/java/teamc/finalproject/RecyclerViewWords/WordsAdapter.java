package teamc.finalproject.RecyclerViewWords;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import teamc.finalproject.VerificationActivity;
import teamc.finalproject.R;
import teamc.finalproject.Word;

public class WordsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Word> wordsList;
    private int viewType;
    private Context context;

    public WordsAdapter(List<Word> wordsList, int viewType, Context context){
        this.wordsList = wordsList;
        this.viewType = viewType;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView;
        RecyclerView.ViewHolder rcv = null;
        switch (viewType) {
            case 0:
                layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_word_item, null);
                rcv = new WordViewHolder(layoutView);
                break;

            case 1:
                layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_found_word_item, null);
                rcv = new FoundWordViewHolder(layoutView);
                break;
        }
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                WordViewHolder wordViewHolder = (WordViewHolder) holder;
                wordViewHolder.mWordTextView.setText(wordsList.get(position).getForeignTranslation());
                wordViewHolder.mCameraButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, VerificationActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;

            case 1:
                FoundWordViewHolder foundWordViewHolder = (FoundWordViewHolder) holder;
                foundWordViewHolder.mWordTextView.setText(wordsList.get(position).getForeignTranslation());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.wordsList.size();
    }
}
