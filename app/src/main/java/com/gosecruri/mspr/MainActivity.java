package com.gosecruri.mspr;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.microblink.MicroblinkSDK;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkid.mrtd.MrtdRecognizer;
import com.microblink.entities.recognizers.blinkid.mrtd.MrzResult;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.DocumentUISettings;

import java.util.Map;

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

        isOnDb(mrtdResult);

    }
    private void onScanCanceled() {
        Toast.makeText(this, "Scan cancelled!", Toast.LENGTH_SHORT).show();
    }

    private void isOnDb(final MrtdRecognizer.Result mrtdResult)
    {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final String name = mrtdResult.getMrzResult().getPrimaryId().toLowerCase();
        final String firstname = mrtdResult.getMrzResult().getSecondaryId().toLowerCase();
        final String dateOfBirth = mrtdResult.getMrzResult().getDateOfBirth().getOriginalDateString().toLowerCase();
        final boolean[] itsEnd = {true};

        db.collection("Agents")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String nameGetted = (String) document.getData().get("Nom");
                                String firstNameGetted = (String) document.getData().get("Prenom");
                                String DDNGetted = (String) document.getData().get("DDN");

                                nameGetted = nameGetted.toLowerCase();
                                firstNameGetted = firstNameGetted.toLowerCase();
                                DDNGetted = DDNGetted.toLowerCase();

                                    if (nameGetted.equals(name))
                                    {
                                        if (firstNameGetted.equals(firstname))
                                        {
                                            if (DDNGetted.equals(dateOfBirth))
                                            {
                                                itsEnd[0] = false;
                                                Intent i=new Intent(MainActivity.this, Card_info.class);

                                                Bundle b = new Bundle();
                                                b.putString("AlienNumber", mrtdResult.getMrzResult().getAlienNumber());
                                                b.putString("DocumentCode", mrtdResult.getMrzResult().getDocumentCode());
                                                b.putString("Name", mrtdResult.getMrzResult().getPrimaryId());
                                                b.putString("Prenom", mrtdResult.getMrzResult().getSecondaryId());
                                                b.putString("Gender", mrtdResult.getMrzResult().getGender());
                                                b.putString("Nationality", mrtdResult.getMrzResult().getNationality());
                                                b.putString("DocumentNumber", mrtdResult.getMrzResult().getDocumentNumber());
                                                b.putString("DateBirth", mrtdResult.getMrzResult().getDateOfBirth().getOriginalDateString());
                                                b.putString("DocumentType", mrtdResult.getMrzResult().getDocumentType().toString());
                                                b.putString("DateOfExpiry", mrtdResult.getMrzResult().getDateOfExpiry().toString());

                                                startActivity(i);
                                                break;
                                            }
                                        }
                                    }
                            }
                        }
                    }
                });
//        if (itsEnd[0]) {
//            Intent i2 = new Intent(MainActivity.this, ErrorActivity.class);
//            startActivity(i2);
//        }
    }
}