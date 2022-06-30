package com.omerarslan.instaclonefirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    EditText emailText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance(); //sınıftan bir obje oluşturmamıza olanak sağlıyor
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);

        //Giriş yapmış kullanıcı her defasında giriş yaptırmamak için
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser(); //Giriş yapmış kullanıcıyı verecek

        if (firebaseUser != null){

            Intent intent = new Intent(SignUpActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();

        }

    }
    //////////// Giriş yapma işlemleri
    public void signInClicked(View view){

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //giriş başarılı olursa ne yapılacak
                Intent intent = new Intent(SignUpActivity.this, FeedActivity.class);
                startActivity(intent);
                finish(); //diğer aktiviteleri yok ediyor. Bu sayede geriye basıldığında giriş ekranına tekrar dönmüyor.
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //giriş başarısız olursa ne yapılacak
                Toast.makeText(SignUpActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }


    ////////////Kayıt olma işlemleri
    public void signUpClicked(View view){

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //kayıt başarılı olursa ne yapılacak
                Toast.makeText(SignUpActivity.this, "User Created Successfuly",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUpActivity.this, FeedActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //kayıt başarısız olursa ne yapılacak
                Toast.makeText(SignUpActivity.this, e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });

    }
}