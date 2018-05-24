
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ImageView logo;
    TextToSpeech t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SaveSharedPreference.getUserName(MainActivity.this).length() == 0) {
            SaveSharedPreference s = new SaveSharedPreference();
            s.setUserName(getApplicationContext(), "d");
            Timer buttonTimer = new Timer();

            buttonTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            t1.speak("Hello there .. Please wait for few seconds "
                                    , TextToSpeech.QUEUE_FLUSH, null);
                        }
                    });
                }
            }, 4500);
            t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        t1.setLanguage(Locale.US);
                        t1.setSpeechRate(1.4f);
                    }

                }
            });
            logo =findViewById(R.id.logo);
            Animation startFadeOutAnimation5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation);
            logo.startAnimation(startFadeOutAnimation5);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    Intent intent = new Intent(getApplicationContext(),Main4Activity.class);
                    startActivity(intent);
                }
            }, 6000);
        }else{
            logo =findViewById(R.id.logo);
            Animation startFadeOutAnimation5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation);
            logo.startAnimation(startFadeOutAnimation5);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    Intent intent = new Intent(getApplicationContext(),Main4Activity.class);
                    startActivity(intent);
                }
            }, 6000);
        }

    }

}
class SaveSharedPreference
{
    static final String PREF_USER_NAME= "initial";
    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }
    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }
    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }
}
