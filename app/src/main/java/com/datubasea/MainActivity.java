package com.datubasea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<String> animess= new ArrayList();
        animess.add(0,"Noragami,9");
        animess.add(1,"Anoer,6");
        animess.add(2,"Evalion,7");
        animess.add(3,"Dragon Ball Z,4");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("insertaranime")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for(int i=0;i<animess.size();i++){
                                        boolean encontrado=false;
                                        Object animesNombre = animess.get(i).split(",")[0];
                                        Object animesValoracion = animess.get(i).split(",")[1];
                                        Map<String, Object> anime = new HashMap<>();
                                        anime.put("nombre",animesNombre);
                                        anime.put("valoracion",animesValoracion);

                                  for (QueryDocumentSnapshot document : task.getResult()) {
                                          String docNombre=document.get("nombre").toString().toLowerCase();
                                            String arrayNombre=animesNombre.toString().toLowerCase();
                                            Log.d("miFiltro", "nombre doc: "+docNombre+"arraynombre: "+arrayNombre);
                                            if(docNombre.contains(arrayNombre)){
                                                Toast.makeText(MainActivity.this, "ESTA YA EN LA BBDD", Toast.LENGTH_SHORT).show();
                                                encontrado=true;

                                            }
                                        }
                                        if(!encontrado){

                                            db.collection("insertaranime").document().set(anime);

                                        }
                                    }
                                } else {
                                    Log.w("miFiltro", "Error getting documents.", task.getException());
                                }

                            }
                        });
            }
        });
    }
}