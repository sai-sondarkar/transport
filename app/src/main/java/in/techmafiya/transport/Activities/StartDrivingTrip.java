package in.techmafiya.transport.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import in.techmafiya.transport.Adapters.CreatedAdapter;
import in.techmafiya.transport.Adapters.StratDriveAdapter;
import in.techmafiya.transport.Adapters.TripDetailedPanelForStreamingAdapter;
import in.techmafiya.transport.FireBaseInfo.FirebaseInfo;
import in.techmafiya.transport.Models.SingleLocalityDetails;
import in.techmafiya.transport.Models.StopModelForCreateTrip;
import in.techmafiya.transport.Models.StopModelForUser;
import in.techmafiya.transport.Models.TripFullDetails;
import in.techmafiya.transport.Models.UserModel;
import in.techmafiya.transport.R;
import in.techmafiya.transport.auth.LoginActivity;

public class StartDrivingTrip extends AppCompatActivity {
    String UserEnteredID, uid;

    ListView listView;

    private FirebaseAuth mAuth; // for the auth state
    private FirebaseAuth.AuthStateListener mAuthListener;  // authlistner
    public StratDriveAdapter adapter;
    ArrayList<StopModelForCreateTrip> tripList = new ArrayList<StopModelForCreateTrip>();
    List<String> Keys = new ArrayList<String>();
    private boolean check = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenDesign();
        FirebaseAuthModule();
        setContentView(R.layout.activity_start_driving_trip);
        initUiElements();
    }

    public void initUiElements(){
        final EditText id_ed = (EditText) findViewById(R.id.id_editText);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView tx = (TextView) toolbar.findViewById(R.id.tx) ;
        TextView tx1 = (TextView) findViewById(R.id.tx1) ;

        Typeface custom_font = Typeface.createFromAsset(this.getAssets(),"fonts/Caviar_Dreams_Bold.ttf");
        tx.setTypeface(custom_font);
        tx1.setTypeface(custom_font);


        listView = (ListView) findViewById(R.id.listview);

        adapter = new StratDriveAdapter(this,tripList,Keys);
        listView.setAdapter(adapter);




//        FirebaseScheduledList();
    }



    public void  FirebaseScheduledList(){

        adapter.clear();
        FirebaseDatabase.getInstance().getReference().child(FirebaseInfo.TRANSPORT).child(FirebaseInfo.User).child(uid).child(FirebaseInfo.Created).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Keys.add(dataSnapshot.getKey());
                StopModelForCreateTrip mStopModelForCreatedTrip = dataSnapshot.getValue(StopModelForCreateTrip.class);
                adapter.add(mStopModelForCreatedTrip);
                adapter.notifyDataSetChanged();

                if(check==false){
                    ImageView imageView = (ImageView) findViewById(R.id.person);
                    TextView textView = (TextView ) findViewById(R.id.persontext);

                    imageView.setVisibility(View.GONE);listView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    check =true;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void FirebaseAuthModule(){

        mAuth = FirebaseAuth.getInstance(); //  getting the instance in the context

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // UserModel is signed in
                    uid = user.getUid();
                    FirebaseScheduledList();


                } else {
                    // UserModel is signed out
                    Intent intent = new Intent(StartDrivingTrip.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

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

    public void ScreenDesign(){
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

    }


}
