package foodorderingapp.com.foodorderingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import foodorderingapp.com.foodorderingapp.Common.Common;
import foodorderingapp.com.foodorderingapp.Model.User;
import foodorderingapp.com.foodorderingapp.Server.DisplayActivityServer;

public class SignIn extends AppCompatActivity {
    EditText edtEmail,edtPassword;
    Button btnsignIn;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btnSignUp=(Button) findViewById(R.id.btnSignUp);

        edtEmail=(EditText)findViewById(R.id.edtEmail);
        edtPassword=(EditText)findViewById(R.id.edtPassword);

        btnsignIn=(Button)findViewById(R.id.btnSignIn);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignupActivity.class));
            }
        });

        btnsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignIn();
            }
        });
    }

    private void userSignIn() {

        if (Common.isConnectedToInternet(getBaseContext())) {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            if (email.isEmpty()) {
                edtEmail.setError("Email is required");
                edtEmail.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())//If email is not valid type
            {
                edtEmail.setError("Please enter a valid email");
                edtEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                edtPassword.setError("Password is required");
                edtPassword.requestFocus();
                return;
            }

            if (password.length() < 6)//firebase accepts password for more than 6 characters
            {
                edtPassword.setError("Minimum length of password should be 6");
                edtPassword.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);


            mAuth.signInWithEmailAndPassword(email, password);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignIn.this, "You are logged in", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(mAuth.getInstance().getCurrentUser().getUid()).exists()) {
                                    User user = dataSnapshot.child(mAuth.getInstance().getCurrentUser().getUid()).getValue(User.class);
                                    if (user.getIsStaff().equals("true")) {
                                        //has to move to server Display activity
                                        Intent intent = new Intent(SignIn.this, DisplayActivityServer.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(SignIn.this, DisplayActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        // TODO It has to be moved to the main activity after successful logging in

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(SignIn.this,"Please check your connection",Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
