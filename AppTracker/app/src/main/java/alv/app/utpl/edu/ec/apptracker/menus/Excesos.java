package alv.app.utpl.edu.ec.apptracker.menus;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import alv.app.utpl.edu.ec.apptracker.R;

public class Excesos extends AppCompatActivity {
    DatabaseReference ref;
    LinearLayout lContenedor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excesos);
        ref = FirebaseDatabase.getInstance().getReference();
        lContenedor = findViewById(R.id.lcontenedor);
        listaFirebase(lContenedor);
        //inicializar Firebase
        try {
            FirebaseApp.initializeApp(this);
        }
        catch (Exception e) {
        }

    }
    public void listaFirebase(final LinearLayout contenedor) {
        
        String myIMEI = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        ref.child(myIMEI).child("excesoVelocidad").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int contador =0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    try {
                        contador = contador+1;
                        Switch switchb = new Switch(getApplicationContext());
                        //Personalizando botones
                        switchb.setText("Exceso de velocidad Nro: "+contador);
                        switchb.setTextColor(Color.rgb(255, 255, 255));
                        switchb.setBackgroundColor(Color.rgb(64,89,120));

                        //Enviar un margin a los botones
                        LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(
                                /*width*/ ViewGroup.LayoutParams.MATCH_PARENT,
                                /*height*/ ViewGroup.LayoutParams.WRAP_CONTENT

                        );
                        parametros.setMargins(5, 15, 5, 5);
                        //boton.setLayoutParams(parametros);
                        switchb.setLayoutParams(parametros);
                        switchb.setGravity(1);
                        //implementar el evento
                        switchb.setOnClickListener(misEventosButton);

                        contenedor.addView(switchb);

                    }catch(DatabaseException e){
                        Log.e("error!!",""+dataSnapshot.getKey());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private View.OnClickListener misEventosButton = new View.OnClickListener() {
        public void onClick(View v) {
            //aca castemos la variable v (View) para que este se convierta en un boton
            Button objBoton = (Button) v;
            Toast.makeText(getApplicationContext(),"Se reportó el exceso de velocidad Nro "+objBoton.getText(),Toast.LENGTH_SHORT).show();
        }
    };


    public static class Reporte {

        public double reportar;

        public Reporte() {
            // ...
        }
        public Reporte(double reportar) {
            // ...
        }

        public double getReportar() {
            return reportar;
        }
    }


    /*private void lista() {
        String myIMEI = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        ref.child(myIMEI).child("excesoVelocidad").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    try {

                        MapsLatLang user = snapshot.getValue(MapsLatLang.class);
                        double latitudll =user.getLatitud();
                        double longitudll = user.getLongitud();

                    }catch(DatabaseException e){
                        Log.e("error!!",""+dataSnapshot.getKey());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static class Post {

        public String author;
        public String title;

        public Post(String author, String title) {
            // ...
        }

    }
*/
}
