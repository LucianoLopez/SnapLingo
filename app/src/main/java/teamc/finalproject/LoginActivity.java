package teamc.finalproject;

// Created by Will
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private TextView mStatusTextView;

    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Views
        this.mEmailEditText = findViewById(R.id.emailEditText);
        this.mPasswordEditText = findViewById(R.id.passwordEditText);
        this.mStatusTextView = findViewById(R.id.statusTextView);

        // Buttons
        findViewById(R.id.signinButton).setOnClickListener(this);
        findViewById(R.id.createaccountButton).setOnClickListener(this);

        // Firebase Auth
        this.mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signinButton) {
            signIn(this.mEmailEditText.getText().toString(), this.mPasswordEditText.getText().toString());
        } else if (i == R.id.createaccountButton) {
            Log.d(TAG, "Create Account Button Pressed");
            createAccount(this.mEmailEditText.getText().toString(), this.mPasswordEditText.getText().toString());
        }
    }

    /** Validates fields, attempts to create a new account. */
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount: " + email);
        if (!this.validateForm()) {
            return;
        }
        showProgressDialog();

        // Create New User with email
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);

                        }
                    }
                });
    }

    /** Validates fields, attempts to sign in. */
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn: " + email);
        if (!this.validateForm()) {
            return;
        }
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    /** Checks to see if fields are valid. */
    private boolean validateForm() {
        boolean valid = true;

        String email = this.mEmailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            this.mEmailEditText.setError("Required.");
            valid = false;
        } else {
            this.mEmailEditText.setError(null);
        }

        String password = this.mPasswordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            this.mPasswordEditText.setError("Required.");
            valid = false;
        } else {
            this.mPasswordEditText.setError(null);
        }

        return valid;
    }

    /** Updates the UI after login attempt. If login success, will pass along Firebase user to next
     *  activity. */
    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            // Login Success

            Intent goToCreateOrJoin = new Intent(LoginActivity.this, CreateOrJoinActivity.class);
            goToCreateOrJoin.putExtra("username", user.getDisplayName());
            goToCreateOrJoin.putExtra("uid", user.getUid());

            System.out.println("UID is " + user.getUid());

            startActivity(goToCreateOrJoin);

        } else {
            // Login Failed

        }
    }

    /** Shows a loading circle. */
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    /** Hides the loading circle. */
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
