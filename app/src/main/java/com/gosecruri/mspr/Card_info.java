package com.gosecruri.mspr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Card_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        Intent i = getIntent();
        Bundle result = i.getBundleExtra("cardInfo");

        TextView name = findViewById(R.id.Name);
        TextView prenom = findViewById(R.id.Prenom);
        TextView gender = findViewById(R.id.Gender);
        TextView nationality = findViewById(R.id.Nationality);
        TextView dateBirth = findViewById(R.id.DateBirth);
        TextView documentNumber = findViewById(R.id.DocumentType);
        TextView documentType = findViewById(R.id.DocumentType);

        name.setText(result.getString("Name"));
        prenom.setText(result.getString("Prenom"));
        gender.setText(result.getString("Gender"));
        nationality.setText(result.getString("Nationality"));
        dateBirth.setText(result.getString("DateBirth"));
        documentNumber.setText(result.getString("DocumentNumber"));
        documentType.setText(result.getString("DocumentType"));

        Log.i("Nationality", result.getString("Nationality"));
    }
}
