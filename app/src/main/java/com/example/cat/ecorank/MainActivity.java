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
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";
    private EditText emailField,passwordField;
    private static FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        onStart();
        Button login = (Button) findViewById(R.id.login_btn);
        login.setOnClickListener(this);
        Button signup= (Button) findViewById(R.id.signup_btn);
        signup.setOnClickListener(this);


    }

        public void onStart () {
            super.onStart();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            emailField=(EditText)findViewById(R.id.email_edit_text);
            passwordField=(EditText)findViewById(R.id.password_edit_text);

        }

        public void onClick (View v) {
            switch (v.getId()) {
                case R.id.login_btn:
                    if (!validForm()) {
                        return;
                    }
                    signIn(emailField.getText().toString(), passwordField.getText().toString());
                    break;
                case R.id.signup_btn:
                    Intent intent=new Intent(MainActivity.this,ThirdActivity.class);
                    startActivity(intent);
                    break;
                default:


            }

        }



        public void signIn(String email, String password){

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            currentUser=user;
                            Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                            startActivity(intent);
                           // updateMain(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateMain(null);
                        }
                    }
                });

            }



        public boolean validForm(){
            boolean valid=true;
            String email=emailField.getText().toString();
            String password=passwordField.getText().toString();
            if(TextUtils.isEmpty(email)){
                emailField.setError("required");
                valid=false;
            }else{
                emailField.setError(null);
            }
            if(TextUtils.isEmpty(password)){
                passwordField.setError("required");
                valid=false;
            }else{
                passwordField.setError(null);
            }
            return valid;
        }

        public void updateMain(FirebaseUser user){
        if(user!=null){
            currentUser=user;


        }
        }
        public FirebaseUser returnUser(){
            return currentUser;
        }
}