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
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Grupos extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_grupos);

        Button botonRegistroGrupos = findViewById(R.id.aniadir_grupo);
        botonRegistroGrupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearGrupo();
            }
        });

        Button botonModificarGrupos = findViewById(R.id.modificar_grupo);
        botonModificarGrupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarGrupos();
            }
        });

        Button botonEliminarTarea = findViewById(R.id.eliminar_grupo);
        botonEliminarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarGrupo();
            }
        });

        Button botonListarGrupos = findViewById(R.id.listar_grupos);
        botonListarGrupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listarGrupos();
            }
        });
    }

    private void crearGrupo() {
        // Obtener las referencias a los elementos de la vista
        EditText etNombreGrupo = findViewById(R.id.nombreGrupo);
        EditText etDescripcionGrupo = findViewById(R.id.descripcionGrupo);
        Spinner spinnerCreadorGrupo = findViewById(R.id.spinnerCreadorGrupo);
        Spinner spinnerTareaGrupo = findViewById(R.id.spinnerTareaGrupo);



        // Crear y llenar las listas con los IDs correspondientes
        ArrayList<Integer> listaIdsCreador = obtenerIdsCreador(); // Reemplaza obtenerIdsCreador() con tu lógica para obtener los IDs
        ArrayList<Integer> listaIdsTarea = obtenerIdsTarea(); // Reemplaza obtenerIdsTarea() con tu lógica para obtener los IDs

        // Obtener los valores seleccionados en los Spinners
        int idCreador = obtenerIdDeSpinner(spinnerCreadorGrupo, listaIdsCreador);
        int idTarea = obtenerIdDeSpinner(spinnerTareaGrupo, listaIdsTarea);


        // Obtener los valores de los EditText
        String nombreGrupo = etNombreGrupo.getText().toString();
        String descripcionGrupo = etDescripcionGrupo.getText().toString();

        // Verificar que los valores no estén vacíos y que los ID de los Spinners sean válidos
        if (!nombreGrupo.isEmpty() && !descripcionGrupo.isEmpty() && idCreador != -1 && idTarea != -1) {
            // Realizar la inserción en la base de datos
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("NombreGrupo", nombreGrupo);
            values.put("DescripcionGrupo", descripcionGrupo);
            values.put("IDCreador", idCreador);
            values.put("IDTarea", idTarea);

            long resultado = db.insert("Grupos", null, values);
            db.close();

            if (resultado != -1) {
                Toast.makeText(this, "Grupo creado con éxito", Toast.LENGTH_SHORT).show();
                // Puedes realizar otras acciones aquí, como limpiar los campos o regresar a la actividad anterior.
            } else {
                Toast.makeText(this, "Error al crear el grupo", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Por favor, complete todos los campos y seleccione un creador y una tarea.", Toast.LENGTH_SHORT).show();
        }
    }

    private int obtenerIdDeSpinner(Spinner spinner, ArrayList<Integer> ids) {
        int position = spinner.getSelectedItemPosition();
        if (position < 0 || position >= ids.size()) {
            return -1; // o algún valor que indique que no es una selección válida
        }
        return ids.get(position);
    }
    private void llenarSpinnerConIds(Spinner spinner, ArrayList<Integer> ids) {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ids);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // Implementa tus funciones para obtener los IDs de los creadores y tareas aquí
    private ArrayList<Integer> obtenerIdsCreador() {
        ArrayList<Integer> ids = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT idUsuarios FROM Usuarios", null);
        int idColumnIndex = cursor.getColumnIndex("idUsuarios");

        if (idColumnIndex != -1) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(idColumnIndex);
                ids.add(id);
            }
        } else {
            // Manejar el caso de que la columna no exista
        }

        cursor.close();
        db.close();

        return ids;
    }

    private ArrayList<Integer> obtenerIdsTarea() {
        ArrayList<Integer> ids = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT idTarea FROM Tareas", null);
        int idColumnIndex = cursor.getColumnIndex("idTarea");

        if (idColumnIndex != -1) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(idColumnIndex);
                ids.add(id);
            }
        } else {
            // Manejar el caso de que la columna no exista
        }

        cursor.close();
        db.close();

        return ids;
    }



    private void modificarGrupos() {

    }

    private void eliminarGrupo() {

    }

    private void listarGrupos() {

    }


}
