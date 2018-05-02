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
    private TextDrawable.IShapeBuilder mBuilder;

    public ScoresAdapter(List<Score> scoreList, int viewType, Context context){
        this.scoreList = scoreList;
        this.viewType = viewType;
        this.context = context;
        this.mColorGenerator = ColorGenerator.DEFAULT;
        mBuilder = TextDrawable.builder()
                                .beginConfig()
                                    .withBorder(4)
                                .endConfig();
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
        foundWordViewHolder.mPlaceNumberTextView.setText(ordinal(position + 1));
        int color = mColorGenerator.getColor(score.getUserName());
        TextDrawable drawable = mBuilder.buildRound(score.getUserName().substring(0, 1), color);
        foundWordViewHolder.mIconImageView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return this.scoreList.size();
    }

    private String ordinal(int i) {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];

        }
    }
}
