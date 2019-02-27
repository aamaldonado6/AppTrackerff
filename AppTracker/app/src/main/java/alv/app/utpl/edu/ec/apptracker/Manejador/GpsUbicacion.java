package alv.app.utpl.edu.ec.apptracker.Manejador;

import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import alv.app.utpl.edu.ec.apptracker.MainActivity;

public class GpsUbicacion implements LocationListener {
    MainActivity mainActivity;
    private DatabaseReference mDatabase;
    Calendar calendario = new GregorianCalendar();
    int hora,minuto,segundo,mes,dia,anio;

    public MainActivity getMainActivity(){
        return mainActivity;
    }
    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onLocationChanged(Location location) {
        //parametros para la hora
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        minuto = calendario.get(Calendar.MINUTE);
        segundo = calendario.get(Calendar.SECOND);
        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        mes = mes+1;
        anio = calendario.get(Calendar.YEAR);
        String horaAc=hora+":"+minuto+":"+segundo;
        String fechaAc=dia+"-"+mes+"-"+anio;
        //
        //AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        //builder.setTitle("guardando");
        //builder.setMessage("datos");
        //AlertDialog alertDialog = builder.create();
        //alertDialog.show();

        //obtener id del dispositivo
        String myIMEI = Settings.Secure.getString(mainActivity.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //obtener la velocidad en km/h ,altitud y longitud
        double speed=((location.getSpeed()*3600)/1000);
        String sp = String.format("%.2f",speed);
        double la = location.getLatitude();
        double lo = location.getLongitude();
        mainActivity.txtLati.setText(String.valueOf(la));
        mainActivity.txtlongi.setText(String.valueOf(lo));
        if (speed < 6.5){
            mainActivity.txtVelocidad.setText("0.00");
        }else {
            mainActivity.txtVelocidad.setText(sp);
        }
        guardarDatos(myIMEI,sp,la,lo,horaAc,fechaAc);
        if (speed >= 100){
            guardarDatosExceso(myIMEI,sp,la,lo,horaAc,fechaAc);
        }
        //this.mainActivity.gpsLoc(location);


    }
    private void guardarDatosExceso(String myIMEI,String sp,double la,double lo,String hora,String fecha) {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Map<String,Object> datos = new HashMap<>();
        datos.put("latitud",la);
        datos.put("longitud",lo);
        datos.put("velocidad",sp);
        datos.put("hora",hora);
        datos.put("fecha",fecha);
        datos.put("idDisposi",myIMEI);
        datos.put("reportado",0);
        mDatabase.child(myIMEI).child("excesoVelocidad").push().setValue(datos);


    }
    private void guardarDatos(String myIMEI,String sp,double la,double lo,String hora,String fecha) {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Map<String,Object> datos = new HashMap<>();
        datos.put("latitud",la);
        datos.put("longitud",lo);
        datos.put("velocidad",sp);
        datos.put("hora",hora);
        datos.put("fecha",fecha);
        datos.put("idDisposi",myIMEI);
        mDatabase.child(myIMEI).child("DatosGPS").push().setValue(datos);


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
