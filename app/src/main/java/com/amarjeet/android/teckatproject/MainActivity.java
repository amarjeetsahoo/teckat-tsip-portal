package com.amarjeet.android.teckatproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    ImageView tklogo,teckatnm1,teckatnm2;
    Animation anim1,anim2,anim3;
    Button letsGobtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tklogo=(ImageView)findViewById(R.id.tklogo);
        teckatnm1=(ImageView)findViewById(R.id.teckatnm1);
        teckatnm2=(ImageView)findViewById(R.id.teckatnm2);
        letsGobtn=(Button)findViewById(R.id.letsGobtn);

        letsGobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Login_ui.class);
                startActivity(intent);
                finish();
            }
        });


        //loading animations
        anim1= AnimationUtils.loadAnimation(this,R.anim.anim1);
        anim2=AnimationUtils.loadAnimation(this,R.anim.anim2);
        anim3=AnimationUtils.loadAnimation(this,R.anim.anim3);

        //passing animations
        tklogo.startAnimation(anim1);
        teckatnm1.startAnimation(anim2);
        teckatnm2.startAnimation(anim2);
        letsGobtn.startAnimation(anim3);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            NotificationChannel channel=
                    new NotificationChannel("MyNotification","MyNotification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}