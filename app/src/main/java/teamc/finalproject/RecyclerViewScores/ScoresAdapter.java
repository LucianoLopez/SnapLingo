package teamc.finalproject.RecyclerViewScores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import teamc.finalproject.R;
import teamc.finalproject.Score;

public class ScoresAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<Score> scoreList;
    private int viewType;
    private Context context;
    private ColorGenerator mColorGenerator;

    public ScoresAdapter(List<Score> scoreList, int viewType, Context context){
        this.scoreList = scoreList;
        this.viewType = viewType;
        this.context = context;
        this.mColorGenerator = ColorGenerator.MATERIAL;
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
        foundWordViewHolder.mPlaceNumberTextView.setText(Integer.toString(position + 1));
        int color = mColorGenerator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(score.getUserName().substring(0, 1), color);
        foundWordViewHolder.mIconImageView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return this.scoreList.size();
    }
}
