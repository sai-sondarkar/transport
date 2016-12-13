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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import in.techmafiya.transport.Adapters.TripDetailedPanelForStreamingAdapter;
import in.techmafiya.transport.FireBaseInfo.FirebaseInfo;
import in.techmafiya.transport.Models.SingleLocalityDetails;
import in.techmafiya.transport.Models.StopModelForCreateTrip;
import in.techmafiya.transport.Models.StopModelForUser;
import in.techmafiya.transport.Models.TripFullDetails;
import in.techmafiya.transport.Models.UserModel;
import in.techmafiya.transport.R;
import in.techmafiya.transport.auth.LoginActivity;

public class CreateNewTripDetailed extends AppCompatActivity {
    private FirebaseAuth mAuth; // for the auth state
    private FirebaseAuth.AuthStateListener mAuthListener;  // authlistner

    ListView listView;
    List<SingleLocalityDetails> trips = new ArrayList<SingleLocalityDetails>();
    SingleLocalityDetails StopName;
    String uid, userNotificatioToken,SelectedStopName,SelectedStopTime;
    int SelectedItem;
    String userRequestedID;
    public TripDetailedPanelForStreamingAdapter adapter;
    ArrayList<SingleLocalityDetails> tripList = new ArrayList<SingleLocalityDetails>();
    List<String> User_Token_to_Notify = new ArrayList<String>();
    List<String> Keys = new ArrayList<String>();
    List<StopModelForUser> ScheduledTrip = new ArrayList<StopModelForUser>();

    EditText nameEditText,etaEditText;
    TripFullDetails mTripFullDetails;

    UserModel mUserModel;
    private boolean OneTimeUpdated = false;
    private boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenDesign();
        GetParamsFromPreviosActivity();
        setContentView(R.layout.activity_create_new_trip_detailed);
        FirebaseAuthModule();
        InitUiElements();
        FirebaseListUpdate();
    }

    public void GetParamsFromPreviosActivity(){
        Bundle bundle = getIntent().getExtras();
        userRequestedID = bundle.getString("user_entered_id");
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
                    userNotificatioToken = FirebaseInstanceId.getInstance().getToken();

                } else {
                    // UserModel is signed out
                    Intent intent = new Intent(CreateNewTripDetailed.this,LoginActivity.class);
                    startActivity(intent);
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
    public void InitUiElements(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView tx = (TextView) toolbar.findViewById(R.id.tx);
        Typeface custom_font = Typeface.createFromAsset(this.getAssets(),"fonts/Caviar_Dreams_Bold.ttf");
        tx.setTypeface(custom_font);

        listView = (ListView) findViewById(R.id.listview);
        adapter = new TripDetailedPanelForStreamingAdapter(this,tripList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedStopName = tripList.get(i).getStopPoint();
                SelectedStopTime = tripList.get(i).getEta();
                SelectedItem = i;

                DialogeForConfirmation();
            }
        });

        mTripFullDetails = new TripFullDetails();
        FloatingActionButton mFloatingActionButton = (FloatingActionButton) findViewById(R.id.id_button);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogeShow();
            }
        });
    }
    public void FirebaseListUpdate(){

        adapter.clear();
        FirebaseDatabase.getInstance().getReference().child(FirebaseInfo.TRANSPORT).child(FirebaseInfo.TRIPS).child(userRequestedID).child(FirebaseInfo.Stoplist).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Keys.add(dataSnapshot.getKey());
                SingleLocalityDetails mSingleLocality = dataSnapshot.getValue(SingleLocalityDetails.class);
                if(mSingleLocality== null){
                    Toast.makeText(CreateNewTripDetailed.this,"ID Not Found",Toast.LENGTH_SHORT).show();
                    finish();
                }
                if(check==false){
                    ImageView imageView = (ImageView) findViewById(R.id.person);
                    TextView textView = (TextView ) findViewById(R.id.persontext);

                    imageView.setVisibility(View.GONE);listView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    check =true;
                }
                adapter.add(mSingleLocality);
                adapter.notifyDataSetChanged();
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
    public void DialogeForConfirmation (){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.logo)
                .setTitle("Confirmation")
                .setMessage("Are you sure to get notified when your transport is about to reach " + SelectedStopName )
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirbaseUidNotifyUpdate();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void FirbaseUidNotifyUpdate (){

        if(tripList.get(SelectedItem).getUidNotify() == null){
            User_Token_to_Notify.add(userNotificatioToken);
        }
        else {
            User_Token_to_Notify = tripList.get(SelectedItem).getUidNotify();
            User_Token_to_Notify.add(userNotificatioToken);}

        SingleLocalityDetails mSingleLocalityDetails = new SingleLocalityDetails(false,false,tripList.get(SelectedItem).stopPoint,tripList.get(SelectedItem).eta,User_Token_to_Notify);
        FirebaseDatabase.getInstance().getReference().child(FirebaseInfo.TRANSPORT).child(FirebaseInfo.TRIPS).child(userRequestedID).child(FirebaseInfo.Stoplist).child(Keys.get(SelectedItem)).setValue(mSingleLocalityDetails);

        StopModelForUser mStopModelForUser = new StopModelForUser(SelectedStopName,SelectedStopTime);
        FirebaseDatabase.getInstance().getReference().child(FirebaseInfo.TRANSPORT).child(FirebaseInfo.User).child(uid).child(FirebaseInfo.Schedule).child(userRequestedID).setValue(mStopModelForUser);



    }

    public void dialogeShow(){

        android.app.AlertDialog.Builder addBookingDialog = displayDialogToAddBooking();

        // set dialog message
        addBookingDialog
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final SingleLocalityDetails mSingleLocalityDetails = new SingleLocalityDetails(false,false,nameEditText.getText().toString(),etaEditText.getText().toString(),null);
                                trips.add(mSingleLocalityDetails);
                                mTripFullDetails.setStoplist(trips);
                                FirebaseDatabase.getInstance().getReference().child(FirebaseInfo.TRANSPORT).child(FirebaseInfo.TRIPS).child(userRequestedID).setValue(mTripFullDetails);
                                int size = mTripFullDetails.getStoplist().size();
                                StopModelForCreateTrip mStopModelForCreateTrip = new StopModelForCreateTrip(mTripFullDetails.getStoplist().get(0).getStopPoint(),mTripFullDetails.getStoplist().get(size-1).getStopPoint());
                                FirebaseDatabase.getInstance().getReference().child(FirebaseInfo.TRANSPORT).child(FirebaseInfo.User).child(uid).child(FirebaseInfo.Created).child(userRequestedID).setValue(mStopModelForCreateTrip);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        android.app.AlertDialog alertDialog = addBookingDialog.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.entertipmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.Done : finish();
                break;
            default:
                Toast.makeText(this,"No actionFound",Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private android.app.AlertDialog.Builder displayDialogToAddBooking() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.details_promt_new_stop, null);

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                this);
        alertDialogBuilder.setView(promptsView);

        nameEditText = (EditText) promptsView
                .findViewById(R.id.editTextName);

        etaEditText = (EditText) promptsView
                .findViewById(R.id.editTextTime);

        return alertDialogBuilder;
    }

}
