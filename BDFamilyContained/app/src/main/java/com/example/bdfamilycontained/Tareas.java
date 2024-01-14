package com.example.bdfamilycontained;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
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

        Button btnVolverAtras = findViewById(R.id.volverAtras);
        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tareas.this, MainActivity.class);
                startActivity(intent);
                finish();
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
                values.put("Estado", estado);
                values.put("FechaCreacion", fechaCreacion);
                values.put("FechaRealizacion", fechaRealizacion);


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

        Button btnVolverAtras = findViewById(R.id.volverAtras);
        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tareas.this, Tareas.class);
                startActivity(intent);
                finish();
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
        Button btnVolverAtras = findViewById(R.id.volverAtras);
        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tareas.this, Tareas.class);
                startActivity(intent);
                finish();
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
        Button btnVolverAtras = findViewById(R.id.volverAtras);
        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tareas.this, Tareas.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void eliminarTareaDeLaBaseDeDatos(String idTarea) {
        DatabaseHelper dbHelper = new DatabaseHelper(Tareas.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Asegúrate de que el nombre de la columna aquí coincide con el nombre en tu base de datos
        int deletedRows = db.delete("Tareas", "IDTarea = ?", new String[]{idTarea});
        db.close();

        if (deletedRows > 0) {
            Toast.makeText(Tareas.this, "Tarea eliminada.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Tareas.this, "No se pudo eliminar la tarea.", Toast.LENGTH_SHORT).show();
        }
    }





    private void listarTareas() {
        setContentView(R.layout.listar_tarea);

        ListView listView = findViewById(R.id.listar_tareas);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consulta para obtener los datos de las tareas
        Cursor cursor = db.rawQuery("SELECT * FROM Tareas", null);

        // Obtiene los índices de las columnas
        int idTareaIndex = cursor.getColumnIndex("idTarea");
        int descripcionIndex = cursor.getColumnIndex("Descripcion");
        int fechaCreacionIndex = cursor.getColumnIndex("FechaCreacion");
        int fechaRealizacionIndex = cursor.getColumnIndex("FechaRealizacion");
        int estadoIndex = cursor.getColumnIndex("Estado");

        List<String> tareas = new ArrayList<>();
        while(cursor.moveToNext()) {
            String idTarea = idTareaIndex != -1 ? cursor.getString(idTareaIndex) : "ID no disponible";
            String descripcion = descripcionIndex != -1 ? cursor.getString(descripcionIndex) : "Descripción no disponible";
            String fechaCreacion = fechaCreacionIndex != -1 ? cursor.getString(fechaCreacionIndex) : "Fecha Creación no disponible";
            String fechaRealizacion = fechaRealizacionIndex != -1 ? cursor.getString(fechaRealizacionIndex) : "Fecha Realización no disponible";
            String estado = estadoIndex != -1 ? cursor.getString(estadoIndex) : "Estado no disponible";

            // Formatear la información de la tarea
            tareas.add("ID: " + idTarea + ", Descripción: " + descripcion + ", Fecha Creación: " + fechaCreacion + ", Fecha Realización: " + fechaRealizacion + ", Estado: " + estado);
        }
        cursor.close();

        // Usar un ArrayAdapter para mostrar la lista de tareas
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tareas);
        listView.setAdapter(adapter);

        Button btnVolverAtras = findViewById(R.id.volverAtras);
        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tareas.this, Tareas.class);
                startActivity(intent);
                finish();
            }
        });
    }



}
