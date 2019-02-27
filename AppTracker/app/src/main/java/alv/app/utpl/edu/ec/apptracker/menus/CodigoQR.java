package alv.app.utpl.edu.ec.apptracker.menus;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;
import java.util.HashMap;
import java.util.Map;

import alv.app.utpl.edu.ec.apptracker.MainActivity;
import alv.app.utpl.edu.ec.apptracker.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CodigoQR extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScanner;
    private DatabaseReference mDatabase;
    TextView txt_qr;
    Button btn_qr;

    public CodigoQR() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_qr);

        Context context =this;
        SharedPreferences sharedPreferences = getSharedPreferences("ArchivoQR", context.MODE_PRIVATE);

        txt_qr=findViewById(R.id.txt_qr);
        btn_qr=findViewById(R.id.btn_qr);
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
        SharedPreferences sharpref = getPreferences(MODE_PRIVATE);
        String valor= sharpref.getString("DatosBus","Aun no se registra ningun bus");
        txt_qr.setText(valor);

    }
    //escanear el codigo
    public void btnEscanear(View v){
        mScanner = new ZXingScannerView(getApplicationContext());
        setContentView(mScanner);
        mScanner.setResultHandler(this);
        mScanner.startCamera();
    }
    //logica para escanear
    @Override
    public void handleResult(Result result) {
        String datos="";
        datos = result.getText();
        //ordenar los datos capturados por el codigo QR
        String[] parts = datos.split("-");
        //Crear archivo para guardar los datos
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        //eliminar***editor.edit().remove("DatosBus").commit();

        //restriccion para guardar los datos
        if (parts[0].equals("apptrackerUTPL")){
            //guardar datos en el archivo
            editor.putString("DatosBus","Servicio de transporte: "+parts[1]+"\n\nNúmero de vehículo: "+parts[2]+"\n\nPlacas del vehículo: "+parts[3]+"\n\nCódigo del chofer: "+parts[4]+"\n\nTeléfono de compañía: "+parts[5]);
            editor.commit();
            //Guardar los datos en la bdd
            String myIMEI = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            guardarDatos(myIMEI,parts[1],parts[2],parts[3],parts[4],parts[5]);
            Toast.makeText(this, "Servicio de transporte: "+parts[1]+"\n\nNúmero de vehículo: "+parts[2]+"\n\nPlacas del vehículo: "+parts[3]+"\n\nCódigo del chofer: "+parts[4]+"\n\nTeléfono de compañía: "+parts[5],Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"Error!! en código QR",Toast.LENGTH_SHORT).show();
        }

        //mScanner.resumeCameraPreview(this);
        mScanner.removeAllViews();
        mScanner.stopCamera();
        Intent intent=new Intent(this, MainActivity.class);startActivity(intent);
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
