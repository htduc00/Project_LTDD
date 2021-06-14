package com.example.musicplayer;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends AppCompatActivity {
    private Button btCancel, btRegister;
    private EditText txtEmail, txtPass;
    protected FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtEmail=findViewById(R.id.txtEmail);
        txtPass=findViewById(R.id.txtPass);
        btCancel=findViewById(R.id.btCancel);
        btRegister=findViewById(R.id.btRegister);
        auth = FirebaseAuth.getInstance();

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                String pass = txtPass.getText().toString();
                if(email.isEmpty()){
                    txtEmail.setError("Email is not blank !");
                    return;
                }
                if(pass.isEmpty()){
                    txtPass.setError("Password is not blank !");
                    return;
                }
                if(pass.length() < 6 ){
                    txtPass.setError("Password need more than 6");
                    return;
                }
                auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                        }else{
                            return;
                        }
                    }
                });
            }
        });
    }
}