package com.example.leafapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.leafapp.R;
import com.example.leafapp.utils.LoadingDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

public class RegisterActivity extends CoreActivity {

    private String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;

    private LoadingDialog mLoadingDialog;

    private TextInputLayout mFirstName;
    private TextInputLayout mLastName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private MaterialButton mRegBtn;
    private MaterialButton mCloseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Setup UI
        setupUI(findViewById(R.id.reg_parent));

        // Setup Firebase
        mAuth = FirebaseAuth.getInstance();

        // Setup widgets
        mFirstName = findViewById(R.id.reg_first_name);
        mLastName = findViewById(R.id.reg_last_name);
        mEmail = findViewById(R.id.reg_email);
        mPassword = findViewById(R.id.reg_password);
        mRegBtn = findViewById(R.id.reg_reg_btn);
        mCloseBtn = findViewById(R.id.reg_close_btn);

        // Setup Loading Dialog
        mLoadingDialog = new LoadingDialog(this);

        // Set onclick listeners
        mRegBtn.setOnClickListener(reg_listener);
        mCloseBtn.setOnClickListener(close_listener);
    }

    View.OnClickListener close_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    };

    View.OnClickListener reg_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String first_name = mFirstName.getEditText().getText().toString();
            String last_name = mLastName.getEditText().getText().toString();
            String email = mEmail.getEditText().getText().toString();
            String password = mPassword.getEditText().getText().toString();

            if (!TextUtils.isEmpty(first_name) && !TextUtils.isEmpty(last_name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                mLoadingDialog.startLoading();
                createAccount(first_name, last_name, email, password);
            }

        }
    };

    private void createAccount(final String first_name, final String last_name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            final String uid = user.getUid();

                            // create user in database
                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                                    HashMap<String, String> userMap = new HashMap<>();
                                    userMap.put("device_token", instanceIdResult.getToken());
                                    userMap.put("firstname", first_name);
                                    userMap.put("lastname", last_name);
                                    userMap.put("image", "default");
                                    userMap.put("thumb_image", "default");
                                    userMap.put("online", ""+System.currentTimeMillis());
                                    myRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Log.d(TAG, "onComplete: User is on database");
                                                user.sendEmailVerification()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(TAG, "onComplete: Email got sent");
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                                                    builder.setMessage("Email verification sent");
                                                                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener(){
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which){
                                                                            //stuff you want the button to do
                                                                            dialog.dismiss();
                                                                            finish();
                                                                        }
                                                                    });
                                                                    builder.setCancelable(false);
                                                                    AlertDialog alertDialog = builder.create();
                                                                    alertDialog.show();
                                                                    mLoadingDialog.dismissDialog();
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage(task.getException().getMessage());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            mLoadingDialog.dismissDialog();
                        }
                    }
                });
    }
}