package com.gosecruri.mspr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.microblink.entities.recognizers.blinkid.mrtd.MrtdRecognizer;

import org.w3c.dom.Text;

public class Card_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        Intent i = getIntent();
        Bundle result = i.getBundleExtra("cardInfo");

        TextView name = (TextView)findViewById(R.id.Name);
        TextView prenom = (TextView)findViewById(R.id.Prenom);
        TextView gender = (TextView)findViewById(R.id.Gender);
        TextView nationality = (TextView)findViewById(R.id.Nationality);
        TextView dateBirth = (TextView)findViewById(R.id.DateBirth);
        TextView documentNumber = (TextView)findViewById(R.id.DocumentNumber);

        name.setText(result.getString("Name"));
        prenom.setText(result.getString("Prenom"));
        gender.setText(result.getString("Gender"));
        nationality.setText(result.getString("Nationality"));
        dateBirth.setText(result.getString("DateBirth"));
        documentNumber.setText(result.getString("DocumentNumber"));

        Log.i("Nationality", result.getString("Nationality"));
    }
}
