package com.example.bdfamilycontained;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Usuario_Tareas extends Activity {

    private ArrayList<Integer> usuariosIds;
    private ArrayList<Integer> tareasIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_usuario_tarea);

        usuariosIds = new ArrayList<>();
        tareasIds = new ArrayList<>();

        Button botonRegistroUsuario_Tareas = findViewById(R.id.aniadir_usuario_tarea);
        botonRegistroUsuario_Tareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearUsuario_Tarea();
            }
        });

        Button botonModificarUsuario_Tareas = findViewById(R.id.modificar_usuario_tarea);
        botonModificarUsuario_Tareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarUsuario_Tareas();
            }
        });

        Button botonEliminarTarea = findViewById(R.id.eliminar_usuario_tarea);
        botonEliminarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarUsuario_Tarea();
            }
        });

        Button botonListarUsuario_Tareas = findViewById(R.id.listar_usuario_tarea);
        botonListarUsuario_Tareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listarUsuario_Tareas();
            }
        });
    }

    private void crearUsuario_Tarea() {
        setContentView(R.layout.crear_usuario_tarea); // Asegúrate de que este es el nombre correcto de tu layout

        Spinner spinnerUsuarios = findViewById(R.id.spinnerSeleccionarUsuario);
        Spinner spinnerTareas = findViewById(R.id.spinnerSeleccionarTarea);
        Button btnCrearRelacion = findViewById(R.id.btnCrearRelacion);

        cargarUsuariosEnSpinner(spinnerUsuarios);
        cargarTareasEnSpinner(spinnerTareas);

        btnCrearRelacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idUsuario = obtenerIdDeSpinner(spinnerUsuarios, usuariosIds);
                int idTarea = obtenerIdDeSpinner(spinnerTareas, tareasIds);
                if (idUsuario != -1 && idTarea != -1) {
                    guardarRelacionUsuarioTarea(idUsuario, idTarea);
                } else {
                    Toast.makeText(Usuario_Tareas.this, "Selección no válida", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void cargarUsuariosEnSpinner(Spinner spinner) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT idUsuarios, nombre FROM Usuarios", null);
        ArrayList<String> usuarios = new ArrayList<>();
        usuariosIds = new ArrayList<>(); // Asegúrate de que usuariosIds es un miembro de la clase

        int idColumnIndex = cursor.getColumnIndex("idUsuarios");
        int nameColumnIndex = cursor.getColumnIndex("nombre");

        if (idColumnIndex != -1 && nameColumnIndex != -1) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(idColumnIndex);
                String nombreUsuario = cursor.getString(nameColumnIndex);
                usuarios.add(nombreUsuario);
                usuariosIds.add(id);
            }
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, usuarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Guarda el ID del usuario seleccionado
                int selectedUserId = usuariosIds.get(position);
                // Haz algo con el ID seleccionado si es necesario
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void cargarTareasEnSpinner(Spinner spinner) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT idTarea, Descripcion FROM Tareas", null);
        ArrayList<String> tareas = new ArrayList<>();
        tareasIds = new ArrayList<>(); // Asegúrate de que tareasIds es un miembro de la clase

        int idColumnIndex = cursor.getColumnIndex("idTarea");
        int descripcionColumnIndex = cursor.getColumnIndex("Descripcion");

        if (idColumnIndex != -1 && descripcionColumnIndex != -1) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(idColumnIndex);
                String descripcion = cursor.getString(descripcionColumnIndex);
                tareas.add(descripcion);
                tareasIds.add(id);
            }
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tareas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Guarda el ID de la tarea seleccionada
                int selectedTaskId = tareasIds.get(position);
                // Haz algo con el ID seleccionado si es necesario
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private int obtenerIdDeSpinner(Spinner spinner, ArrayList<Integer> ids) {
        int position = spinner.getSelectedItemPosition();
        if (position < 0 || position >= ids.size()) {
            return -1; // o algún valor que indique que no es una selección válida
        }
        return ids.get(position);
    }


    private void guardarRelacionUsuarioTarea(int idUsuario, int idTarea) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idUsuarios", idUsuario);
        values.put("idTarea", idTarea);

        long result = db.insert("Usuario_Tarea", null, values);
        db.close();

        if (result != -1) {
            Toast.makeText(this, "Relación creada", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al crear la relación", Toast.LENGTH_SHORT).show();
        }
    }


    private void modificarUsuario_Tareas() {

    }

    private void eliminarUsuario_Tarea() {

    }

    private void listarUsuario_Tareas() {

    }


}
