package com.example.bdfamilycontained;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText etNombre, etCorreo, etEdad;
    private Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Usuarios usdbh = new Usuarios(this, "DBFamilyContained", null, 1);
        final SQLiteDatabase db = usdbh.getWritableDatabase();

        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etEdad = findViewById(R.id.etEdad);
        btnAgregar = findViewById(R.id.btnAgregar);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener los valores ingresados por el usuario
                String nombre = etNombre.getText().toString();
                String correo = etCorreo.getText().toString();
                int edad = Integer.parseInt(etEdad.getText().toString());

                // Utilizar ContentValues para insertar los datos en la tabla Usuarios
                ContentValues values = new ContentValues();
                values.put("nombre", nombre);
                values.put("correo_electronico", correo);
                values.put("edad", edad);

                // Insertar los datos en la tabla Usuarios
                db.insert("Usuarios", null, values);

                // Limpiar los campos después de la inserción
                etNombre.setText("");
                etCorreo.setText("");
                etEdad.setText("");
            }
        });
    }
}
