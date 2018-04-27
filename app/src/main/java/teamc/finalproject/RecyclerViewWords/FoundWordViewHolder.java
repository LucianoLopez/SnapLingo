package teamc.finalproject.RecyclerViewWords;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import teamc.finalproject.R;

public class FoundWordViewHolder extends RecyclerView.ViewHolder {
    public TextView mWordTextView;
    public Button mImageButton;

    public FoundWordViewHolder(View itemView){
        super(itemView);
        mWordTextView = itemView.findViewById(R.id.wordTextView);
        mImageButton = itemView.findViewById(R.id.imageButton);
    }
}
