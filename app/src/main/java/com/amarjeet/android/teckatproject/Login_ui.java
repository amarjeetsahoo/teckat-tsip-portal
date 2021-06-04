package com.amarjeet.android.teckatproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class Login_ui extends AppCompatActivity {
    EditText login_id;
    EditText login_password;
    TextView forgetPassword;
    Button signin_btn;
    ProgressBar mProgressBar;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME="mypref";
    private static final String KET_NAME="userID";
    private static final String KEY_PASS="password";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ui);

        login_id=(EditText)findViewById(R.id.login_id);
        login_password=(EditText)findViewById(R.id.login_password);
        signin_btn=(Button)findViewById(R.id.signin_btn);


        forgetPassword=(TextView)findViewById(R.id.forgetPassword);
        mProgressBar=(ProgressBar)findViewById(R.id.progressBar);

        sharedPreferences= getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String name = sharedPreferences.getString(KET_NAME,null);
        String pass = sharedPreferences.getString(KEY_PASS,null);

        if(name!=null && pass!=null) {
            logInShared(name,pass);
            Intent intent=new Intent(Login_ui.this,PaymentDetails.class);
            startActivity(intent);
            finish();
        }


        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://api.whatsapp.com/send?phone=918114957980&text=I%20AM%20UNABLE%20TO%20SIGNIN%20INTO%20T-SIP%20PORTAL&source=&data=&app_absent="));
                startActivity(i1);
            }
        });

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login_id.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter UID",Toast.LENGTH_SHORT).show();
                }
                else if (login_password.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    logIn();
                }
            }
        });

    }

    private void logIn() // without shared pref.
    {
        final String id=login_id.getText().toString();
        mProgressBar.setVisibility(View.VISIBLE);
        signin_btn.setVisibility(View.INVISIBLE);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_UID,login_id.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD,login_password.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult()!=null && task.getResult().getDocuments().size()>0)
                        {
                            Toast.makeText(getApplicationContext(),"Signed Up Successfully",Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.INVISIBLE);
                            signin_btn.setVisibility(View.VISIBLE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString(KET_NAME,login_id.getText().toString());
                            editor.putString(KEY_PASS,login_password.getText().toString());
                            editor.apply();

                            Intent intent= new Intent(Login_ui.this,PaymentDetails.class);
                            intent.putExtra("username",id);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Incorrect Credentials",Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.INVISIBLE);
                            signin_btn.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
    private void logInShared(String name, String pass) { //using shared pref data

        final String id = name;
        mProgressBar.setVisibility(View.VISIBLE);
        signin_btn.setVisibility(View.INVISIBLE);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_UID, name)
                .whereEqualTo(Constants.KEY_PASSWORD, pass)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                            //Toast.makeText(getApplicationContext(), "Signed Up Successfully", Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.INVISIBLE);
                            signin_btn.setVisibility(View.VISIBLE);
//                            SharedPreferences.Editor editor=sharedPreferences.edit();
//                            editor.putString(KET_NAME,login_id.getText().toString());
//                            editor.putString(KEY_PASS,login_password.getText().toString());
//                            editor.apply();

                            Intent intent = new Intent(Login_ui.this, PaymentDetails.class);
                            intent.putExtra("username", id);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong!\nPlease Log out", Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.INVISIBLE);
                            signin_btn.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}