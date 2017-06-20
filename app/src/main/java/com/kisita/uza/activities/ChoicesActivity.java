package com.kisita.uza.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import com.kisita.uza.R;
import com.kisita.uza.ui.ChoicesActivityFragment;

public class ChoicesActivity extends Activity implements ChoicesActivityFragment.OnFoodSelectedListener{
    private final int RESULT_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choices);
    }

    @Override
    public void onFoodSelectedListener(String name) {
        Intent intent=new Intent();
        intent.putExtra(getString(R.string.food_type),name);
        setResult(RESULT_CODE,intent);
        finish();
    }
}
