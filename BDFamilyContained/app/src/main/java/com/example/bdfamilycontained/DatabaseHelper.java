package com.example.bdfamilycontained;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FamilyContainedDB";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_USUARIOS_CREATE =
            "CREATE TABLE Usuarios (" +
                    "idUsuarios INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT, " +
                    "correo_electronico TEXT, " +
                    "edad NUMERIC);";

    private static final String TABLE_TAREAS_CREATE =
            "CREATE TABLE Tareas (" +
                    "idTarea INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Descripcion TEXT, " +
                    "Estado TEXT, " +
                    "FechaCreacion TEXT, " +
                    "FechaRealizacion TEXT);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_USUARIOS_CREATE);
        db.execSQL(TABLE_TAREAS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            // Elimina la tabla existente si existe
            db.execSQL("DROP TABLE IF EXISTS Tareas");

            // Crea la nueva tabla "Tareas"
            db.execSQL(TABLE_TAREAS_CREATE);
        }
    }
}

