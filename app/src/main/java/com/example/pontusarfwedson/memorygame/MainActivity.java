package com.example.pontusarfwedson.memorygame;


import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, RulesFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainFragment fragment = new MainFragment();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.commit();

    }


    /**
     *
     * @param v
     */
    public void startRules(View v)
    {
       // Intent intent = new Intent(this, RulesActivity.class);
       // startActivity(intent);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RulesFragment fragment = new RulesFragment();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
