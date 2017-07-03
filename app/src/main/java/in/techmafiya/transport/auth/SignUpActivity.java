package in.techmafiya.transport.auth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import dmax.dialog.SpotsDialog;
import in.techmafiya.transport.Activities.Home;
import in.techmafiya.transport.R;


public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth; // for the auth state
    String TAG = "Signup";

    EditText email_EditText;
    EditText password_EditText;
    EditText name_EditText;
    FirebaseUser user;

    String name;


    private FirebaseAuth.AuthStateListener mAuthListener;  // authlistner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            boolean shouldChangeStatusBarTintToDark = true;

            if (shouldChangeStatusBarTintToDark) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                // We want to change tint color to white again.
                // You can also record the flags in advance so that you can turn UI back completely if
                // you have set other flags before, such as translucent or full screen.
                decor.setSystemUiVisibility(0);

            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDarkL));
            }
        }


        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance(); //  getting the instance in the context
        intiUIelements();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // UserModel is signed in

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // UserModel is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    void intiUIelements(){  //ui element declaration
        email_EditText = (EditText) findViewById(R.id.email);
        password_EditText = (EditText) findViewById(R.id.password);
        name_EditText = (EditText) findViewById(R.id.name);

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void onClick(View view ) {  //method for the login button

        String email = email_EditText.getText().toString();
        String password = password_EditText.getText().toString();
        name= name_EditText.getText().toString();
        if(email.equals("")||password.equals("")||name.equals("")){
            Toast.makeText(SignUpActivity.this, "All details are complasary",
                    Toast.LENGTH_SHORT).show();
            return;
        }else {
            new SpotsDialog(this, R.style.Custom_signup).show();
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        UserProfileChangeRequest userProfile = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        user.updateProfile(userProfile)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "UserModel profile updated.");
                                        }
                                    }
                                });
                        user.sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Email sent.");
                                        }
                                    }
                                });


                        Intent intent = new Intent(SignUpActivity.this, Home.class);

                        startActivity(intent);
                        finish();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Auth failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signup(View view){
        finish();

    }
}
