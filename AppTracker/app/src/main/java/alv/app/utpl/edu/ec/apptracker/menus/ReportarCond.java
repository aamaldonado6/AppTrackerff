package alv.app.utpl.edu.ec.apptracker.menus;

import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import alv.app.utpl.edu.ec.apptracker.MapsLatLang;
import alv.app.utpl.edu.ec.apptracker.R;

public class ReportarCond extends AppCompatActivity {
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar_cond);
        ref = FirebaseDatabase.getInstance().getReference();
        lista();
    }

    private void lista() {
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


}
