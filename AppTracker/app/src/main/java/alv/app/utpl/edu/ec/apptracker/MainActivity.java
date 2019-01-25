package alv.app.utpl.edu.ec.apptracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import alv.app.utpl.edu.ec.apptracker.menus.CodigoQR;
import alv.app.utpl.edu.ec.apptracker.menus.Excesos;
import alv.app.utpl.edu.ec.apptracker.menus.MapsActivity01;
import alv.app.utpl.edu.ec.apptracker.menus.ReportarCond;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView txtVelocidad;
    public CardView reportar, mapa, codigoQr, listaV;
    LottieAnimationView animationView;
    boolean estaPresionado = false;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //animationView = (LottieAnimationView) findViewById(R.id.animation_view);

        //pedirPermisos
        permisosUbicacion();
        //definicion de los cardview
        reportar = (CardView) findViewById(R.id.cardReportar);
        mapa = (CardView) findViewById(R.id.cardMapa);
        codigoQr = (CardView) findViewById(R.id.codigoQr);
        listaV = (CardView) findViewById(R.id.cardLista);
        //añadir cardview a addClick Listener
        reportar.setOnClickListener(this);
        mapa.setOnClickListener(this);
        codigoQr.setOnClickListener(this);
        listaV.setOnClickListener(this);
        //inicializar Firebase
        try {
            FirebaseApp.initializeApp(this);
            mDatabase = FirebaseDatabase.getInstance().getReference();

        }
        catch (Exception e) {
        }

        // id del dispositivo
        String myIMEI = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        //definir la variable para mostrar la velocidad
        txtVelocidad = (TextView) findViewById(R.id.txtVelocidad);
        //gd.setIdUsuario(myIMEI);
        gpsUb();
        /*

        CodigoQR coQR = new CodigoQR();
        if (coQR.isQrOK() == true){
            Toast.makeText(this,"bien mrd",Toast.LENGTH_LONG).show();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("!!Alerta!!");
            builder.setMessage("Debes registrar el código QR");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }*/





    }
    public void gpsLoc(Location location) {
        if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0){
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                if (!list.isEmpty()){
                    Address address = list.get(0);
                    txtVelocidad.setText(address.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void gpsUb() {

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        GpsUbicacion  gpsu = new GpsUbicacion();
        gpsu.setMainActivity(this);
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 3,(GpsUbicacion) gpsu);

    }

    public void permisosUbicacion() {

        //solicitar el permiso
        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_DENIED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
    public void panico(View v){

            
    }

    @Override
    public void onClick(View v) {

        Intent i;
        switch (v.getId()){
            case R.id.codigoQr: i = new Intent(this,CodigoQR.class);startActivity(i); break;
            case R.id.cardLista : i = new Intent(this,Excesos.class);startActivity(i); break;
            case R.id.cardReportar : i = new Intent(this,ReportarCond.class);startActivity(i); break;
            case R.id.cardMapa : i = new Intent(this,MapsActivity01.class);startActivity(i); break;
        }
    }
}