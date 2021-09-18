package com.CleverTapTest.clevertapandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    CleverTapAPI cleverTapAPI;
    private String str_fcmid = "";
    static String TAG = MainActivity.class.getSimpleName();
    private Button button;
    private EditText editTextTextEmailAddress,editTextTextPersonName;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(MainActivity.this);

        button =findViewById(R.id.button);
        editTextTextEmailAddress =findViewById(R.id.editTextTextEmailAddress);
        editTextTextPersonName =findViewById(R.id.editTextTextPersonName);
        cleverTapAPI = CleverTapAPI.getDefaultInstance(getApplicationContext());
        CleverTapAPI.createNotificationChannel(getApplicationContext(),"200","MyChannel","Your Channel Description", NotificationManager.IMPORTANCE_MAX,true);
        str_fcmid = FirebaseInstanceId.getInstance().getToken();

        FirebaseMessaging.getInstance().subscribeToTopic("general").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG,"Success");
                Log.e(TAG,"FCMID: "+str_fcmid);
//                Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email="", name="";
                email = editTextTextEmailAddress.getText().toString();
                name = editTextTextPersonName.getText().toString();

                if(email==null || email.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                return;
                }
                if(name==null|| name.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                return;
                }
                showProgressDialog();
                // event with properties
                HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
                prodViewedAction.put("Product ID", "1");
                prodViewedAction.put("Product Name", "CleverTap");
                prodViewedAction.put("Product Image", "https://d35fo82fjcw0y8.cloudfront.net/2018/07/26020307/customer-success-clevertap.jpg");
                prodViewedAction.put("Name", name);
                prodViewedAction.put("Email", email);

                cleverTapAPI.pushEvent("Product viewed", prodViewedAction);
                Bundle bundle=new Bundle();
                bundle.putString("Title","TEST TITLE");
//                CleverTapAPI.createNotification(getApplicationContext(),bundle);
                CleverTapAPI.getDefaultInstance(getApplicationContext()).pushNotificationViewedEvent(bundle);
                ResetFields();

            }
        });
    }

    private void ResetFields() {
        editTextTextEmailAddress.setText("");
        editTextTextPersonName.setText("");
    }
    private void showProgressDialog(){
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Your data has been sent successfully.", Toast.LENGTH_SHORT).show();
            }
        }, 2000); // 3000 milliseconds delay
    }
}