package in.techmafiya.transport.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import in.techmafiya.transport.R;


public class AwareSpotMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    com.github.clans.fab.FloatingActionButton fab;
    com.github.clans.fab.FloatingActionMenu menu;
    private EditText nameEditText;
    private EditText despEditText;
    private EditText cityEditText;
    Geocoder geocoder;

    Context context;

    private String title,desp,city;
    private double latPoint,lngPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenDesign();
        setContentView(R.layout.activity_aware_spot_map);
        InitUiElements();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        new GetDataRequest().execute();

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

        menu = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogeForConfirmation();
            }
        });
    }

    public void DialogeForConfirmation (){
        menu.close(true);
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.logo)
                .setTitle("Identity will a be Secret")
                .setMessage("Want to report Semi Anonymously ?" )
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                            @Override
                            public void onMapClick(LatLng point) {
                                // TODO Auto-generated method stub

                                mMap.addMarker(new MarkerOptions().position(point));
                                latPoint = point.latitude;
                                lngPoint = point.longitude;

                                dialogeShow();
                                mMap.setOnMapClickListener(null);
                            }
                        });
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void dialogeShow(){

        android.app.AlertDialog.Builder addBookingDialog = displayDialogToAddBooking();

        // set dialog message
        addBookingDialog
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                city = cityEditText.getText().toString();
                                desp = despEditText.getText().toString();
                                if(city.equals("")||desp.equals("")){
                                        Toast.makeText(context,"City or description is empty",Toast.LENGTH_SHORT);
                                }else {
                                    new SendPostRequest().execute();
                                    new GetDataRequest().execute();
                                }
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

    private android.app.AlertDialog.Builder displayDialogToAddBooking() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.details_promt_new_map, null);

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                this);
        alertDialogBuilder.setView(promptsView);

        despEditText = (EditText) promptsView
                .findViewById(R.id.editTextTime);

        cityEditText = (EditText) promptsView
                .findViewById(R.id.editTextCity);

        return alertDialogBuilder;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                MarkerOptions spot = new MarkerOptions().position(sydney).title("Aware-spot" );
        spot.icon(BitmapDescriptorFactory.fromResource(R.drawable.spot));
        mMap.addMarker(spot);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(25.153900, 78.013092))
                .zoom(4)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void markOnMap(String id , String Title,String Despcription,String city,double lat, double lng){
        LatLng sydney = new LatLng(lat, lng);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        MarkerOptions spot = new MarkerOptions().position(sydney).title("Aware-spot" + city );
        spot.icon(BitmapDescriptorFactory.fromResource(R.drawable.spot));
        spot.snippet("" + Despcription);
        mMap.addMarker(spot);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(25.153900,78.013092))
                .zoom(4)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://139.59.9.118/location");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("title", "Aware spot");
                postDataParams.put("description", desp);
                postDataParams.put("city", city);
                postDataParams.put("lat", latPoint);
                postDataParams.put("lng", lngPoint);


                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);



                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
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

    public class GetDataRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {
            String result=null;
            String response = null;
            URL url;

            HttpURLConnection urlConnection = null;
            try {
                url = new URL("http://139.59.9.118/location");

                urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader isw = new InputStreamReader(in);

                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    response = response + current;
                    System.out.print(current);
                }



            } catch (Exception e) {
                result = e.getMessage().toString();
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    Log.e("DatafrmServer : ",response);
                    urlConnection.disconnect();
                    return result = response;
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            String title,id,city,description;
            double lat,lng;
            mMap.clear();
//            Toast.makeText(getApplicationContext(), result,
//                    Toast.LENGTH_LONG).show();
            result =  result.substring(4);
            try {
                JSONObject obj = new JSONObject(result);

                JSONArray jsonArray = obj.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject explrObject = jsonArray.getJSONObject(i);
                    Log.e("datalist",explrObject.toString());
                    title = explrObject.getString("title");
                    id = explrObject.getString("_id");
                    city = explrObject.getString("city");
                    description = explrObject.getString("description");
                    lng = explrObject.getDouble("lng");
                    lat = explrObject.getDouble("lat");
                    markOnMap(id,title,description,city,lat,lng);
                }

                Log.d("My App", obj.toString());

            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON:" + result);
            }
        }
    }
}
