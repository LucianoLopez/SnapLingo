package teamc.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;

public class VerificationActivity extends AppCompatActivity {

    private static final String TAG = "VerificationActivity";

    private Bitmap mPhotoBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        Intent intent = getIntent();
        String labelStr = intent.getStringExtra(CameraActivity.DETECTION_LABELS);
        Uri imageUri = Uri.parse(intent.getStringExtra("imageUri"));
        try {
            mPhotoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            Log.d(TAG, "error while creating bitmap");
        }
        ImageView photoImageView = findViewById(R.id.photoImageView);
        photoImageView.setImageBitmap(mPhotoBitmap);
    }
}
