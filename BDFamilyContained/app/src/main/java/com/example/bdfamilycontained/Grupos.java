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
        setContentView(R.layout.crear_grupo);

        EditText etNombreGrupo = findViewById(R.id.nombreGrupo);
        EditText etDescripcionGrupo = findViewById(R.id.descripcionGrupo);
        Spinner spinnerCreadorGrupo = findViewById(R.id.spinnerCreadorGrupo);
        Spinner spinnerTareaGrupo = findViewById(R.id.spinnerTareaGrupo);
        Button btnAgregarGrupo = findViewById(R.id.AgregarGrupo);

        // Llenar los Spinners con la información de usuarios y tareas
        llenarSpinnerConDatos(spinnerCreadorGrupo, obtenerInfoUsuarios());
        llenarSpinnerConDatos(spinnerTareaGrupo, obtenerInfoTareas());

        btnAgregarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idCreador = obtenerIdDeSpinner(spinnerCreadorGrupo);
                int idTarea = obtenerIdDeSpinner(spinnerTareaGrupo);

                String nombreGrupo = etNombreGrupo.getText().toString();
                String descripcionGrupo = etDescripcionGrupo.getText().toString();

                if (!nombreGrupo.isEmpty() && !descripcionGrupo.isEmpty() && idCreador != -1 && idTarea != -1) {
                    guardarGrupo(nombreGrupo, descripcionGrupo, idCreador, idTarea);
                } else {
                    Toast.makeText(Grupos.this, "Por favor, complete todos los campos y seleccione un creador y una tarea.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void guardarGrupo(String nombre, String descripcion, int idCreador, int idTarea) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NombreGrupo", nombre);
        values.put("DescripcionGrupo", descripcion);
        values.put("IDCreador", idCreador);
        values.put("IDTarea", idTarea);

        long resultado = db.insert("Grupos", null, values);
        db.close();

        if (resultado != -1) {
            Toast.makeText(this, "Grupo creado con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al crear el grupo", Toast.LENGTH_SHORT).show();
        }
    }


    private void llenarSpinnerConDatos(Spinner spinner, ArrayList<String> datos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, datos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private int obtenerIdDeSpinner(Spinner spinner) {
        String selectedItem = (String) spinner.getSelectedItem();
        if (selectedItem != null && !selectedItem.isEmpty()) {
            String[] parts = selectedItem.split(" - "); // Suponiendo que el formato es "ID - Nombre/Descripción"
            if (parts.length > 0) {
                try {
                    return Integer.parseInt(parts[0]); // Obtiene el ID
                } catch (NumberFormatException e) {
                    // Manejar excepción si el formato no es el esperado
                }
            }
        }
        return -1; // Retorna -1 si no hay selección válida
    }


    private ArrayList<String> obtenerInfoUsuarios() {
        ArrayList<String> info = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT idUsuarios, nombre FROM Usuarios", null);
        int idColumnIndex = cursor.getColumnIndex("idUsuarios");
        int nombreColumnIndex = cursor.getColumnIndex("nombre");

        if (idColumnIndex != -1 && nombreColumnIndex != -1) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(idColumnIndex);
                String nombre = cursor.getString(nombreColumnIndex);
                info.add(id + " - " + nombre);
            }
        }
        cursor.close();
        db.close();

        return info;
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


    private void modificarGrupos() {

                }
    private void eliminarGrupo() {
        setContentView(R.layout.eliminar_grupo);
        EditText etIdGrupo = findViewById(R.id.idTarea);
        Button btnEliminar = findViewById(R.id.btnEliminar);

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idGrupoStr = etIdGrupo.getText().toString();
                if (!idGrupoStr.isEmpty()) {
                    int idGrupo = Integer.parseInt(idGrupoStr);
                    eliminarGrupoDeBaseDeDatos(idGrupo);
                } else {
                    Toast.makeText(Grupos.this, "Por favor, ingrese un ID de grupo válido.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void eliminarGrupoDeBaseDeDatos(int idGrupo) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int affectedRows = db.delete("Grupos", "IDGrupo = ?", new String[]{String.valueOf(idGrupo)});
        db.close();

        if (affectedRows > 0) {
            Toast.makeText(this, "Grupo eliminado con éxito.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se encontró el grupo con el ID especificado.", Toast.LENGTH_SHORT).show();
        }
    }


    private void listarGrupos() {
        setContentView(R.layout.listar_grupos);

        ListView listViewGrupos = findViewById(R.id.listar_grupos);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Grupos", null);
        ArrayList<String> gruposList = new ArrayList<>();

        while (cursor.moveToNext()) {
            int idGrupoIndex = cursor.getColumnIndex("IDGrupo");
            int nombreGrupoIndex = cursor.getColumnIndex("NombreGrupo");
            int descripcionGrupoIndex = cursor.getColumnIndex("DescripcionGrupo");
            int idCreadorIndex = cursor.getColumnIndex("IDCreador");
            int idTareaIndex = cursor.getColumnIndex("IDTarea");

            if (idGrupoIndex != -1 && nombreGrupoIndex != -1 && descripcionGrupoIndex != -1 && idCreadorIndex != -1 && idTareaIndex != -1) {
                int idGrupo = cursor.getInt(idGrupoIndex);
                String nombreGrupo = cursor.getString(nombreGrupoIndex);
                String descripcionGrupo = cursor.getString(descripcionGrupoIndex);
                int idCreador = cursor.getInt(idCreadorIndex);
                int idTarea = cursor.getInt(idTareaIndex);

                // Obtener el nombre del creador
                String nombreCreador = obtenerInfoUsuario(db, idCreador);

                // Obtener la descripción de la tarea
                String descripcionTarea = obtenerInfoTarea(db, idTarea);

                String grupoInfo = "ID: " + idGrupo + ", Nombre: " + nombreGrupo + ", Descripción: " + descripcionGrupo + ", Creador: " + nombreCreador + ", Tarea: " + descripcionTarea;
                gruposList.add(grupoInfo);
            }
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gruposList);
        listViewGrupos.setAdapter(adapter);
    }

    private String obtenerInfoUsuario(SQLiteDatabase db, int idUsuario) {
        Cursor cursor = db.rawQuery("SELECT idUsuarios, nombre FROM Usuarios WHERE idUsuarios = ?", new String[]{String.valueOf(idUsuario)});
        String usuarioInfo = "";
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("idUsuarios");
            int nombreIndex = cursor.getColumnIndex("nombre");
            if (idIndex != -1 && nombreIndex != -1) {
                int id = cursor.getInt(idIndex);
                String nombre = cursor.getString(nombreIndex);
                usuarioInfo = "ID: " + id + ", Nombre: " + nombre;
            }
        }
        cursor.close();
        return usuarioInfo;
    }


    private String obtenerInfoTarea(SQLiteDatabase db, int idTarea) {
        Cursor cursor = db.rawQuery("SELECT idTarea, Descripcion FROM Tareas WHERE idTarea = ?", new String[]{String.valueOf(idTarea)});
        String tareaInfo = "";
        if (cursor.moveToFirst()) {
            int idTareaIndex = cursor.getColumnIndex("idTarea");
            int descripcionIndex = cursor.getColumnIndex("Descripcion");
            if (idTareaIndex != -1 && descripcionIndex != -1) {
                int id = cursor.getInt(idTareaIndex);
                String descripcion = cursor.getString(descripcionIndex);
                tareaInfo = "ID: " + id + ", Descripción: " + descripcion;
            }
        }
        cursor.close();
        return tareaInfo;
    }



}
