package com.bobmchenry.ad340.week3assignment;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    boolean isRecording;
    MediaRecorder mRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String mFile =
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MUSIC) + "/test.3gp";
        isRecording = false;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecorder = new MediaRecorder();

        FloatingActionButton playFab = (FloatingActionButton) findViewById(R.id.playFab);
        assert playFab != null;
        playFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mp = new MediaPlayer();
                try {
                    mp.setDataSource(mFile);
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mp.start();
            }
        });

        FloatingActionButton recFab = (FloatingActionButton) findViewById(R.id.recFab);
        assert recFab != null;
        recFab.setOnClickListener(new View.OnClickListener() {
            public static final String LOG_TAG = "#######################";

            @Override
            public void onClick(View view) {
                mRecorder = getmRecorder();

                Log.d(LOG_TAG, mFile);
                if (!isRecording) {
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
                } else {
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                    isRecording = false;
                    Toast.makeText(getApplicationContext(), "File saved to :" + mFile, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    MediaRecorder getmRecorder() {
        return mRecorder;
    }
    @Override
    public void onResume(){
        super.onResume();
        mRecorder = new MediaRecorder();
    }

}
