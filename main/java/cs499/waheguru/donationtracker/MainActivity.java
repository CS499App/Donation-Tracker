package cs499.waheguru.donationtracker;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static DonationsDBHelper donationsHelper = null;
    private static SQLiteDatabase donationsDB = null;
    private static SimpleCursorAdapter cursorAdapter = null;
    private static ListView listView = null;
    Float total;

    AdapterView.OnItemClickListener mOnItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, EntryDetailActivity.class);
            TextView tv = (TextView) view;
            String titleOfEntry = tv.getText().toString();
            String queryString =
                    String.format("SELECT * FROM donations WHERE entryDesignation = '%s'", titleOfEntry);
            Cursor cursor = donationsDB.rawQuery(queryString, null);
            if (cursor.moveToFirst()) {
                int entryRowID = cursor.getInt(0);
                String entryDesignation = cursor.getString(1);
                String entryAmount = cursor.getString(2);
                String entryDate = cursor.getString(3);

                //make a bundle to hold the intent extras:
                Bundle bundle = new Bundle();
                bundle.putInt("rowID", entryRowID);
                bundle.putString("designation", entryDesignation);
                bundle.putString("amount", entryAmount);
                bundle.putString("date", entryDate);
                //add the bundle to the intent:
                intent.putExtra("entryData", bundle);
            }

            //start the detail activity:
            startActivityForResult(intent, 2);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        donationsHelper = new DonationsDBHelper(this);
        donationsDB = donationsHelper.getWritableDatabase();
        listView = (ListView) findViewById(R.id.list);

        Cursor cursor = donationsDB.rawQuery("SELECT _id, entryDesignation FROM donations", null);
        String[] columns = {cursor.getColumnName(1)};
        int[] displayViews = {R.id.listText};
        cursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item,
                cursor,
                columns,
                displayViews,
                0
        );

        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(mOnItemClick);
    }


    public void addNewEntry(View view){
        Intent intent = new Intent(this, NewEntryActivity.class);
        startActivityForResult(intent, 1);
    }

    public void calculateTotal(View view){
        donationsHelper = new DonationsDBHelper(this);
        donationsDB = donationsHelper.getWritableDatabase();
        Cursor cursor = donationsDB.rawQuery("SELECT SUM(entryNUM) FROM donations", null);
        if(cursor.moveToFirst()){
            total = cursor.getFloat(0);
        }
        Toast.makeText(this, "Total Donations Amount is " + total, Toast.LENGTH_SHORT).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("rcc", "onActivityResult called");
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1: //display the data in the list view:
                    //get the new note out of the returning data intent:
                    Bundle dataBundle = data.getBundleExtra("entryData");
                    String entryDesignation = dataBundle.getString("designation");
                    String entryAmount = dataBundle.getString("amount");
                    String entryDate = dataBundle.getString("date");
                    Double entryNum = 0.0;
                    if(isInteger(entryAmount)){
                        entryNum = Double.parseDouble(entryAmount);
                    }

                    //log out the result bundle content:
                    Log.i("rcc", String.format("%s: %s: %s", entryDesignation, entryAmount, entryDate));

                    //post to the database:
                    String insert_query =
                            "INSERT INTO donations (entryDesignation, entryAmount, entryDate, entryNum) " +
                                    "VALUES ('" + entryDesignation + "', '" + entryAmount + "', '" + entryDate + "', '" + entryNum + "')";
                    donationsDB.execSQL(insert_query);

                    updateCursorAdapter();
                    break;
                case 2: //delete the record
                    //getIntExtra requires a default value, pass 0
                    int row_id = data.getIntExtra("ID", 0);
                    String delete_query =
                            String.format("DELETE FROM donations WHERE _id = %d", row_id);
                    donationsDB.execSQL(delete_query);

                    updateCursorAdapter();
                    break;
                default:

            }
        }

    }

    private void updateCursorAdapter() {
        Cursor cursor = donationsDB.rawQuery("SELECT _id, entryDesignation FROM donations", null);
        String[] columns = {cursor.getColumnName(1)};
        int[] displayViews = {R.id.listText};

        cursorAdapter.changeCursor(cursor);
    }

    public boolean isInteger(String input){
        try{
            Integer.parseInt(input);
            return true;
        }
        catch( NumberFormatException e) {
            return false;
        }
    }

}
