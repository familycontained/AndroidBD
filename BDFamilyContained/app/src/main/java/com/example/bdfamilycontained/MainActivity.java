package com.example.bdfamilycontained;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etNombre, etCorreo, etEdad, etDescripcion, etFechaCreacion, etFechaRealizacion;
    private Spinner spinnerEstado;
    private Button btnAgregar, btnAgregarTarea;

    private String[] estados = {"Azul (Nueva Tarea)", "Naranja (En Proceso)", "Verde (Hecho)", "Rojo (Pendiente)"};
    private String[] colores = {"Azul", "Naranja", "Verde", "Rojo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreacionBD usdbh = new CreacionBD(this, "DBFamilyContained", null, 1);
        final SQLiteDatabase db = usdbh.getWritableDatabase();

        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etEdad = findViewById(R.id.etEdad);
        etDescripcion = findViewById(R.id.etDescripcion);
        etFechaCreacion = findViewById(R.id.etFechaCreacion);
        etFechaRealizacion = findViewById(R.id.etFechaRealizacion);
        spinnerEstado = findViewById(R.id.spinnerEstado);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnAgregarTarea = findViewById(R.id.btnAgregarTarea);

        // Configurar el adaptador para el Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);

        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // El usuario seleccionó un estado, puedes obtener el color correspondiente
                String colorSeleccionado = colores[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // No se seleccionó nada
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener los valores ingresados por el usuario para el registro de usuarios
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
                showToast("Usuario agregado correctamente");
            }
        });

        btnAgregarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener los valores ingresados por el usuario para la tarea
                String descripcion = etDescripcion.getText().toString();
                String fechaCreacion = etFechaCreacion.getText().toString();
                String fechaRealizacion = etFechaRealizacion.getText().toString();
                String estado = colores[spinnerEstado.getSelectedItemPosition()]; // Obtener el color seleccionado

                // Utilizar ContentValues para insertar los datos en la tabla Tareas
                ContentValues values = new ContentValues();
                values.put("Descripcion", descripcion);
                values.put("FechaCreacion", fechaCreacion);
                values.put("FechaRealizacion", fechaRealizacion);
                values.put("Estado", estado);

                // Insertar los datos en la tabla Tareas
                db.insert("Tareas", null, values);

                // Limpiar los campos después de la inserción
                etDescripcion.setText("");
                etFechaCreacion.setText("");
                etFechaRealizacion.setText("");
                spinnerEstado.setSelection(0); // Establecer la selección predeterminada
                showToast("Tarea agregada correctamente");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
