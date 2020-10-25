package com.example.dell.otplogin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText e1,e2;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;
    String verification_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1=findViewById(R.id.phone_num);
        e2=findViewById(R.id.otp_num);
        //intilize auth
        auth=FirebaseAuth.getInstance();
        //
        mcallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verification_code=s;
                Toast.makeText(MainActivity.this, "Verification Code Sent", Toast.LENGTH_SHORT).show();
            }
        };
    }
    public void send_sms(View view)
    {
        String num=e1.getText().toString().trim();
        PhoneAuthProvider.getInstance().verifyPhoneNumber
                ( num,60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        this,               // Activity (for callback binding)
                        mcallback);
    }
    //

    //verification start
    public  void verify(View view)
    {
        //otp num
        String otp_num=e2.getText().toString().trim();

        verifyPhoneNumber(verification_code,otp_num);

    }
    public void verifyPhoneNumber(String verify_code,String input_code)
    {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verify_code,input_code);
        SignInWithPhone(credential);

    }


    public void SignInWithPhone(PhoneAuthCredential credential)
    {

        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "user signIn sucessfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    //next activity
    public void email(View view)
    {
        Intent intent=new Intent(this,Main2Activity.class);
        startActivity(intent);

    }
}
