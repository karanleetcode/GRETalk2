
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Main3Activity extends AppCompatActivity  implements RecognitionListener {
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    TextView heading,meaning;
    DBHandler dbHandler = new DBHandler(this);
    TextToSpeech t1;
    String s;
    TextView returnedText;
    ProgressBar progressBar;
    public String text;
    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Maven");
        builder.setMessage("Please connect to the Internet..");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);

                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder;
    }

    public AlertDialog.Builder buildDialog2(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Maven");
        builder.setMessage("Please allow Microphone access..");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);

                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            buildDialog2(this).show();
        }
        setContentView(R.layout.activity_main3);
        meaning =  findViewById(R.id.meaning);
        heading =  findViewById(R.id.heading);
        meaning.setVisibility(View.INVISIBLE);
        heading.setVisibility(View.INVISIBLE);

        returnedText =  findViewById(R.id.textView1);

        Animation startFadeOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation);
        returnedText.startAnimation(startFadeOutAnimation);
        progressBar =  findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);

        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5000);

        final ImageView siriButton =  findViewById(R.id.siriButton);

        siriButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                heading.setVisibility(View.INVISIBLE);
                meaning.setVisibility(View.INVISIBLE);
                progressBar.setIndeterminate(true);
                speech.startListening(recognizerIntent);
            }
        });


        siriButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                progressBar.setIndeterminate(false);
                progressBar.setVisibility(View.INVISIBLE);
                speech.stopListening();
                return true;
            }
        });
        //introduction of maven
        Timer buttonTimer = new Timer();

        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        t1.speak("Hello there .. you can ask me about GRE"
                                , TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
            }
        }, 500);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                    t1.setSpeechRate(1.4f);
                }

            }
        });
    }
    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }
    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");

        Toast.makeText(getApplicationContext(), "Begiun", Toast.LENGTH_LONG).show();
        // progressBar.setIndeterminate(false);
        //progressBar.setMax(10);
    }
    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Oops something went wrong.";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
    }
    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }
    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        //   progressBar.setIndeterminate(true);
        //  toggleButton.setChecked(false);
        Toast.makeText(getApplicationContext(), "after math", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        returnedText.setText(errorMessage);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        text = matches.get(0).trim().toString();

        s = artificial(text);
       // returnedText.setText(s);
        Animation startFadeOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation);
        returnedText.startAnimation(startFadeOutAnimation);
        progressBar.setIndeterminate(false);
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        t1.speak(s, TextToSpeech.QUEUE_FLUSH, null);

                    }
                });
            }
        }, 1800);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                    t1.setSpeechRate(1.0f);
                }
            }
        });
    }
    @Override
    public void onPartialResults(Bundle bundle) {

    }
    @Override
    public void onEvent(int i, Bundle bundle) {

    }
    public String artificial(String text){
        String returnedText = null ;
        boolean indi= false,indi1 = false;

        String inputWords[] = text.split(" ");

        if(text.toLowerCase().contains("what is gre")){
            meaning.setText("The GRE (Graduate Record Exam) Test is the standardised test used to fetch admissions in various graduate schools or business graduate schools in various countries, especially the United States. More than 100,000 graduate school applicants from approximately 160 countries take the GRE General Test at 700 test centres. Aspirants interested in pursuing a master's degree, specialised master's course, MS, MBA, MEM or doctoral degree can sit for the GRE Test. In addition to the GRE revised general test, there are six GRE subjects tests that evaluate candidates’ knowledge in their respective field. GRE test is conducted by ETS®(Educational Testing Service).");
            heading.setText(text);
            heading.setVisibility(View.VISIBLE);
            meaning.setVisibility(View.VISIBLE);
            indi = true;
        }
        if(text.toLowerCase().contains("validity")|| text.toLowerCase().contains("valid")){
            meaning.setText("GRE scores are valid for 5 years. One can request a re-score up to 3 months after your test date.");
            returnedText = "GRE scores are valid for 5 years. One can request a re-score up to 3 months after your test date.";
                    heading.setText(text);
            heading.setVisibility(View.VISIBLE);
            meaning.setVisibility(View.VISIBLE);
            indi = true;
        }
        if(text.toLowerCase().contains("good score")||text.toLowerCase().contains("bad score")|| text.toLowerCase().contains("average score")|| text.toLowerCase().contains("best score")){
            meaning.setText("And the average verbal score of a GRE test taker worldwide, is 150.8, with a standard deviation of 8.5. Now, what this means, is that the world average for the composite GRE score is 151.3 + 150.8 = 302.1. So, if you scored above 302, you're better than half the test takers around the world.");
            returnedText = "And the average verbal score of a GRE test taker worldwide, is 150.8, with a standard deviation of 8.5.";
            heading.setText(text);
            heading.setVisibility(View.VISIBLE);
            meaning.setVisibility(View.VISIBLE);
            indi = true;
            indi1 = true;
        }
        if(indi1 = false){
            if (text.toLowerCase().contains("score")) {
                meaning.setText("Three scores are reported: Verbal Reasoning on a 130–170 score scale, with -point increments; Quantitative Reasoning also on a 130–170 score scale with 1-point increment and Analytical Writing score on a 0–6 score level, in half-point increments. Official scores are mailed in 10–15 days after your test date.");
                heading.setVisibility(View.VISIBLE);
                heading.setText(text);
                meaning.setVisibility(View.VISIBLE);
                indi = true;
            }
        }
        if(text.toLowerCase().contains("for harvard")||text.toLowerCase().contains("harvard university")){
            indi = true;
            meaning.setText("Harvard is an extremely competitive school for graduate applicants. On average, the GRE scores of admitted applicants range from about 155 to 166 for Verbal and 155 to 170 for Quant, with many programs wanting scores in the 160s, or the top 10-15 percent.");
            meaning.setVisibility(View.VISIBLE);
            heading.setVisibility(View.VISIBLE);
            heading.setText(text);
            returnedText = "Harvard is an extremely competitive school for graduate applicants.";
        }
        if(text.toLowerCase().contains("is")&&text.toLowerCase().contains("GRE")&&text.toLowerCase().contains("difficult")){
            indi = true;
            meaning.setText("Compared to the ACT and the SAT, the GRE is typically considered more difficult because, even though the math tested on the GRE is a lower level than the math tested on the SAT and ACT, the GRE has more challenging vocabulary and reading passages, and the math problems have trickier wording or require higher-level. ");
            meaning.setVisibility(View.VISIBLE);
            heading.setVisibility(View.VISIBLE);
            heading.setText(text);
            returnedText = "Compared to the ACT and the SAT, the GRE is typically considered more difficult because, even though the math tested on the GRE is a lower level than the math tested on the SAT and ACT.";
        }
        if(text.toLowerCase().contains("What is on the test")){
            indi  = true;
            meaning.setText("Verbal Reasoning — There are two 30-minute sections, each containing 20 questions." +
                    "Quantitative Reasoning — There are two 35-minute sections, each containing 20 questions." +
                    "Analytical Writing — There is one section with two separately timed tasks: one Analyze an Issue task and one Analyze an Argument task." +
                    "You may also be required to complete an unidentified, unscored and/or research section of the test. The unidentified, unscored section can appear at any point after the Analytical Writing section. The research section is always at the end of the test. The time given to complete these sections varies. You will not receive a score for them.");
            meaning.setVisibility(View.VISIBLE);
            heading.setText(View.VISIBLE);
            heading.setText(text);
            returnedText = "Here is what i got .";
        }
        if(text.toLowerCase().contains("change")&&text.toLowerCase().contains("date")){
            meaning.setText("You must cancel, reschedule or change the location of your test up to four days prior to test day (10 days for those in Mainland China) or your test fee will be forfeited.");
            meaning.setVisibility(View.VISIBLE);
            heading.setVisibility(View.VISIBLE);
            heading.setText(text);
            returnedText = "You must cancel, reschedule or change the location of your test up to four days prior to test day (10 days for those in Mainland China) or your test fee will be forfeited.";
            indi = true;
        }
        if(text.toLowerCase().contains("register for")&&text.toLowerCase().contains("gre")){
            indi= true;
            meaning.setText("You can register for the computer-delivered GRE General Test online or by phone.");
            meaning.setVisibility(View.VISIBLE);
            heading.setText(text);
            heading.setVisibility(View.VISIBLE);
            returnedText = "You can register for the computer-delivered GRE® General Test online or by phone.";
        }
        if(text.toLowerCase().contains("register")&&text.toLowerCase().contains("subject test")){
            returnedText = "You can register for a GRE Subject Test online or by mail";
            indi = true;
            meaning.setText("You can register for a GRE® Subject Test online or by mail");
            meaning.setVisibility(View.VISIBLE);
            heading.setText(text);
            heading.setVisibility(View.VISIBLE);
        }
        if(text.toLowerCase().contains("name")&&text.toLowerCase().contains("my id")&&text.toLowerCase().contains("registration")&&text.toLowerCase().contains("not")&&text.toLowerCase().contains("match")){
            returnedText = "GRE Services can help you with your ID questions. You can call them directly at 1-609-771-7670 or you can email them at gre-info@ets.org";
            indi = true;
            meaning.setText("GRE Services can help you with your ID questions. You can call them directly at 1-609-771-7670 or you can email them at gre-info@ets.org ");
            meaning.setVisibility(View.VISIBLE);
            heading.setText(text);
            heading.setVisibility(View.VISIBLE);
        }
        if(text.toLowerCase().contains("best")&&text.toLowerCase().contains("way")&&text.toLowerCase().contains("prepare")&&text.toLowerCase().contains("gre")){
            returnedText = "Here's what i got";
                indi = true;
            meaning.setText("Ultimately, the way you prepare for the GRE® General Test will be a personal decision. However, a good place to start is by reviewing all of the  official GRE prep materials, including the FREE official POWERPREP Online, which provides two online practice tests that simulate the actual computer-delivered GRE® General Test, including the test-taker friendly design features you’ll encounter on test day, an on-screen calculator and more!");
            meaning.setVisibility(View.VISIBLE);
            heading.setText(text);
            heading.setVisibility(View.VISIBLE);
        }
        if(text.toLowerCase().contains("gre")&&text.toLowerCase().contains("general")){
            indi = true;
            meaning.setText("The GRE General Test is the only admissions test that is accepted by both graduate and business programs worldwide — including most top-ranked M.B.A. programs. Scores are also valid for five years, so even if you don’t know your direction now, you have time to explore and decide.");
            meaning.setVisibility(View.VISIBLE);
            heading.setVisibility(View.VISIBLE);
            heading.setText(text);
            returnedText = "The GRE General Test is the only admissions test that is accepted by both graduate and business programs worldwide — including most top-ranked M.B.A. programs.";
        }
        if(text.toLowerCase().contains("highest")&&text.toLowerCase().contains("GRE")&&text.toLowerCase().contains("score")){
            indi = true;
            meaning.setText("Remember that the Verbal and Quantitative portions of the GRE are scored between 130–170, and the average score falls somewhere around 150-152. The Analytical Writing section of the GRE is scored between 0 and 6 in half-point increments, and the average hits somewhere around 3.5.");
            meaning.setVisibility(View.VISIBLE);
            heading.setVisibility(View.VISIBLE);
            heading.setText(text);
            returnedText = "Remember that the Verbal and Quantitative portions of the GRE are scored between 130–170, and the average score falls somewhere around 150-152.";
        }
        if(text.toLowerCase().contains("time")&&text.toLowerCase().contains("GRE")){
            indi = true;
            meaning.setText("You can take the GRE revised General Test once every 21 days, up to five times within any continuous rolling 12-month period (365 days). This applies even if you canceled your scores on a test taken previously. If you take the paper-delivered GRE revised general test, you can take it as often as it is offered.");
            meaning.setVisibility(View.VISIBLE);
            heading.setVisibility(View.VISIBLE);
            heading.setText(text);
            returnedText = "You can take the GRE revised General Test once every 21 days, up to five times within any continuous rolling 12-month period (365 days).";
        }
        if(text.contains("fee")||text.toLowerCase().contains("fees")){
            indi = true;
            meaning.setText("The registration fee to take the GRE is $195. This is cheaper than the $250 fee that you have to pay for GMAT. ETS is also willing to be flexible on the GRE test fee for test takers who can prove their financial hardship. There are extra charges ($50) if you want to change your center or reschedule the test.");
            meaning.setVisibility(View.VISIBLE);
            heading.setVisibility(View.VISIBLE);
            heading.setText(text);
            returnedText = "The registration fee to take the GRE is $195";
        }
        if(text.toLowerCase().contains("syllabus")){
            returnedText = "GRE is available in two formats. They are GRE General Test and GRE Subject Test. The GRE Test Syllabus is different for both the formats. While the GRE General test measures a candidate’s verbal reasoning, quantitative reasoning, critical thinking and analytical writing skills, the GRE Subject Test is focused on judging the candidate’s expertise in specific fields.";
            meaning.setText("GRE is available in two formats. They are GRE General Test and GRE Subject Test. The GRE Test Syllabus is different for both the formats. While the GRE General test measures a candidate’s verbal reasoning, quantitative reasoning, critical thinking and analytical writing skills, the GRE Subject Test is focused on judging the candidate’s expertise in specific fields.");
            meaning.setVisibility(View.VISIBLE);
            indi = true;
        }
        if(text.toLowerCase().contains("date")||text.toLowerCase().contains("dates")){
            returnedText = "The computer-based GRE Test is offered year-round at Prometric test centres. You can take the GRE Test once every 21 days, up to five times within a 12-month period. This applies even if you cancelled your scores on a test taken previously. However, you have to first create a ‘My GRE Account’ in order to register for a specific date.";
            meaning.setText("The computer-based GRE Test is offered year-round at Prometric test centres. You can take the GRE Test once every 21 days, up to five times within a 12-month period. This applies even if you cancelled your scores on a test taken previously. However, you have to first create a ‘My GRE Account’ in order to register for a specific date.");
            meaning.setVisibility(View.VISIBLE);
            indi = true;
        }
        if(text.contains("what is the meaning of") || text.contains("what's the meaning of")){
            String word=null;
            if(text.contains("what is the meaning of")){
                word = text.substring(23);
                int exists = dbHandler.wordExistsForMaven(word);

                if(exists == 1){
                    returnedText = "Meaning of "+word+" is ";
                    final String[] data = dbHandler.getAllDetailsOfWord(word).split(":");
                    heading.setVisibility(View.VISIBLE);
                    meaning.setVisibility(View.VISIBLE);
                    heading.setText(word);
                    meaning.setText(data[2]);
                }
                else{
                    returnedText = "Meaning of "+word+" is ";
                    heading.setVisibility(View.VISIBLE);
                    meaning.setVisibility(View.VISIBLE);
                    heading.setText(word);
                    new ParseURL().execute(word);
                }
            }
            else if(text.contains("what's is the meaning of")){
                word = text.substring(23);
                int exists = dbHandler.wordExistsForMaven(word);
                if(exists == 1){
                    returnedText = "Meaning of "+word+" is ";
                    final String[] data = dbHandler.getAllDetailsOfWord(word).split(":");
                    heading.setVisibility(View.VISIBLE);
                    meaning.setVisibility(View.VISIBLE);
                    heading.setText(word);
                    meaning.setText(data[2]);
                }
                else{
                    returnedText = "Meaning of "+word+" is ";
                    heading.setVisibility(View.VISIBLE);
                    meaning.setVisibility(View.VISIBLE);
                    heading.setText(word);
                    new ParseURL().execute(word);
                }
            }
            indi = true;
        }
        if(indi == false){
            returnedText = "Sorry I can't undertand what you said.. You may try to rephrase it.";
        }
        return returnedText;


    }
    class ParseURL extends AsyncTask<String, Void, Void> {
        public String s;

        @Override
        protected Void doInBackground(String... strings) {

            try {
                String x = strings[0].toString();
                org.jsoup.nodes.Document doc = Jsoup.connect("https://en.oxforddictionaries.com/definition/" + x).get();
                Elements element = doc.getElementsByClass("ind");

                s = element.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @TargetApi(24)
        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
            String j = s.replaceAll("<span class=\"ind\">", "*").replaceAll("</span>", "\n").
                    replaceAll("<strong class=\"phrase\">", "").replaceAll("</strong>", "");

            meaning.setText(j);
        }

    }
}
