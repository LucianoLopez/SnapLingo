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
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    private StorageReference mStorageReference;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);

        //Init Firebase and Firestore instances
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        mWord = (Word) intent.getSerializableExtra("word");
        mImageUri = Uri.parse(mWord.getImageURI());
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
        wordTitle.setText(mWord.getEnglishTranslation());
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
                        String path = intent.getStringExtra(MyDownloadService.EXTRA_DOWNLOAD_PATH);
                        mStorageReference.child(path).getBytes(numBytes).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap image =BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                photoImageView.setImageBitmap(image);
                                imageViewCardView.setVisibility(View.VISIBLE);
                            }
                        });
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
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();

        // Register receiver for uploads and downloads
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(mBroadcastReceiver, MyDownloadService.getIntentFilter());
        manager.registerReceiver(mBroadcastReceiver, MyUploadService.getIntentFilter());
    }

    @Override
    public void onStop() {
        super.onStop();

        // Unregister download receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
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


