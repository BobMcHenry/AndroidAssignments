package com.bobmchenry.ad340.week3assignment;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    boolean isRecording;
    MediaRecorder mRecorder;
    final String mFile =
            Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MUSIC) + "/test.3gp";

    public static final String LOG_TAG = "#######################";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecorder = new MediaRecorder();


        final TextView recView = (TextView) findViewById(R.id.textView);

        assert recView != null;
        recView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        int meAction = motionEvent.getActionMasked();


                        switch (meAction){

                            case (MotionEvent.ACTION_DOWN):
                                Log.d(LOG_TAG, "onTouch: DOWN");
                                isRecording = true;

                                if (mRecorder == null){ mRecorder = new MediaRecorder(); }
                                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                                mRecorder.setOutputFile(mFile);
                                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

                                try {
                                    mRecorder.prepare();
                                    isRecording = true;
                                    Log.d(LOG_TAG, "prepare() success");
                                } catch (IOException e) {
                                    Log.e(LOG_TAG, "prepare() failed");
                                }

                                mRecorder.start();
                                view.setBackgroundColor(0xFFFF0000);
                                recView.setText("Recording Message");

                                return true;
                            case (MotionEvent.ACTION_UP):
                            case (MotionEvent.ACTION_OUTSIDE):
                            case (MotionEvent.ACTION_CANCEL):
                                Log.d(LOG_TAG, "onTouch: UP");
                                mRecorder.stop();
                                mRecorder.release();
                                mRecorder = null;
                                isRecording = false;
                                view.setBackgroundColor(0xFF00FF00);
                                recView.setText("Message Recorded.\nPress and hold to record a new message.");
                                Toast.makeText(getApplicationContext(), "File saved to :" + mFile, Toast.LENGTH_LONG).show();
                                return true;
                            default:
                                return false;

                        }
                    }
                }
        );

        FloatingActionButton playFab = (FloatingActionButton) findViewById(R.id.playFab);
        assert playFab != null;
        playFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playback();
            }
        });
    }


    MediaRecorder getmRecorder() {
        return mRecorder;
    }
    @Override
    public void onResume(){
        super.onResume();
        mRecorder = new MediaRecorder();
    }


    @Override
    public void onPause(){
        super.onPause();

        mRecorder = null;
    }

    private void playback(){
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(mFile);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mp.start();
    }
}
