package teamc.finalproject.RecyclerViewScores;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import teamc.finalproject.R;

public class ScoresViewHolder extends RecyclerView.ViewHolder {
    public ImageView mIconImageView;
    public TextView mPlaceNumberTextView;
    public TextView mWordTextView;
    public TextView mScoresTextView;

    public ScoresViewHolder(View itemView){
        super(itemView);
        mIconImageView = itemView.findViewById(R.id.iconImageView);
        mPlaceNumberTextView = itemView.findViewById(R.id.placeNumberTextView);
        mWordTextView = itemView.findViewById(R.id.wordTextView);
        mScoresTextView = itemView.findViewById(R.id.wordTextView2);
    }
}
