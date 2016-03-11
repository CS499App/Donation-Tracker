package cs499.waheguru.donationtracker;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.Button;

public class NewEntryActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE= 1;
    ImageView imageView;
    Bundle bundle;
    Bundle extras;
    double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        Button picButton = (Button) findViewById(R.id.photoButton);
        imageView = (ImageView) findViewById(R.id.imageView);

        if(!hasCamera())
            picButton.setEnabled(false);
    }
    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void launchCamera(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //take a picture and pass results to onActivityResult
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
    }

    public void newEntryAdded(View view){
        EditText etDesignation = (EditText) findViewById(R.id.newDesignation);
        EditText etItemOrAmount = (EditText) findViewById(R.id.newItem_Amount);
        EditText etDate = (EditText) findViewById(R.id.newDate);
        Log.i("rcc", "in newEntryAdded");
        String designation = etDesignation.getText().toString();
        String amount = etItemOrAmount.getText().toString();
        String date = etDate.getText().toString();

        if(!designation.isEmpty() && !amount.isEmpty() && !date.isEmpty()){
            Intent data = getIntent();
            bundle = new Bundle();
            bundle.putString("designation", designation);
            bundle.putString("amount",amount);
            bundle.putString("date",date);
            Log.i("rcc", String.format("%s,%s,%s", bundle.getString("designation"), bundle.getString("amount"), bundle.getString("date")));
            data.putExtra("entryData", bundle);
            Log.i("rcc", "setting bundle result and finishing");
            this.setResult(Activity.RESULT_OK, data);

            finish();
        }
        else{
            Toast.makeText(this, "Designation, Amount, and Date are required", Toast.LENGTH_SHORT).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            imageView.setImageBitmap(photo);
        }
    }


}


