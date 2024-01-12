package com.example.bdfamilycontained;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Tareas extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_tareas);

        Button botonRegistroTareas = findViewById(R.id.aniadir_tarea);
        botonRegistroTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearTarea();
            }
        });

        Button botonModificarTareas = findViewById(R.id.modificar_tarea);
        botonModificarTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarTareas();
            }
        });

        Button botonEliminarTarea = findViewById(R.id.eliminar_tarea);
        botonEliminarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarTarea();
            }
        });

        Button botonListarTareas = findViewById(R.id.listar_tareas);
        botonListarTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listarTareas();
            }
        });
    }

    private void crearTarea() {
        setContentView(R.layout.crear_tarea);

        EditText nombreEditText = findViewById(R.id.nombre);
        EditText correoEditText = findViewById(R.id.correo);
        EditText edadEditText = findViewById(R.id.edad);
        Button submitButton = findViewById(R.id.Agregar);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = nombreEditText.getText().toString();
                String correo = correoEditText.getText().toString();
                String edadStr = edadEditText.getText().toString();

                if (nombre.isEmpty() || correo.isEmpty() || edadStr.isEmpty()) {
                    Toast.makeText(Tareas.this, "Falta completar campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                int edad = Integer.parseInt(edadStr);
                DatabaseHelper dbHelper = new DatabaseHelper(Tareas.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Verificar si el Tarea ya existe (opcional, depende de tu lógica de negocio)
                // ...

                ContentValues values = new ContentValues();
                values.put("nombre", nombre);
                values.put("correo_electronico", correo);
                values.put("edad", edad);

                long result = db.insert("Tareas", null, values);
                db.close();

                if (result == -1) {
                    Toast.makeText(Tareas.this, "Tarea ya existe", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Tareas.this, "Tarea creado", Toast.LENGTH_SHORT).show();
                    nombreEditText.setText("");
                    correoEditText.setText("");
                    edadEditText.setText("");
                }
            }
        });
    }

    private void modificarTareas() {
        setContentView(R.layout.modificar_Tarea);

        EditText etIdTarea = findViewById(R.id.idTarea);
        EditText etNombre = findViewById(R.id.etNombre);
        EditText etCorreo = findViewById(R.id.etCorreo);
        Button btnModificar = findViewById(R.id.btnModificar);

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idTarea = etIdTarea.getText().toString();
                String nuevoNombre = etNombre.getText().toString();
                String nuevoCorreo = etCorreo.getText().toString();

                // Verificar solo el ID del Tarea
                if (idTarea.isEmpty()) {
                    Toast.makeText(Tareas.this, "Por favor, ingrese el ID del Tarea.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar si al menos uno de los campos está lleno
                if (nuevoNombre.isEmpty() && nuevoCorreo.isEmpty()) {
                    Toast.makeText(Tareas.this, "Ingrese al menos un campo para actualizar.", Toast.LENGTH_SHORT).show();
                    return;
                }

                actualizarTareaEnLaBaseDeDatos(idTarea, nuevoNombre, nuevoCorreo);
            }
        });
    }

    private void actualizarTareaEnLaBaseDeDatos(String idTarea, String nombre, String correo) {
        DatabaseHelper dbHelper = new DatabaseHelper(Tareas.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (!nombre.isEmpty()) {
            values.put("nombre", nombre);
        }
        if (!correo.isEmpty()) {
            values.put("correo_electronico", correo);
        }

        int affectedRows = db.update("Tareas", values, "idTareas = ?", new String[]{idTarea});
        db.close();

        if (affectedRows > 0) {
            Toast.makeText(Tareas.this, "Tarea actualizado.", Toast.LENGTH_SHORT).show();
            EditText idTareaEditText = findViewById(R.id.idTarea);
            EditText nombreEditText = findViewById(R.id.etNombre);
            EditText correoEditText = findViewById(R.id.etCorreo);
            idTareaEditText.setText("");
            nombreEditText.setText("");
            correoEditText.setText("");

        } else {
            Toast.makeText(Tareas.this, "Error al actualizar el Tarea o el Tarea no existe.", Toast.LENGTH_SHORT).show();
        }

    }


    private void eliminarTarea() {
        setContentView(R.layout.eliminar_Tarea);

        final EditText etIdTarea = findViewById(R.id.idTarea);
        Button btnEliminar =findViewById(R.id.btnEliminar);

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idTarea = etIdTarea.getText().toString();
                if (!idTarea.isEmpty()) {
                    eliminarTareaDeLaBaseDeDatos(idTarea);
                } else {
                    Toast.makeText(Tareas.this, "Por favor, ingrese un ID de Tarea.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void eliminarTareaDeLaBaseDeDatos(String idTarea) {
        DatabaseHelper dbHelper = new DatabaseHelper(Tareas.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int deletedRows = db.delete("Tareas", "idTareas = ?", new String[]{idTarea});
        db.close();

        if (deletedRows > 0) {
            Toast.makeText(Tareas.this, "Tarea eliminado.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Tareas.this, "No se pudo eliminar el Tarea.", Toast.LENGTH_SHORT).show();
        }
    }





    private void listarTareas() {
        setContentView(R.layout.listar_Tarea);

        ListView listView = findViewById(R.id.listaTareas);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consulta para obtener los datos de los Tareas
        Cursor cursor = db.rawQuery("SELECT * FROM Tareas", null);

        // Obtiene los índices de las columnas
        int idTareasIndex = cursor.getColumnIndex("idTareas");
        int nombreIndex = cursor.getColumnIndex("nombre");
        int correoIndex = cursor.getColumnIndex("correo_electronico");
        int edadIndex = cursor.getColumnIndex("edad");

        List<String> Tareas = new ArrayList<>();
        while(cursor.moveToNext()) {
            // Verifica que los índices de las columnas sean válidos
            String idTarea = nombreIndex != -1 ? cursor.getString(idTareasIndex) : "ID no disponible";
            String nombre = nombreIndex != -1 ? cursor.getString(nombreIndex) : "Nombre no disponible";
            String correo = correoIndex != -1 ? cursor.getString(correoIndex) : "Correo no disponible";
            String edad = edadIndex != -1 ? Integer.toString(cursor.getInt(edadIndex)) : "Edad no disponible";

            // Formatear la información del Tarea
            Tareas.add(idTarea + " - " + nombre + " - " + correo + " - " + edad);
        }
        cursor.close();


        // Usar un ArrayAdapter para mostrar la lista de Tareas
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Tareas);
        listView.setAdapter(adapter);
    }


}
