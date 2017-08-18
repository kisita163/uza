package com.kisita.uza.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.kisita.uza.R;
import com.kisita.uza.ui.ChoicesActivityFragment;

public class ChoicesActivity extends AppCompatActivity implements ChoicesActivityFragment.OnFoodSelectedListener{
    private final int RESULT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String currentFragment  = getIntent().getStringExtra(getString(R.string.choices));
        setContentView(R.layout.activity_choices);
        setFragment(currentFragment);
    }

    void setFragment(String fragment){
        ChoicesActivityFragment f = ChoicesActivityFragment.newInstance(fragment);
        getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, f)
                    .commit();
    }

    @Override
    public void onChoiceMadeListener(String name) {
        Intent intent=new Intent();
        intent.putExtra(getString(R.string.choice),name);
        setResult(RESULT_CODE,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // TODO return to the previous activity
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
