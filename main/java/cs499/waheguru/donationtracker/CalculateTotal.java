package cs499.waheguru.donationtracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class CalculateTotal extends AppCompatActivity {

    private static DonationsDBHelper donationsHelper = null;
    private static SQLiteDatabase donationsDB = null;
    Float total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        donationsHelper = new DonationsDBHelper(this);
        donationsDB = donationsHelper.getWritableDatabase();
        Cursor cursor = donationsDB.rawQuery("SELECT SUM(entryNUM) FROM donations", null);
        if(cursor.moveToFirst()){
            total = cursor.getFloat(0);
        }
        Toast.makeText(this, "Total Donations Amount is $ " + total, Toast.LENGTH_SHORT).show();
    }

}