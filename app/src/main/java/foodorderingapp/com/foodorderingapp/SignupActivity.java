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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import foodorderingapp.com.foodorderingapp.Common.Common;
import foodorderingapp.com.foodorderingapp.Model.User;

public class SignupActivity extends AppCompatActivity {
    EditText edtEmail,edtPassword,edtName,edtPhone;
    Button btnsignUp;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnSignIn=(Button) findViewById(R.id.signIn);

        edtEmail=(EditText) findViewById(R.id.edtEmail);
        edtPassword=(EditText)findViewById(R.id.edtPassword);
        edtName=(EditText)findViewById(R.id.edtName);
        edtPhone=(EditText)findViewById(R.id.edtPhone);

        btnsignUp=(Button)findViewById(R.id.btnSignUp);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, SignIn.class));
            }
        });

        btnsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    private void registerUser() {
        if (Common.isConnectedToInternet(getBaseContext())) {

            final String email = edtEmail.getText().toString().trim();
            final String password = edtPassword.getText().toString().trim();
            final String name = edtName.getText().toString().trim();
            final String phone = edtPhone.getText().toString().trim();
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
            if (phone.isEmpty()) {
                edtPhone.setError("Phone number is required");
                edtPhone.requestFocus();
                return;
            }

            if (!Patterns.PHONE.matcher(phone).matches())//If phone is not valid type
            {
                edtPhone.setError("Please enter a valid Phone");
                edtPhone.requestFocus();
                return;
            }
            if (name.isEmpty()) {
                edtName.setError("Name is required");
                edtName.requestFocus();
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
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() //onclicklistener to detect the status
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        User user = new User(name, email, password, phone);
                        FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SignupActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                    // TODO It has to be moved to the main activity after successful logging in
                                    startActivity(new Intent(SignupActivity.this, DisplayActivity.class));
                                    finish();

                                } else {
                                    Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException)//checks if the exception is already registered
                        {
                            Toast.makeText(SignupActivity.this, "Email already exits", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }
        else
        {
            Toast.makeText(SignupActivity.this,"Please check your connection",Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
