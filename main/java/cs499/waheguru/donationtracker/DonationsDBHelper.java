package cs499.waheguru.donationtracker;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Waheguru on 2/29/16.
 */
public class DonationsDBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 2;
    private static final String DB_NAME = "DonationsDB";
    private static final String TABLE_NAME = "donations";

    private static final String CREATE_DONATIONS_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "_id INTEGER NOT NULL PRIMARY KEY, " +
                    "entryDesignation TEXT NOT NULL, " +
                    "entryAmount TEXT NOT NULL, " +
                    "entryDate TEXT NOT NULL, " +
                    "entryNum FLOAT NOT NULL)";


    DonationsDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DONATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }
}


