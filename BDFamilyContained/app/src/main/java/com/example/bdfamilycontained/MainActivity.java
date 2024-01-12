package com.example.bdfamilycontained;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece la vista usando el layout XML que has creado
        setContentView(R.layout.menu_principal);

        //---------------------------------------------------------------
        Button botonUsuarios = findViewById(R.id.usuarios);
        botonUsuarios.setText(R.string.usuarios);
        botonUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Usuarios.class);
                startActivity(intent);
            }
        });
        //---------------------------------------------------------------
        Button botonTareas = findViewById(R.id.tareas);
        botonTareas.setText(R.string.tareas);
        botonTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Tareas.class);
                startActivity(intent);
            }
        });
        //---------------------------------------------------------------
        Button botonUsuarioTareas = findViewById(R.id.usuarioTareas);
        botonUsuarioTareas.setText(R.string.usuarioTareas);
        botonUsuarioTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Usuario_Tareas.class);
                startActivity(intent);
            }
        });
        //---------------------------------------------------------------
        Button botonGrupos = findViewById(R.id.grupos);
        botonGrupos.setText(R.string.grupos);
        botonGrupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Grupos.class);
                startActivity(intent);
            }
        });




    }
}
