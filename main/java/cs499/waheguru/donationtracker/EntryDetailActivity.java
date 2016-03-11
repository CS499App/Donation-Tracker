package cs499.waheguru.donationtracker;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class EntryDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);
    }

    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("entryData");

        TextView tvDesignation = (TextView) findViewById(R.id.entryDetailDesignation);
        TextView tvAmount = (TextView) findViewById(R.id.entryDetailAmount);
        TextView tvDate = (TextView) findViewById(R.id.entryDetailDate);

        tvDesignation.setText(bundle.getString("designation"));
        tvAmount.setText(bundle.getString("amount"));
        tvDate.setText(bundle.getString("date"));
    }

    public void deleteEntry(View view){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("entryData");
        int rowID = bundle.getInt("rowID");
        intent.putExtra("ID", rowID);
        setResult(RESULT_OK, intent);
        finish();
    }
}
