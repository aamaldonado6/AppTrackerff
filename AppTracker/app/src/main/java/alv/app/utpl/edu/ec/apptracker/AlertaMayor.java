package alv.app.utpl.edu.ec.apptracker;

import android.provider.Settings;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import alv.app.utpl.edu.ec.apptracker.MainActivity;

public class AlertaMayor {
    MainActivity mainActivity;
    private DatabaseReference mDatabase;
    String myIMEI = Settings.Secure.getString(mainActivity.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    Calendar calendario = new GregorianCalendar();
    int hora,minuto,segundo,mes,dia,anio;

    public void guardarDatosPanic(double la, double lo) {
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
        datos.put("idDisposi",this.myIMEI);
        mDatabase.child(this.myIMEI).child("botonPanico").push().setValue(datos);


    }
}
