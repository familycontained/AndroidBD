package com.example.bdfamilycontained;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UsuariosDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CREATE =
            "CREATE TABLE Usuarios (" +
                    "idUsuarios INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT, " +
                    "correo_electronico TEXT, " +
                    "edad NUMERIC);";

    private static final String DATABASE_NAME2 = "TareasDB";
    private static final String TABLE_CREATE2 =
            "CREATE TABLE Tareas (" +
                    "idTarea INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Descripcion TEXT, " +
                    "Estado TEXT, " +
                    "FechaCrecion TEXT, " +
                    "FechaRealizacion TEXT);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_CREATE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aquí puedes manejar las actualizaciones de la base de datos
    }
}

