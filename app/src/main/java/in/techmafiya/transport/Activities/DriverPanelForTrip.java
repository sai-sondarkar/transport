package in.techmafiya.transport.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import in.techmafiya.transport.Adapters.DriverCheckListAdapter;
import in.techmafiya.transport.Adapters.TripDetailedPanelForStreamingAdapter;
import in.techmafiya.transport.FireBaseInfo.FirebaseInfo;
import in.techmafiya.transport.Models.SingleLocalityDetails;
import in.techmafiya.transport.Models.StopModelForUser;
import in.techmafiya.transport.Models.UserModel;
import in.techmafiya.transport.R;
import in.techmafiya.transport.auth.LoginActivity;

public class DriverPanelForTrip extends AppCompatActivity {

    private FirebaseAuth mAuth; // for the auth state
    private FirebaseAuth.AuthStateListener mAuthListener;  // authlistner

    ListView listView;
    SingleLocalityDetails StopName;
    String uid, userNotificatioToken,SelectedStopName,SelectedStopTime;
    int SelectedItem;
    String userRequestedID,userTokenId,message;
    public DriverCheckListAdapter adapter;
    ArrayList<SingleLocalityDetails> tripList = new ArrayList<SingleLocalityDetails>();
    List<String> User_Token_to_Notify = new ArrayList<String>();
    List<String> Keys = new ArrayList<String>();
    List<StopModelForUser> ScheduledTrip = new ArrayList<StopModelForUser>();
    com.github.clans.fab.FloatingActionButton fab;
    com.github.clans.fab.FloatingActionMenu menu;


    UserModel mUserModel;
    private boolean OneTimeUpdated = false;
    private boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenDesign();
        GetParamsFromPreviosActivity();
        setContentView(R.layout.activity_driver_panel_for_trip);
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
                    Intent intent = new Intent(DriverPanelForTrip.this,LoginActivity.class);
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

        fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearCheckList();

            }
        });

        menu = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);

        listView = (ListView) findViewById(R.id.listview);
        adapter = new DriverCheckListAdapter(this,tripList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedStopName = tripList.get(i).getStopPoint();
                SelectedStopTime = tripList.get(i).getEta();
                SelectedItem = i;
                if(SelectedItem < tripList.size()-1) {
                    User_Token_to_Notify = tripList.get(i + 1).getUidNotify();
                    if (User_Token_to_Notify != null) {
                        int size = User_Token_to_Notify.size();
                        for (int j = 0; j < size; j++) {
                            userTokenId = User_Token_to_Notify.get(j);
                            message = "Your Transport is at " + SelectedStopName + " Will be arriving shortly at your place";
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    new SendPostRequest().execute();
                                }
                            });
                            thread.run();
                        }
                    }
                }

                FirebaseCheckStop();

            }
        });
    }
    public void FirebaseListUpdate(){

        adapter.clear();

        FirebaseDatabase.getInstance().getReference().child(FirebaseInfo.TRANSPORT).child(FirebaseInfo.TRIPS).child(userRequestedID).child(FirebaseInfo.Stoplist).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Keys.add(dataSnapshot.getKey());
                if(check==false){
                    ImageView imageView = (ImageView) findViewById(R.id.person);
                    TextView textView = (TextView ) findViewById(R.id.persontext);
                    CardView card = (CardView) findViewById(R.id.start);
                    card.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    check =true;
                }
                SingleLocalityDetails mSingleLocality = dataSnapshot.getValue(SingleLocalityDetails.class);
                adapter.add(mSingleLocality);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            SingleLocalityDetails mSingleLocalityDetails = dataSnapshot.getValue(SingleLocalityDetails.class);
                int pos = adapter.getPosition(mSingleLocalityDetails);
                adapter.remove(mSingleLocalityDetails);
                adapter.insert(mSingleLocalityDetails,pos);
                adapter.notifyDataSetChanged();
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

    public void FirebaseCheckStop(){
        SingleLocalityDetails  mSingleLocalityDetails;
        if(tripList.get(SelectedItem).getUidNotify()==null){
            mSingleLocalityDetails = new SingleLocalityDetails(false,true,tripList.get(SelectedItem).getStopPoint(),tripList.get(SelectedItem).getEta(),null);
        }else{
         mSingleLocalityDetails = new SingleLocalityDetails(false,true,tripList.get(SelectedItem).getStopPoint(),tripList.get(SelectedItem).getEta(),tripList.get(SelectedItem).getUidNotify());}

        FirebaseDatabase.getInstance().getReference().child(FirebaseInfo.TRANSPORT).child(FirebaseInfo.TRIPS).child(userRequestedID).child(FirebaseInfo.Stoplist).child(String.valueOf(SelectedItem).toString()).setValue(mSingleLocalityDetails);
    }

    public void startTrip(View view){

        User_Token_to_Notify = tripList.get(0).getUidNotify();
        if(User_Token_to_Notify != null){
            int size = User_Token_to_Notify.size();
            for(int j = 0 ; j<size;j++) {
                userTokenId = User_Token_to_Notify.get(j);
                message = "Your Transport is at " + SelectedStopName + " Will be arriving shortly at your place";
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        new SendPostRequest().execute();
                    }
                });
                thread.run();

            }

        }

    }
