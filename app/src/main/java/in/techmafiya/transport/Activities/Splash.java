package in.techmafiya.transport.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import in.techmafiya.transport.BounceLoadingView;
import in.techmafiya.transport.OnBoard.OnBoard;
import in.techmafiya.transport.R;
import in.techmafiya.transport.auth.LoginActivity;

public class Splash extends AppCompatActivity {
    protected boolean _active = true;
    protected int _splashTime = 5000; // endStopName to display the splash screen in ms

    private BounceLoadingView loadingView;

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
        }
        setContentView(R.layout.activity_main);

        TextView tx = (TextView) findViewById(R.id.tx) ;
        Typeface custom_font = Typeface.createFromAsset(this.getAssets(),"fonts/Caviar_Dreams_Bold.ttf");
        tx.setTypeface(custom_font);

        loadingView = (BounceLoadingView) findViewById(R.id.loadingView);
        loadingView.addBitmap(R.drawable.bus);
        loadingView.addBitmap(R.drawable.car1);
        loadingView.addBitmap(R.drawable.car);
        loadingView.addBitmap(R.drawable.school);

        loadingView.setShadowColor(Color.LTGRAY);
        loadingView.setDuration(700);
        loadingView.start();


        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                            if(waited>_splashTime-200)
                            {

                            }
                        }
                    }
                } catch (Exception e) {

                } finally {                    {
                    startActivity(new Intent(Splash.this,
                            OnBoard.class));
                }

                    finish();
                }
            }

            ;
        };
        splashTread.start();
    }
}
