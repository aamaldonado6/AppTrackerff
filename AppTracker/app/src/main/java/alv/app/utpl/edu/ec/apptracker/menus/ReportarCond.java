package alv.app.utpl.edu.ec.apptracker.menus;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import alv.app.utpl.edu.ec.apptracker.MainActivity;
import alv.app.utpl.edu.ec.apptracker.MapsLatLang;
import alv.app.utpl.edu.ec.apptracker.R;

public class ReportarCond extends AppCompatActivity {
    DatabaseReference ref;
    MainActivity mainActivity;
    public RadioButton radio11,radio12,radio13,radio14;
    public Button button;
    public EditText textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar_cond);
        ref = FirebaseDatabase.getInstance().getReference();
        radio11 = (RadioButton) findViewById(R.id.radio1);
        radio12 = (RadioButton) findViewById(R.id.radio2);
        radio13 = (RadioButton) findViewById(R.id.radio3);
        radio14 = (RadioButton) findViewById(R.id.radio4);
        textView = (EditText) findViewById(R.id.txt_tv);



    }
    public void guardarD(View v){
        String myIMEI = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Map<String,Object> datos = new HashMap<>();
        String coment = String.valueOf(textView.getText());
        String r1 = String.valueOf(radio11.isChecked());
        String r2 = String.valueOf(radio12.isChecked());
        String r3 = String.valueOf(radio13.isChecked());
        String r4 = String.valueOf(radio14.isChecked());
        datos.put("rebasar",r1);
        datos.put("acogePasajeros",r2);
        datos.put("estacionado",r3);
        datos.put("combustible",r4);
        datos.put("comentario",coment);
        datos.put("idDisposi",myIMEI);
        ref.child(myIMEI).child("reporteCond").push().setValue(datos);
        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        "REPORTE GUARDADO", Toast.LENGTH_SHORT);

        toast1.show();
        Intent a = new Intent(this,MainActivity.class);startActivity(a);
    }



}
