package com.example.cecil.myapplication.AccoountActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cecil.myapplication.AddhotelActivity;
import com.example.cecil.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button b1;
    private EditText t1,t2;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        Button b1= findViewById(R.id.loginbtn);
        t1= findViewById(R.id.loginemail);
        t2= findViewById(R.id.loginpass);
        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(t1.getText().toString().trim()) && !TextUtils.isEmpty(t2.getText().toString().trim())){
                    mAuth.signInWithEmailAndPassword(t1.getText().toString().trim(), t2.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                                        Intent i =new Intent(LoginActivity.this, AddhotelActivity.class);
                                        startActivity(i);

                                    }
                                    else{

                                        Toast.makeText(LoginActivity.this, "Incorrect credentials.", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }

            }

        });
    }

}
