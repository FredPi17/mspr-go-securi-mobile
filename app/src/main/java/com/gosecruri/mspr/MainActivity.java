package com.gosecruri.mspr;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.microblink.MicroblinkSDK;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkid.mrtd.MrtdRecognizer;
import com.microblink.entities.recognizers.blinkid.mrtd.MrzResult;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.DocumentUISettings;

public class MainActivity extends AppCompatActivity {
    private MrtdRecognizer mRecognizer;
    private RecognizerBundle mRecognizerBundle;
    public static final int MY_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MicroblinkSDK.setLicenseKey("sRwAAAASY29tLmdvc2VjcnVyaS5tc3BywAbzZf2VuPW9X/hwubjUDEMrTrjHT+fJTQ2ena5qffL0dLTmDwju949vfgu0H1MibvfEnr/POftDwxadru2is+LjUlm58Qy+R4tYrdhXPGFEQvssbKjQBQK4F+pqKGYkorYaeqzbWvdLmmggFvaGPJr4u8xQFtx9SsuHyGdpBt4Y+EVwh5SUNiEQ6KyGgiWzoMUD5USLXZxlnY1Nqn5DHH2hpdg46ZWYEuusDv0hi2qPhVhDOZ7w/LTW6PuC", this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup views, as you would normally do in onCreate callback

        // create MrtdRecognizer
        mRecognizer = new MrtdRecognizer();

        // bundle recognizers into RecognizerBundle
        mRecognizerBundle = new RecognizerBundle(mRecognizer);


        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startScanning();
            }
        });
    }

    // method within MyActivity from previous step
    public void startScanning() {
        // Settings for DocumentScanActivity Activity
        DocumentUISettings settings = new DocumentUISettings(mRecognizerBundle);

        // tweak settings as you wish

        // Start activity
        ActivityRunner.startActivityForResult(this, MY_REQUEST_CODE, settings);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // onActivityResult is called whenever we returned from activity started with startActivityForResult
        // We need to check request code to determine that we have really returned from BlinkID activity
        if (requestCode != MY_REQUEST_CODE) {
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
            // OK result code means scan was successful
            onScanSuccess(data);
        } else {
            // user probably pressed Back button and cancelled scanning
            onScanCanceled();
        }
    }

    private void onScanSuccess(Intent data) {
        // update recognizer results with scanned data
        mRecognizerBundle.loadFromIntent(data);

        // you can now extract any scanned data from result, we'll just get primary id
        MrtdRecognizer.Result mrtdResult = mRecognizer.getResult();
        Intent i=new Intent(MainActivity.this, Card_info.class);

        Bundle b = new Bundle();
        b.putString("AlienNumber", mrtdResult.getMrzResult().getAlienNumber());
        b.putString("DocumentCode", mrtdResult.getMrzResult().getDocumentCode());
        b.putString("Name", mrtdResult.getMrzResult().getPrimaryId());
        b.putString("Prenom", mrtdResult.getMrzResult().getSecondaryId());
        //b.putString("Gender", mrtdResult.getMrzResult().getGender());
        b.putString("Nationality", mrtdResult.getMrzResult().getNationality());
        b.putString("DocumentNumber", mrtdResult.getMrzResult().getDocumentNumber());
        b.putString("Gender", mrtdResult.getMrzResult().getMrzText());
        b.putString("DateBirth", mrtdResult.getMrzResult().getDateOfBirth().toString());

        i.putExtra("cardInfo",b);
        startActivity(i);

    }
    private void onScanCanceled() {
        Toast.makeText(this, "Scan cancelled!", Toast.LENGTH_SHORT).show();
    }
}
