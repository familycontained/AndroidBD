package com.example.bdfamilycontained;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Usuarios extends SQLiteOpenHelper {

    String sqlCreateUsuarios = "CREATE TABLE IF NOT EXISTS Usuarios (idUsuarios INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, correo_electronico TEXT, edad NUMERIC)";
    String sqlCreateTareas = "CREATE TABLE IF NOT EXISTS Tareas ( IDTarea INTEGER PRIMARY KEY AUTOINCREMENT, Descripcion TEXT,FechaCreacion DATE, FechaRealizacion DATE,Estado TEXT,IDUsuario INTEGER)";
    String sqlCreateUsTar = "CREATE TABLE IF NOT EXISTS Usuario_Tarea (  IDRelacion INTEGER PRIMARY KEY AUTOINCREMENT,IDUsuario INTEGER, IDTarea INTEGER)";
    String sqlCreateGrupos = "CREATE TABLE IF NOT EXISTS Grupos ( IDGrupo INTEGER PRIMARY KEY AUTOINCREMENT, NombreGrupo TEXT, DescripcionGrupo TEXT,IDCreador INTEGER )";

    public Usuarios(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateUsuarios);
        db.execSQL(sqlCreateTareas);
        db.execSQL(sqlCreateUsTar);
        db.execSQL(sqlCreateGrupos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}