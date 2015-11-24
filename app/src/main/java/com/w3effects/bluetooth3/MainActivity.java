package com.w3effects.bluetooth3;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    private EditText mPhoneNumber;
    private Button mCallBtn;
    private Boolean appLauchedFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        Firebase myFirebaseRef = new Firebase("https://bluetooth3.firebaseio.com/");

        myFirebaseRef.child("Task").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                String command = snapshot.getKey();

                String Command = "";
                String PhoneNumber = "";

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //Toast.makeText(MainActivity.this, postSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();

                    if(postSnapshot.getKey().equalsIgnoreCase("command")){
                        Command = postSnapshot.getValue().toString();
                    }
                    if(postSnapshot.getKey().equalsIgnoreCase("number")){
                        PhoneNumber = postSnapshot.getValue().toString();
                    }
                }

                if (Command.isEmpty() || PhoneNumber.isEmpty()) {

                } else {
                    if (Command.equalsIgnoreCase("call")) {

                        if(!appLauchedFirstTime){
                            Toast.makeText(MainActivity.this, Command+"  "+PhoneNumber, Toast.LENGTH_SHORT).show();
                            callHandler(PhoneNumber);
                        }
                    }
                }

                appLauchedFirstTime = false;

            }

            @Override
            public void onCancelled(FirebaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void callHandler(String phoneNumber) {

        //Toast.makeText(MainActivity.this, "Inside Call Module", Toast.LENGTH_SHORT).show();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
    }


}
