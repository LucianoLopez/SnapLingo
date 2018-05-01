package teamc.finalproject.RecyclerViewScores;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import teamc.finalproject.R;

public class ScoresViewHolder extends RecyclerView.ViewHolder {
    public TextView mWordTextView;
    public TextView mScoresTextView;

    public ScoresViewHolder(View itemView){
        super(itemView);
        mWordTextView = itemView.findViewById(R.id.wordTextView);
        mScoresTextView = itemView.findViewById(R.id.wordTextView2);
    }
}
