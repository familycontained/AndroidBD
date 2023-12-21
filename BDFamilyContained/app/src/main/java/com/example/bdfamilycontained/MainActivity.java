package com.example.bdfamilycontained;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.example.bdfamilycontained.Usuarios; // Asegúrate de importar la clase Usuarios con el paquete correcto

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Usuarios usdbh = new Usuarios(this, "DBFamilyContained", null, 1); // Ajusta el nombre de la base de datos
        SQLiteDatabase db = usdbh.getWritableDatabase();

        // Si hemos abierto correctamente la base de datos
        if (db != null) {
            // Insertamos 5 usuarios de ejemplo
            for (int i = 1; i <= 5; i++) {

                // Generamos los datos
                String nombre = "Usuario" + i;
                String correo = "correo" + i + "@example.com";
                int edad = 25 + i;

                // Utilizamos ContentValues para insertar los datos de manera segura
                ContentValues values = new ContentValues();
                values.put("nombre", nombre);
                values.put("correo_electronico", correo);
                values.put("edad", edad);

                // Insertamos los datos en la tabla Usuarios
                db.insert("Usuarios", null, values);
            }

            // Cerramos la base de datos
            db.close();
        }
    }
}
