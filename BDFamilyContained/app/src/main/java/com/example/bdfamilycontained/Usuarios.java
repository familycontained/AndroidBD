package com.example.bdfamilycontained;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Usuarios extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE IF NOT EXISTS Usuarios (idUsuarios INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, correo_electronico TEXT, edad NUMERIC)";

    public Usuarios(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}