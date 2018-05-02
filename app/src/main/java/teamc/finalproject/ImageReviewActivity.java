package teamc.finalproject;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class ImageReviewActivity extends AppCompatActivity{

    private final String TAG = "ImageReviewActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private Word mWord;
    private String userUID;
    private int gameID;
    private CardView imageViewCardView;
    private ConstraintLayout header;
    private TextView feedbackTextView;
    private FloatingActionButton actionButton;
    private TextView wordTitle;
    private TextView translationTextView;
    private ImageView photoImageView;
    private Uri mImageUri;
    private BroadcastReceiver mBroadcastReceiver;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);

        //Init Firebase and Firestore instances
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        mWord = (Word) intent.getSerializableExtra("word");
        mImageUri = mWord.getImageURI();
        userUID = intent.getStringExtra("uid");
        gameID = intent.getIntExtra("gameID", 0);

        imageViewCardView = findViewById(R.id.imageReviewCardView);
        header = findViewById(R.id.header);
        feedbackTextView = findViewById(R.id.feedbackTextView);
        actionButton = findViewById(R.id.actionButton);
        wordTitle = findViewById(R.id.titleTextView);
        translationTextView = findViewById(R.id.translationTextView);
        photoImageView = findViewById(R.id.photoImageView);

        imageViewCardView.setVisibility(View.GONE);
        translationTextView.setText(mWord.getForeignTranslation());
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive:" + intent);
                hideProgressDialog();

                switch (intent.getAction()) {
                    case MyDownloadService.DOWNLOAD_COMPLETED:
                        // Get number of bytes downloaded
                        long numBytes = intent.getLongExtra(MyDownloadService.EXTRA_BYTES_DOWNLOADED, 0);

                        // Alert success
                        showMessageDialog(getString(R.string.success), String.format(Locale.getDefault(),
                                "%d bytes downloaded from %s",
                                numBytes,
                                intent.getStringExtra(MyDownloadService.EXTRA_DOWNLOAD_PATH)));
                        Bitmap image = BitmapFactory.decodeFile(MyDownloadService.EXTRA_DOWNLOAD_PATH);
                        photoImageView.setImageBitmap(image);
                        imageViewCardView.setVisibility(View.VISIBLE);
                        break;
                    case MyDownloadService.DOWNLOAD_ERROR:
                        // Alert failure
                        showMessageDialog("Error", String.format(Locale.getDefault(),
                                "Failed to download from %s",
                                intent.getStringExtra(MyDownloadService.EXTRA_DOWNLOAD_PATH)));
                        break;
                }
            }
        };
        beginDownload();


    }

    private void showMessageDialog(String title, String message) {
        AlertDialog ad = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .create();
        ad.show();
    }


    private void beginDownload() {
        if (mImageUri != null) {
            // Get path
            String path = "photos/" + mImageUri.getLastPathSegment();

            // Kick off MyDownloadService to download the file
            Intent intent = new Intent(this, MyDownloadService.class)
                    .putExtra(MyDownloadService.EXTRA_DOWNLOAD_PATH, path)
                    .setAction(MyDownloadService.ACTION_DOWNLOAD);
            startService(intent);

            // Show loading spinner
            showProgressDialog(getString(R.string.progress_downloading));
        } else {
            return;
        }
    }


        private void showProgressDialog (String caption){
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setIndeterminate(true);
            }

            mProgressDialog.setMessage(caption);
            mProgressDialog.show();
        }
        private void hideProgressDialog () {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }


