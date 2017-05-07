package com.kisita.uza;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.model.Data;
import com.kisita.uza.ui.DetailFragment;

public class DetailsActivity extends CustomActivity {
    /** The toolbar. */
    public Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setTitle(getIntent().getStringArrayExtra("Details")[Data.UzaData.NAME.ordinal()]);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        DetailFragment f = DetailFragment.newInstance(getIntent().getStringArrayExtra("Details"),"");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, f)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
