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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        EditText descEditText = findViewById(R.id.descripcionTarea);
        EditText fechaRealizacionEditText = findViewById(R.id.fechaRealizacion);
        Spinner estadoSpinner = findViewById(R.id.estado_tarea_opciones);
        TextView tvFechaCreacion = findViewById(R.id.tvFechaCreacion);
        Button submitButton = findViewById(R.id.Agregar);

        // Establecer la fecha de creación
        String fechaCreacion = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        tvFechaCreacion.setText(fechaCreacion);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String descripcion = descEditText.getText().toString();
                String fechaRealizacion = fechaRealizacionEditText.getText().toString();
                String estado = estadoSpinner.getSelectedItem().toString();

                if (descripcion.isEmpty() || fechaRealizacion.isEmpty()) {
                    Toast.makeText(Tareas.this, "Falta completar campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseHelper dbHelper = new DatabaseHelper(Tareas.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("Descripcion", descripcion);
                values.put("FechaCreacion", fechaCreacion);
                values.put("FechaRealizacion", fechaRealizacion);
                values.put("Estado", estado);

                long result = db.insert("Tareas", null, values);
                db.close();

                if (result == -1) {
                    Toast.makeText(Tareas.this, "Error al crear la tarea", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Tareas.this, "Tarea creada", Toast.LENGTH_SHORT).show();
                    descEditText.setText("");
                    fechaRealizacionEditText.setText("");
                    // Restablecer el estado del Spinner si es necesario
                }
            }
        });
    }


    private void modificarTareas() {
        setContentView(R.layout.modificar_tarea);

        EditText etIdTarea = findViewById(R.id.idTarea);
        EditText etDescripcion = findViewById(R.id.descripcionTarea);
        Spinner estadoSpinner = findViewById(R.id.estado_tarea_opciones);
        Button btnModificar = findViewById(R.id.btnModificar);

        // Configurar el spinner de estado
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.estado_tarea_opciones, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estadoSpinner.setAdapter(adapter);

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idTarea = etIdTarea.getText().toString();
                String nuevaDescripcion = etDescripcion.getText().toString();
                String nuevoEstado = estadoSpinner.getSelectedItem().toString();

                if (idTarea.isEmpty()) {
                    Toast.makeText(Tareas.this, "Por favor, ingrese el ID de la tarea.", Toast.LENGTH_SHORT).show();
                    return;
                }

                actualizarTareaEnLaBaseDeDatos(idTarea, nuevaDescripcion, nuevoEstado);
            }
        });
    }

    private void actualizarTareaEnLaBaseDeDatos(String idTarea, String descripcion, String estado) {
        DatabaseHelper dbHelper = new DatabaseHelper(Tareas.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (!descripcion.isEmpty()) {
            values.put("Descripcion", descripcion);
        }
        values.put("Estado", estado);

        int affectedRows = db.update("Tareas", values, "IDTarea = ?", new String[]{idTarea});
        db.close();

        if (affectedRows > 0) {
            Toast.makeText(Tareas.this, "Tarea actualizada.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Tareas.this, "Error al actualizar la tarea.", Toast.LENGTH_SHORT).show();
        }
    }



    private void eliminarTarea() {
        setContentView(R.layout.eliminar_tarea);

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
        setContentView(R.layout.listar_tarea);

        ListView listView = findViewById(R.id.listar_tareas);
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
