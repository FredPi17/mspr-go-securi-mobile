package com.gosecruri.mspr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.microblink.MicroblinkSDK;
import com.microblink.activity.DocumentScanActivity;
import com.microblink.entities.recognizers.Recognizer;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkid.mrtd.MrtdRecognizer;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.DocumentUISettings;

public class MainActivity extends AppCompatActivity {

    private MrtdRecognizer mRecognizer;
    private RecognizerBundle mRecognizerBundle;
    public static final int MY_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MicroblinkSDK.setLicenseKey("sRwAAAASY29tLmdvc2VjcnVyaS5tc3BywAbzZf2VuPW9X/hwubjUDEMrTrjHT+fJTQ2ena5qffL0dLTmDwju949vfgu0H1MibvfEnr/POftDwxadru2is+LjUlm58Qy+R4tYrdhXPGFEQvssbKjQBQK4F+pqKGYkorYaeqzbWvdLmmggFvaGPJr4u8xQFtx9SsuHyGdpBt4Y+EVwh5SUNiEQ6KyGgiWzoMUD5USLXZxlnY1Nqn5DHH2hpdg46ZWYEuusDv0hi2qPhVhDOZ7w/LTW6PuC", this);

        // create MrtdRecognizer
        mRecognizer = new MrtdRecognizer();

        // bundle recognizers into RecognizerBundle
        mRecognizerBundle = new RecognizerBundle(mRecognizer);
    }

    public void onScanButtonClick(View view) {
        this.startScanning();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void startScanning() {
        // Settings for DocumentScanActivity Activity
        DocumentUISettings settings = new DocumentUISettings(mRecognizerBundle);

        // tweak settings as you wish

        // Start activity
        ActivityRunner.startActivityForResult(this, MY_REQUEST_CODE, settings);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == DocumentScanActivity.RESULT_OK && data != null) {
                // load the data into all recognizers bundled within your RecognizerBundle
                mRecognizerBundle.loadFromIntent(data);

                // now every recognizer object that was bundled within RecognizerBundle
                // has been updated with results obtained during scanning session

                // you can get the result by invoking getResult on recognizer
                MrtdRecognizer.Result result = mRecognizer.getResult();
                if (result.getResultState() == Recognizer.Result.State.Valid) {
                    // result is valid, you can use it however you wish
                }
            }
        }
    }
}
