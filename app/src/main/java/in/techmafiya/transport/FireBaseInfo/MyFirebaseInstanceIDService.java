package in.techmafiya.transport.FireBaseInfo;

/**
 * Created by ABCD on 21-Oct-16.
 */
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Belal on 5/27/2016.
 */


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

    }

    private String sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project

        return token;
    }
}