//    public void DialogeForConfirmation (){
//        new AlertDialog.Builder(this)
//                .setIcon(R.drawable.logo)
//                .setTitle("Confirmation")
//                .setMessage("Are you sure to get notified when your transport is about to reach " + SelectedStopName )
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        FirbaseUidNotifyUpdate();
//                    }
//                })
//                .setNegativeButton("No", null)
//                .show();
//    }
//
//    public void FirbaseUidNotifyUpdate (){
//
//        if(tripList.get(SelectedItem).getUidNotify() == null){
//            User_Token_to_Notify.add(userNotificatioToken);
//        }
//        else {
//            User_Token_to_Notify = tripList.get(SelectedItem).getUidNotify();
//            User_Token_to_Notify.add(userNotificatioToken);}
//
//        SingleLocalityDetails mSingleLocalityDetails = new SingleLocalityDetails(false,false,tripList.get(SelectedItem).stopPoint,tripList.get(SelectedItem).eta,User_Token_to_Notify);
//        FirebaseDatabase.getInstance().getReference().child(FirebaseInfo.TRANSPORT).child(FirebaseInfo.TRIPS).child(userRequestedID).child(FirebaseInfo.Stoplist).child(Keys.get(SelectedItem)).setValue(mSingleLocalityDetails);
//
//        StopModelForUser mStopModelForUser = new StopModelForUser(SelectedStopName,SelectedStopTime);
//        FirebaseDatabase.getInstance().getReference().child(FirebaseInfo.TRANSPORT).child(FirebaseInfo.User).child(uid).child(FirebaseInfo.Schedule).child(userRequestedID).setValue(mStopModelForUser);
//
//
//
//    }

    public void ClearCheckList(){
            int i = adapter.getCount();
            for(int j=0;j<i;j++){
                final SingleLocalityDetails trip = new SingleLocalityDetails(false,false,tripList.get(j).getStopPoint(),tripList.get(j).getEta(),tripList.get(j).getUidNotify());
//                adapter.remove(tripList.get(j));
                FirebaseDatabase.getInstance().getReference().child(FirebaseInfo.TRANSPORT).child(FirebaseInfo.TRIPS).child(userRequestedID).child(FirebaseInfo.Stoplist).child(String.valueOf(j).toString()).setValue(trip);
                adapter.notifyDataSetChanged();
            }
        menu.close(true);

    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try{

                URL url = new URL("http://139.59.9.118/send");

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("to", userTokenId);
                postDataParams.put("title", "Transports");
                postDataParams.put("body", message);
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {

//            Toast.makeText(getApplicationContext(), result,
//                    Toast.LENGTH_LONG).show();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
