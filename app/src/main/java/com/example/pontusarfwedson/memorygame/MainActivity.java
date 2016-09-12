package com.example.pontusarfwedson.memorygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     *
     * @param v
     */
    public void startRules(View v)
    {
        Intent intent = new Intent(this, RulesActivity.class);
        startActivity(intent);


    }

    /**
     *
     * @param v
     */
    public void startPlay(View v)
    {
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }
}
