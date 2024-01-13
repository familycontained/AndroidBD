package com.example.bdfamilycontained;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FamilyContained";
    private static final int DATABASE_VERSION = 5;

    // SQL para crear las tablas
    private static final String TABLE_USUARIOS_CREATE =
            "CREATE TABLE IF NOT EXISTS Usuarios (" +
                    "idUsuarios INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT, " +
                    "correo_electronico TEXT, " +
                    "edad NUMERIC);";

    private static final String TABLE_TAREAS_CREATE =
            "CREATE TABLE IF NOT EXISTS Tareas (" +
                    "idTarea INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Descripcion TEXT, " +
                    "FechaCreacion DATE, " +
                    "FechaRealizacion DATE, " +
                    "Estado TEXT);";

    private static final String TABLE_USUARIO_TAREA_CREATE =
            "CREATE TABLE IF NOT EXISTS Usuario_Tarea (" +
                    "IDRelacion INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "IDUsuario INTEGER, " +
                    "IDTarea INTEGER);";

    private static final String TABLE_GRUPOS_CREATE =
            "CREATE TABLE IF NOT EXISTS Grupos (" +
                    "IDGrupo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NombreGrupo TEXT, " +
                    "DescripcionGrupo TEXT, " +
                    "IDCreador INTEGER, " +
                    "IDTarea INTEGER);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_USUARIOS_CREATE);
        db.execSQL(TABLE_TAREAS_CREATE);
        db.execSQL(TABLE_USUARIO_TAREA_CREATE);
        db.execSQL(TABLE_GRUPOS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) { // Si es una actualización desde una versión anterior a la 4
            db.execSQL(TABLE_USUARIO_TAREA_CREATE); // Crea la nueva tabla
        }
        // Agrega aquí más actualizaciones para versiones futuras
    }

}
