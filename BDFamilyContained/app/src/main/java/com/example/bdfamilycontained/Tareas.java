package com.example.bdfamilycontained;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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

        Spinner spinnerTareas = findViewById(R.id.spinnerTarea);
        EditText etDescripcion = findViewById(R.id.descripcionTarea);
        Spinner estadoSpinner = findViewById(R.id.estado_tarea_opciones);
        Button btnModificar = findViewById(R.id.btnModificar);
        Button btnVolverAtras = findViewById(R.id.volverAtras);

        ArrayList<String> infoTareas = obtenerInfoTareas();
        llenarSpinnerConDatos(spinnerTareas, infoTareas);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.estado_tarea_opciones, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estadoSpinner.setAdapter(adapter);

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idTarea = obtenerIdDeSpinner(spinnerTareas);
                String nuevaDescripcion = etDescripcion.getText().toString();
                String nuevoEstado = estadoSpinner.getSelectedItem().toString();

                if (idTarea == -1) {
                    Toast.makeText(Tareas.this, "Por favor, seleccione una tarea.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (nuevaDescripcion.isEmpty() && nuevoEstado.isEmpty()) {
                    Toast.makeText(Tareas.this, "Ingrese al menos una modificación (descripción o estado).", Toast.LENGTH_SHORT).show();
                    return;
                }

                actualizarTareaEnLaBaseDeDatos(String.valueOf(idTarea), nuevaDescripcion, nuevoEstado);
            }
        });

        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tareas.this, Tareas.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private ArrayList<String> obtenerInfoTareas() {
        ArrayList<String> info = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT idTarea, Descripcion FROM Tareas", null);
        int idTareaColumnIndex = cursor.getColumnIndex("idTarea");
        int descripcionColumnIndex = cursor.getColumnIndex("Descripcion");

        if (idTareaColumnIndex != -1 && descripcionColumnIndex != -1) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(idTareaColumnIndex);
                String descripcion = cursor.getString(descripcionColumnIndex);
                info.add(id + " - " + descripcion);
            }
        }
        cursor.close();
        db.close();

        return info;
    }

    private void llenarSpinnerConDatos(Spinner spinner, ArrayList<String> datos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, datos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private int obtenerIdDeSpinner(Spinner spinner) {
        String selectedItem = (String) spinner.getSelectedItem();
        if (selectedItem != null && !selectedItem.isEmpty()) {
            String[] parts = selectedItem.split(" - ");
            if (parts.length > 0) {
                try {
                    return Integer.parseInt(parts[0]);
                } catch (NumberFormatException e) {
                    String errorMessage = e.getMessage();
                    assert errorMessage != null;
                    Log.e("NumberFormatException", errorMessage);
                }
            }
        }
        return -1;
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

        Spinner spinnerTareas = findViewById(R.id.spinnerEliminarTarea);
        Button btnEliminar = findViewById(R.id.btnEliminar);

        ArrayList<String> infoTareas = obtenerInfoTareas();
        llenarSpinnerConDatos(spinnerTareas, infoTareas);

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idTarea = obtenerIdDeSpinner(spinnerTareas);
                if (idTarea != -1) {
                    eliminarTareaDeLaBaseDeDatos(String.valueOf(idTarea));
                } else {
                    Toast.makeText(Tareas.this, "Por favor, seleccione una tarea.", Toast.LENGTH_SHORT).show();
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

        Cursor cursor = db.rawQuery("SELECT * FROM Tareas", null);

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

            tareas.add("ID: " + idTarea + ", Descripción: " + descripcion + ", Fecha Creación: " + fechaCreacion + ", Fecha Realización: " + fechaRealizacion + ", Estado: " + estado);
        }
        cursor.close();

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
