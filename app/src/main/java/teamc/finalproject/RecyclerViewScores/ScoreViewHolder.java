package teamc.finalproject.RecyclerViewScores;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import teamc.finalproject.R;

public class ScoreViewHolder extends RecyclerView.ViewHolder {
    public TextView mWordTextView;
    public Button mCameraButton;

    public ScoreViewHolder(View itemView){
        super(itemView);
        mWordTextView = itemView.findViewById(R.id.wordTextView);
        mCameraButton = itemView.findViewById(R.id.cameraButton);
    }
}
