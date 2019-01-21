package alv.app.utpl.edu.ec.apptracker.menus;

import android.Manifest;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;

import alv.app.utpl.edu.ec.apptracker.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CodigoQR extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScanner;
    private DatabaseReference mDatabase;
    public boolean qrOK=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_qr);

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    1);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }

        mScanner = new ZXingScannerView(this);
        setContentView(mScanner);
        mScanner.setResultHandler(this);
        mScanner.startCamera();

    }

    @Override
    public void handleResult(Result result) {
        this.qrOK = true;
        Log.v("hadleResult",result.getText());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Resultado del scan");
        builder.setMessage(result.getText());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        String myIMEI = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        guardarDatos(myIMEI);
        //mScanner.resumeCameraPreview(this);


    }
    private void guardarDatos(String a){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(a).child("sss").setValue("233442125121321");
        mDatabase.child(a).child("sss").push().setValue("as12123");

    }
}
