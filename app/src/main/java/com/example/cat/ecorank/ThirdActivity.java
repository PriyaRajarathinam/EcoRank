package com.example.cat.ecorank;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private static EditText mNewUsername,mNewPassword,mNewEmail;
    private DatabaseReference mDatabase;
    private static final String TAG = "ThirdActivity";
    @Override
  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        onStart();
        Button finish=(Button)findViewById(R.id.finish_btn);
        finish.setOnClickListener(this);

    }
    public void onStart () {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mNewEmail=(EditText)findViewById(R.id.newemail_edit_text);
        mNewPassword=(EditText)findViewById(R.id.newpassword_edit_text);
        mNewUsername=(EditText)findViewById(R.id.newusername_edit_text);


    }

    public void onClick (View v) {
        Log.d(TAG,"Working");
        switch (v.getId()) {
            case R.id.finish_btn:
                if(!validForm()){
                   return;
                }
                signUp(mNewEmail.getText().toString(),mNewPassword.getText().toString());


            default:


        }

    }

    public boolean validForm(){
        boolean valid=true;
        String email=mNewEmail.getText().toString();
        String password=mNewPassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            mNewEmail.setError("required");
            valid=false;
        }else{
            mNewEmail.setError(null);
        }
        if(TextUtils.isEmpty(password)){
            mNewPassword.setError("required");
            valid=false;
        }else{
            mNewPassword.setError(null);
        }
        return valid;
    }
    public void signUp(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userName=mNewUsername.getText().toString();
                            Log.d(TAG,"string is "+userName);

                            UserProfileChangeRequest profileUpdates= new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userName)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }else{
                                                Log.d(TAG, "User profile not updated");
                                            }
                                        }
                                    });
                            try
                            {
                                Thread.sleep(1000);
                            }
                            catch(InterruptedException ex)
                            {
                                Thread.currentThread().interrupt();
                            }
                            String id=user.getUid();
                            User bang=new User(user.getDisplayName(),0);
                            mDatabase.child(id).setValue(bang);

                            Log.d(TAG, "username is "+user.getDisplayName());
                            updateMain(user);
                        } else {

                            // If sign in fails, display a message to the user.
                            Toast.makeText(ThirdActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateMain(null);
                        }

                        // ...
                    }
                });
    }
    public void updateMain(FirebaseUser user){
        if(user!=null){
            Intent intent=new Intent(ThirdActivity.this,SecondActivity.class);
            startActivity(intent);
        }

    }
}
