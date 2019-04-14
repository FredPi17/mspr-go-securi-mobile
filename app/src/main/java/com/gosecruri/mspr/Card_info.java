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

        name.setText(result.getString("Name"));
        prenom.setText(result.getString("Prenom"));
    }
}
