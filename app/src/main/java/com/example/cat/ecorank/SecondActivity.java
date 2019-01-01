package com.example.cat.ecorank;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private static TextView mNewQuestion;
    private static FirebaseUser currentUser;
    private static final String TAG="ThirdActivity";
    private static EditText[]list=new EditText[4];
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Button leader=(Button)findViewById(R.id.leader_btn);
        Button submit=(Button)findViewById(R.id.submit_btn);
        submit.setOnClickListener(this);
        leader.setOnClickListener(this);
        list[0]=(EditText)findViewById(R.id.Recycle_edit_view);
        list[1]=(EditText)findViewById(R.id.walk_edit_view);
        list[2]=(EditText)findViewById(R.id.sustainable_edit_text);
        list[3]=(EditText)findViewById(R.id.multi_edit_view);
        onStart();
    }
    public void onStart () {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mNewQuestion=(TextView)findViewById(R.id.question_text_view);
        String userName=currentUser.getDisplayName();
        Log.d(TAG,"username is"+userName);
        mNewQuestion.setText("What have you done today, "+userName+"?");


    }
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
            System.out.println("user id is"+getUid());
                int total=0;
                for(int i=0;i<list.length;i++){
                   String temp=list[i].getText().toString();
                   if(TextUtils.isEmpty(temp)){
                       continue;
                   }

                   total+=Integer.valueOf(temp);
                   System.out.println(total +"My foor lop");
                }
                DatabaseReference ref=mDatabase.child(mAuth.getCurrentUser().getUid());
                updateUser(ref,total);

                break;
            case R.id.leader_btn:
                Log.d(TAG,"clicked leader button");
                Intent intent=new Intent(SecondActivity.this,FourthActivity.class);
                startActivity(intent);
                break;
            default:


        }

    }
    public void updateUser(DatabaseReference ref, final int points){

       ref.runTransaction(new Transaction.Handler() {
           @NonNull
           @Override
           public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
               User u=mutableData.getValue(User.class);
               if(u==null)
               {
                   Log.d(TAG,"user was null");
                   return Transaction.success(mutableData);
               }
               u.points=u.points+points;
               mutableData.setValue(u);
               return Transaction.success(mutableData);
           }

           @Override
           public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
               Log.d(TAG, "postTransaction:onComplete:" + databaseError);
           }
       });

    }
    public String getUid(){
        return mAuth.getCurrentUser().getUid();
    }

}
