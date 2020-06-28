package foodorderingapp.com.foodorderingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

import foodorderingapp.com.foodorderingapp.Server.DisplayActivityServer;

public class SplashActivity extends AppCompatActivity {

     FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        mAuth=FirebaseAuth.getInstance();

        TimerTask newtask;
        if (checkFirstRun()==false && mAuth.getCurrentUser()==null)
        {
            newtask=new TimerTask() {
                @Override
                public void run()
                {
                    startActivity(new Intent(SplashActivity.this,SignupLoginPage.class));
                    finish();
                }
            };
        }
        else if(checkFirstRun()==false && mAuth.getCurrentUser()!=null && mAuth.getCurrentUser().getUid().equals("jwZLsLGEClfvWtkkqlJlH2I0NB62"))
        {
            newtask=new TimerTask() {
                @Override
                public void run()
                {
                    startActivity(new Intent(SplashActivity.this,DisplayActivityServer.class));
                    finish();
                }
            };
        }
        else if(checkFirstRun()==false && mAuth.getCurrentUser()!=null)
        {
            newtask=new TimerTask() {
                @Override
                public void run()
                {
                    startActivity(new Intent(SplashActivity.this,DisplayActivity.class));
                    finish();
                }
            };
        }
        else
        {
            newtask=new TimerTask() {
                @Override
                public void run() {

                    startActivity(new Intent(SplashActivity.this,ViewPagerActivity.class));
                    finish();

                }
            };
        }

        Timer myTimer=new Timer();
        myTimer.schedule(newtask,1000);
    }

    private  boolean checkFirstRun() {

        String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);



        // Get saved version code

        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode)
        {

            // This is just a normal run
            return false;

        }
        else if (savedVersionCode == DOESNT_EXIST)
        {
            // Update the shared preferences with the current version code
            prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
            return true;
            // TODO This is a new install (or the user cleared the shared preferences)

        }
        else if (currentVersionCode > savedVersionCode)
        {
            // Update the shared preferences with the current version code
            prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
            return false;
            // TODO This is an upgrade
        }

        return true;

    }

    }

