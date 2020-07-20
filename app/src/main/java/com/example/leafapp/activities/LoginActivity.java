package com.example.leafapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.leafapp.R;
import com.example.leafapp.utils.LoadingDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends CoreActivity {

    private String TAG = "LoginActivity";
    private FirebaseAuth mAuth;

    private LoadingDialog mLoadingDialog;

    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private MaterialButton mLoginBtn;
    private MaterialButton mRegBtn;
    private LinearLayout mFieldContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Setup UI
        setupUI(findViewById(R.id.login_parent));

        // Setup Firebase
        mAuth = FirebaseAuth.getInstance();

        // Setup widgets
        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_password);
        mLoginBtn = findViewById(R.id.login_login_btn);
        mRegBtn = findViewById(R.id.login_reg_btn);
        mFieldContainer = findViewById(R.id.login_field_container);

        // Setup Loading Dialog
        mLoadingDialog = new LoadingDialog(this);

        // Set onclick listeners
        mLoginBtn.setOnClickListener(login_listener);
        mRegBtn.setOnClickListener(reg_listener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // start fade up animation
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_up);
//        mFieldContainer.startAnimation(animation);
    }

    // start login listener
    View.OnClickListener login_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String email = mEmail.getEditText().getText().toString();
            String password = mPassword.getEditText().getText().toString();

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                mLoadingDialog.startLoading();
                signIn(email, password);
            }
        }
    };

    // send to register listener
    View.OnClickListener reg_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(regIntent);
        }
    };

    // log in the user or show error message
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user.isEmailVerified()){
                               Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                               mLoadingDialog.dismissDialog();
                               startActivity(mainIntent);
                               finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Please verify your email!");
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                mLoadingDialog.dismissDialog();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage(task.getException().getMessage());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            mLoadingDialog.dismissDialog();
                        }
                    }
                });
    }
}