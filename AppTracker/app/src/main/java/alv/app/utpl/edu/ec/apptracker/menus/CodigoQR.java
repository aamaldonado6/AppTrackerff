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

import java.util.HashMap;
import java.util.Map;

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
        //ordenar los datos capturados por el codigo QR
        String datos = result.getText();
        String[] parts = datos.split("-");
        //cuadro de dialogo para mostrar los resultados del codigo QR
        Log.v("hadleResult",result.getText());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Usted se encuentra en:");
        if (parts[0].equals("apptrackerUTPL")){
            builder.setMessage( "Servicio de transporte: "+parts[1]+"\nNúmero de vehículo: "+parts[2]+"\nPlacas del vehículo: "+parts[3]+"\nCódigo del chofer: "+parts[4]+"\nTeléfono de compañía: "+parts[5]);
        }else {
            builder.setMessage("Error!! en código QR");
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //Guardar los datos registrados por el codigo QR
        String myIMEI = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        guardarDatos(myIMEI,parts[1],parts[2],parts[3],parts[4],parts[5]);
        //mScanner.resumeCameraPreview(this);


    }
    private void guardarDatos(String imei,String st,String nv,String pl,String cod,String te){


        Map<String,Object> datos = new HashMap<>();
        datos.put("servicio_transporte",st);
        datos.put("numero_vehiculi",nv);
        datos.put("placa_vehiculo",pl);
        datos.put("codigo_chofer",cod);
        datos.put("telefono_compania",te);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(imei).child("DatosBus").setValue(datos);
        //mDatabase.child(a).child("ssws").push().setValue("wwas12123");

    }
}
