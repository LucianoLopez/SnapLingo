package teamc.finalproject.RecyclerViewWords;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import teamc.finalproject.ImageReviewActivity;
import teamc.finalproject.R;
import teamc.finalproject.VerificationActivity;
import teamc.finalproject.Word;

public class WordsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<Word> wordList;
    private int viewType;
    private Context context;

    public WordsAdapter(List<Word> wordList, int viewType, Context context){
        this.wordList = wordList;
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
        final Word word = wordList.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                WordViewHolder wordViewHolder = (WordViewHolder) holder;
                wordViewHolder.mWordTextView.setText(word.getForeignTranslation());
                wordViewHolder.mCameraButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, VerificationActivity.class);
                        intent.putExtra("word", word);
                        Activity currentActivity = (Activity) context;
                        System.out.println("The activity is " + currentActivity);
                        String userUID = currentActivity.getIntent().getStringExtra("uid");
                        int gameID = currentActivity.getIntent().getIntExtra("gameID", 0);

                        intent.putExtra("uid", userUID);
                        intent.putExtra("gameID", gameID);
                        context.startActivity(intent);
                    }
                });
                break;

            case 1:
                FoundWordViewHolder foundWordViewHolder = (FoundWordViewHolder) holder;
                foundWordViewHolder.mWordTextView.setText(word.getForeignTranslation());
                foundWordViewHolder.mImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ImageReviewActivity.class);
                        intent.putExtra("word", word);
                        Activity currentActivity = (Activity) context;
                        String userUID = currentActivity.getIntent().getStringExtra("uid");
                        int gameID = currentActivity.getIntent().getIntExtra("gameID", 0);

                        intent.putExtra("uid", userUID);
                        intent.putExtra("gameID", gameID);
                        context.startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.wordList.size();
    }
}
