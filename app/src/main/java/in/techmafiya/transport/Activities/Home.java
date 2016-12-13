package in.techmafiya.transport.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


import in.techmafiya.transport.R;
import in.techmafiya.transport.auth.LoginActivity;

public class Home extends AppCompatActivity {



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

        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(" ");
        TextView tx = (TextView) toolbar.findViewById(R.id.tx) ;
        Typeface custom_font = Typeface.createFromAsset(this.getAssets(),"fonts/Caviar_Dreams_Bold.ttf");
        tx.setTypeface(custom_font);

        TextView t1 = (TextView) findViewById(R.id.a1);
        TextView t2 = (TextView) findViewById(R.id.a2);
        TextView t3 = (TextView) findViewById(R.id.a3);
        TextView t4 = (TextView) findViewById(R.id.a4);
        t1.setTypeface(custom_font);
        t2.setTypeface(custom_font);
        t3.setTypeface(custom_font);
        t4.setTypeface(custom_font);
    }

    public void OnClickSearch(View view){
        Intent intent = new Intent(Home.this,SearchTrip.class);
        startActivity(intent);
    }

    public void OnClickCreate(View view){
        Intent intent = new Intent(Home.this,CreateTrip.class);
        startActivity(intent);
    }

    public void OnClickDrive(View view){
        Intent intent = new Intent(Home.this,StartDrivingTrip.class);
        startActivity(intent);
    }

    public void OnClickAware(View view){
        Intent intent = new Intent(Home.this,AwareSpotMap.class);
        startActivity(intent);
    }

    public void OnClickLogout(View view){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this,"Logout Sucessful",Toast.LENGTH_SHORT);
        Intent intent = new Intent(Home.this,LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.logo)
                .setTitle("Transport")
                .setMessage("Are you sure you want to close Transport ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

}
