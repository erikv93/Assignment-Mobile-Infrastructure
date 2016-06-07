package erik.caa;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    Sensor accelerometer;
    Sensor light;
    SensorManager sm;
    TextView dataTextView;
    EditText nameEditText;
    CountDownTimer shakeCountdownTimer;
    List<Float> shakeData = new ArrayList<>();
    boolean recordData = false;
    Button startTimerButton;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;

    public static final int SHAKE_DURATION = 5000;
    public static final int LIGHT_TRESHOLD = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setMax(SHAKE_DURATION);

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        startTimerButton = (Button)findViewById(R.id.startTimerButton);

        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        light = sm.getDefaultSensor(Sensor.TYPE_LIGHT);

        dataTextView = (TextView)findViewById(R.id.sensorDataTV);
        nameEditText = (EditText)findViewById(R.id.nameEditText);

        shakeCountdownTimer = new CountDownTimer(SHAKE_DURATION, 100) {

            @Override
            public void onTick(long millisUntilFinished) {
                int test = SHAKE_DURATION - (int)millisUntilFinished;
                        progressBar.setProgress(test);
            }

            @Override
            public void onFinish() {
                recordData = false;
                startTimerButton.setEnabled(true);
                printLargest();
                progressBar.setProgress(0);
                shakeData.clear();
            }
        };

        setNameEditText(getName());
    }

    public void setNameEditText(String name) {
        nameEditText.setText(name);
    };

    public String getName(){
        String name = sharedPreferences.getString("SCHUDMEESTER_NAME", "Player");
        return name;
    };

    public void printLargest() {
        Log.d(this.getLocalClassName(),getScoreResponse(getLargest(shakeData)));
        Toast.makeText(MainActivity.this, getScoreResponse(getLargest(shakeData)), Toast.LENGTH_SHORT).show();
    }

    public String getScoreResponse(float score) {
        if (score < 35) {
            return String.format("Je score was: %.1f, dat is niet zo goed, maar geef de hoop niet op!", score);
        } else if (score >= 35 && score < 40) {
            return String.format("Je score was %.1f, een redelijk score, maar dat kan beter!", score);
        } else if (score >= 40 && score < 47.5) {
            return String.format("Wauw, je score was %.1f, ga zo door!", score);
        } else {
            return String.format("Serieus, een score van %.1f? Volgens deze app had je beter Tinder kunnen installeren", score);
        }
    };

    public Float getLargest(List<Float> list) {
        return Collections.max(list);
    }

    public void startShakeCountdownButtonPressed(View view) {
        if (!recordData) {
            recordData = true;
            shakeCountdownTimer.start();
            startTimerButton.setEnabled(false);
        }
    }

    public void changeNameButtonPressed(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("SCHUDMEESTER_NAME", nameEditText.getText().toString());
        editor.commit();
        Toast.makeText(MainActivity.this, "Naam verandert", Toast.LENGTH_SHORT).show();
    }

    protected void onResume(){
        super.onResume();
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        sm.registerListener(this, light, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];
            String dataString = String.format("X: %f, Y: %f, Z: %f", x, y, z);
            dataTextView.setText(dataString);

            if (recordData) {
                shakeData.add((y+x+z)-9.3f);
            }
        }
        if (sensor.getType() == Sensor.TYPE_LIGHT) {
            Log.d(this.getLocalClassName(),"Light: " + event.values[0] );
            if (event.values[0] >= LIGHT_TRESHOLD) {
                getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                dataTextView.setTextColor(Color.BLACK);
                progressBar.setBackgroundColor(Color.WHITE);
            } else if (event.values[0] < LIGHT_TRESHOLD) {
                getWindow().getDecorView().setBackgroundColor(Color.BLACK);
                dataTextView.setTextColor(Color.WHITE);
                progressBar.setBackgroundColor(Color.GRAY);
            }
        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
