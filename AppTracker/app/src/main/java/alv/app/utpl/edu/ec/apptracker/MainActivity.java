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
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import alv.app.utpl.edu.ec.apptracker.Manejador.GpsUbicacion;
import alv.app.utpl.edu.ec.apptracker.menus.CodigoQR;
import alv.app.utpl.edu.ec.apptracker.menus.Excesos;
import alv.app.utpl.edu.ec.apptracker.menus.MapsActivity01;
import alv.app.utpl.edu.ec.apptracker.menus.ReportarCond;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView txtVelocidad;
    public TextView txtlongi;
    public TextView txtLati;
    private TextView txt_alert;
    private LinearLayout LiAnima;
    public CardView reportar, mapa, codigoQr, listaV;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LiAnima = (LinearLayout) findViewById(R.id.LiAnima);
        txtlongi=(TextView) findViewById(R.id.txtlongitud);
        txtLati=(TextView) findViewById(R.id.txtlatitud);;
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



        //definir la variable para mostrar la velocidad
        txtVelocidad = (TextView) findViewById(R.id.txtVelocidad);
        //contador de la alerta
        txt_alert= (TextView)findViewById(R.id.txt_alert);
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
        GpsUbicacion gpsu = new GpsUbicacion();
        gpsu.setMainActivity(this);
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,(GpsUbicacion) gpsu);

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
private String gg = "";
    public void panico(final View v){
        CountDownTimer countDownTimer = new CountDownTimer(6*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                    txt_alert.setText("Espera: "+millisUntilFinished/1000);
                    LiAnima.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFinish() {
                    txt_alert.setText("ALERTA");
                    LiAnima.setVisibility(View.VISIBLE);
            }
        }.start();
        guardarDatosPanic(Double.parseDouble((String) txtLati.getText()),Double.parseDouble((String) txtLati.getText()));

    }
    Calendar calendario = new GregorianCalendar();
    int hora,minuto,segundo,mes,dia,anio;
    public void guardarDatosPanic(double la, double lo) {
        System.out.println(la+"-----"+lo);
        // id del dispositivo
        String myIMEI="";
        try {

             myIMEI = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        }catch (Exception e){}
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        minuto = calendario.get(Calendar.MINUTE);
        segundo = calendario.get(Calendar.SECOND);
        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        mes = mes+1;
        anio = calendario.get(Calendar.YEAR);
        String horaAc=hora+":"+minuto+":"+segundo;
        String fechaAc=dia+"-"+mes+"-"+anio;


        mDatabase = FirebaseDatabase.getInstance().getReference();

        Map<String,Object> datos = new HashMap<>();
        datos.put("latitud",la);
        datos.put("longitud",lo);
        datos.put("hora",horaAc);
        datos.put("fecha",fechaAc);
        datos.put("idDisposi",myIMEI);
        mDatabase.child(myIMEI).child("botonPanico").push().setValue(datos);


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