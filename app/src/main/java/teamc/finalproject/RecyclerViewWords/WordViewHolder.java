package teamc.finalproject.RecyclerViewWords;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import teamc.finalproject.R;

public class WordViewHolder extends RecyclerView.ViewHolder {
    public TextView mWordTextView;
    public Button mCameraButton;

    public WordViewHolder(View itemView){
        super(itemView);
        mWordTextView = itemView.findViewById(R.id.wordTextView);
        mCameraButton = itemView.findViewById(R.id.cameraButton);
    }
}
