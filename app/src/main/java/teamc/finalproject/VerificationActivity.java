package teamc.finalproject;

/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * Adapted from https://github.com/firebase/quickstart-android/tree/master/storage
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Activity to upload and download photos from Firebase Storage.
 *
 * See {@link MyUploadService} for upload example.
 * See {@link MyDownloadService} for download example.
 */
public class VerificationActivity extends AppCompatActivity {

    public static final String DETECTION_LABELS = "teamc.finalproject.DETECTION_LABELS";

    private static final String TAG = "VerificationActivity";

    private Bitmap mPhotoBitmap;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final String KEY_FILE_URI = "key_file_uri";
    private static final String KEY_DOWNLOAD_URL = "key_download_url";

    private BroadcastReceiver mBroadcastReceiver;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private Uri mDownloadUrl = null;
    private Uri mImageUri = null;
    private String mResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        // Restore instance state
        if (savedInstanceState != null) {
            mImageUri = savedInstanceState.getParcelable(KEY_FILE_URI);
            mDownloadUrl = savedInstanceState.getParcelable(KEY_DOWNLOAD_URL);
        }
        onNewIntent(getIntent());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        // Local broadcast receiver
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
                        break;
                    case MyDownloadService.DOWNLOAD_ERROR:
                        // Alert failure
                        showMessageDialog("Error", String.format(Locale.getDefault(),
                                "Failed to download from %s",
                                intent.getStringExtra(MyDownloadService.EXTRA_DOWNLOAD_PATH)));
                        break;
                    case MyUploadService.UPLOAD_COMPLETED:
                        retrieveMetadata();
                    case MyUploadService.UPLOAD_ERROR:
                        onUploadResultIntent(intent);
                        break;
                }
            }
        };

        launchCamera();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // Check if this Activity was launched by clicking on an upload notification
        if (intent.hasExtra(MyUploadService.EXTRA_DOWNLOAD_URL)) {
            onUploadResultIntent(intent);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI(mAuth.getCurrentUser());

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

    @Override
    public void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        out.putParcelable(KEY_FILE_URI, mImageUri);
        out.putParcelable(KEY_DOWNLOAD_URL, mDownloadUrl);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                if (mImageUri != null) {
                    try {
                        mPhotoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                    } catch (IOException e) {
                        Log.d(TAG, "error while creating bitmap");
                    }
                    uploadFromUri(mImageUri);
                } else {
                    Log.w(TAG, "File URI is null");
                }
            } else {
                Toast.makeText(this, "Taking picture failed.", Toast.LENGTH_SHORT).show();
                Log.d(getClass().getName(), "value = " + resultCode);
            }
        }
    }

    private void uploadFromUri(Uri fileUri) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

        // Save the File URI
        mImageUri = fileUri;

        // Clear the last download, if any
        updateUI(mAuth.getCurrentUser());
        mDownloadUrl = null;

        // Start MyUploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background
        startService(new Intent(this, MyUploadService.class)
                .putExtra(MyUploadService.EXTRA_FILE_URI, fileUri)
                .setAction(MyUploadService.ACTION_UPLOAD));

        // Show loading spinner
        showProgressDialog(getString(R.string.progress_uploading));
    }

    private void beginDownload() {
        // Get path
        String path = "photos/" + mImageUri.getLastPathSegment();

        // Kick off MyDownloadService to download the file
        Intent intent = new Intent(this, MyDownloadService.class)
                .putExtra(MyDownloadService.EXTRA_DOWNLOAD_PATH, path)
                .setAction(MyDownloadService.ACTION_DOWNLOAD);
        startService(intent);

        // Show loading spinner
        showProgressDialog(getString(R.string.progress_downloading));
    }

    private void launchCamera() {
        Log.d(TAG, "launchCamera");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d(TAG, "error while creating image file");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                mImageUri = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                "camera_photo",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    private void retrieveMetadata () {

        showProgressDialog("Processing Image...");
        DocumentReference docRef = mFirestore.collection("images").document(mImageUri.getLastPathSegment());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());
                        mResponse = ("" +  task.getResult().getData()).replaceAll("=", ":");
                    } else {
                        Log.d(TAG, "No such document");
                        mResponse = "No stored label data";
                    }
                    updateUI(mAuth.getCurrentUser());
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void onUploadResultIntent(Intent intent) {
        // Got a new intent from MyUploadService with a success or failure
        mDownloadUrl = intent.getParcelableExtra(MyUploadService.EXTRA_DOWNLOAD_URL);
        mImageUri = intent.getParcelableExtra(MyUploadService.EXTRA_FILE_URI);

        updateUI(mAuth.getCurrentUser());
    }

    private void updateUI(FirebaseUser user) {
        if (mResponse != null) {
            hideProgressDialog();
            // Manually filter the proto message to the label descriptions
            ArrayList<String> labels = new ArrayList<String>();
            String labelStr = "";
            for (String it : mResponse.split(",")) {
                if (it.split(":")[0].contains("description")) {
                    labels.add(it.split(":")[1]);
                    labelStr += it.split(":")[1] + " / ";
                }
            }

            Log.d(TAG, labelStr);

            if (labelStr.length() > 2) {
                // Remove trailing slash
                labelStr = labelStr.substring(0, labelStr.length() - 2);
                Log.d(TAG,"Found: " + labels.size() + " labels.");
                ImageView photoImageView = findViewById(R.id.photoImageView);
                photoImageView.setImageBitmap(mPhotoBitmap);
            }
        }
    }

    private void showMessageDialog(String title, String message) {
        AlertDialog ad = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .create();
        ad.show();
    }

    private void showProgressDialog(String caption) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.setMessage(caption);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            updateUI(null);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}