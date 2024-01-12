package com.example.bdfamilycontained;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Usuarios extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_usuarios); // Usa el layout correspondiente
    }

    //---------------------------------------------------------------
    Button botonRegistroUsuarios = findViewById(R.id.usuarios);
        botonRegistroUsuarios.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           // ejecutar el metodo crearUsuario
        }
    });
    //---------------------------------------------------------------
    Button botonModificarUsuarios = findViewById(R.id.tareas);
        botonModificarUsuarios.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // ejecutar el metodo modificarUsuario
        }
    });
    //---------------------------------------------------------------
    Button botonEliminarUsuario = findViewById(R.id.usuarioTareas);
        botonEliminarUsuario.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // ejecutar el metodo eliminarUsuario
        }
    });
    //---------------------------------------------------------------
    Button botonListarUsuarios = findViewById(R.id.grupos);
        botonGrupos.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // ejecutar el metodo listarUsuarios
        }
    });

    public crearUsuario() {
        String sqlCreateUsuarios = "CREATE TABLE IF NOT EXISTS Usuarios (idUsuarios INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, correo_electronico TEXT, edad NUMERIC)";
        Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
            super(contexto, nombre, factory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(sqlCreateUsuarios);}


    }
    public modificarUsuarios() {

    }
    public eliminarUsuario() {

    }
    public listarUsuarios() {

    }


}