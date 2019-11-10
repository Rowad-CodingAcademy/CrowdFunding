package com.abood.crowdfunding;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

abstract public class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        fragmentManager.beginTransaction().replace(R.id.fragment_container,createFragment()).commit();

    }

}
