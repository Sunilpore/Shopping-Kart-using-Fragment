package com.example.sunil.cartadd.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sunil.cartadd.Database.DatabaseHandler;
import com.example.sunil.cartadd.R;
import com.example.sunil.cartadd.Model.UserModel;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG ="data";
    DatabaseHandler db;
    EditText fname,uname,pass,cpass;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fname= (EditText) findViewById(R.id.tv_fname);
        uname= (EditText) findViewById(R.id.tv_uname);
        pass= (EditText) findViewById(R.id.tv_pass);
        cpass= (EditText) findViewById(R.id.tv_cpass);

        signup= (Button) findViewById(R.id.bt_signup);

        db=new DatabaseHandler(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true); //For back button changes done at Manifest file by extending parentActivityName=".MainActivity" to "SignupActivity"

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tfname=fname.getText().toString();
                String tuname=uname.getText().toString();
                String tpass=pass.getText().toString();
                String tcpass=cpass.getText().toString();

                Log.d(TAG,tuname);
                if(tfname.length()!=0 && tuname.length()!=0 && tpass.length()!=0){

                    if(tpass.equals(tcpass)){

                        boolean inserted = db.addUserData(new UserModel(tfname,tuname,tpass));
                        if (inserted) {
                            Toast.makeText(SignupActivity.this, "SignUp successfully", Toast.LENGTH_LONG).show();

                            Intent i = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else
                            Toast.makeText(SignupActivity.this, "Failed to SignUp", Toast.LENGTH_LONG).show();

                    }
                    else
                        Toast.makeText(SignupActivity.this, "Enter the same password", Toast.LENGTH_LONG).show();

                }

                else if(tfname.length()==0)
                    Toast.makeText(SignupActivity.this, "Please,fill the Full Name", Toast.LENGTH_LONG).show();
                else if(tuname.length()==0)
                    Toast.makeText(SignupActivity.this, "Enter the username", Toast.LENGTH_LONG).show();
                else if(tpass.length()==0)
                    Toast.makeText(SignupActivity.this, "Enter the password", Toast.LENGTH_LONG).show();
                else if(tcpass.length()==0)
                    Toast.makeText(SignupActivity.this, "Confirm the Password", Toast.LENGTH_LONG).show();

            }
        });


    }



}