package teamc.finalproject.RecyclerViewScores;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import teamc.finalproject.R;
import teamc.finalproject.Score;
import teamc.finalproject.VerificationActivity;
import teamc.finalproject.Word;

public class ScoresAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<Score> scoreList;
    private int viewType;
    private Context context;

    public ScoresAdapter(List<Score> scoreList, int viewType, Context context){
        this.scoreList = scoreList;
        this.viewType = viewType;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_scoreboard_item, parent, false);
        return new ScoresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Score score = scoreList.get(position);
        ScoresViewHolder foundWordViewHolder = (ScoresViewHolder) holder;
        foundWordViewHolder.mWordTextView.setText(score.getUserName());
        foundWordViewHolder.mScoresTextView.setText(String.valueOf(score.getScore()));

    }

    @Override
    public int getItemCount() {
        return this.scoreList.size();
    }
}
