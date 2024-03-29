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

        Button btnVolverAtras = findViewById(R.id.volverAtras);
        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Grupos.this, MainActivity.class);
                startActivity(intent);
                finish();
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

        llenarSpinnerConDatos(spinnerCreadorGrupo, obtenerInfoUsuarios());
        llenarSpinnerConDatos(spinnerTareaGrupo, obtenerInfoTareas());

        btnAgregarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idCreador = obtenerIdDeSpinner(spinnerCreadorGrupo, obtenerIdsGrupo());
                int idTarea = obtenerIdDeSpinner(spinnerTareaGrupo, obtenerIdsGrupo());

                String nombreGrupo = etNombreGrupo.getText().toString();
                String descripcionGrupo = etDescripcionGrupo.getText().toString();

                if (!nombreGrupo.isEmpty() && !descripcionGrupo.isEmpty() && idCreador != -1 && idTarea != -1) {
                    guardarGrupo(nombreGrupo, descripcionGrupo, idCreador, idTarea);
                } else {
                    Toast.makeText(Grupos.this, "Por favor, complete todos los campos y seleccione un creador y una tarea.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btnVolverAtras = findViewById(R.id.volverAtras);
        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Grupos.this, Grupos.class);
                startActivity(intent);
                finish();
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

    private int obtenerIdDeSpinner(Spinner spinner, ArrayList<Integer> integers) {
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
        setContentView(R.layout.modificar_grupo);

        Spinner spinnerGrupos = findViewById(R.id.spinnerGrupos);
        EditText etNuevoNombreGrupo = findViewById(R.id.etNuevoNombreGrupo);
        EditText etNuevaDescripcionGrupo = findViewById(R.id.etNuevaDescripcionGrupo);
        Spinner spinnerNuevasTareas = findViewById(R.id.spinnerNuevasTareas);
        Button btnModificarRelacion = findViewById(R.id.btnModificarRelacion);

        ArrayList<String> infoTareas = obtenerInfoTareas();
        llenarSpinnerConDatos(spinnerNuevasTareas, infoTareas);

        ArrayList<String> infoGrupos = obtenerInfoGrupos();
        llenarSpinnerConDatos(spinnerGrupos, infoGrupos);

        btnModificarRelacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idGrupo = obtenerIdDeSpinner(spinnerGrupos, obtenerIdsGrupo());
                String nuevoNombreGrupo = etNuevoNombreGrupo.getText().toString();
                String nuevaDescripcionGrupo = etNuevaDescripcionGrupo.getText().toString();
                int idNuevaTarea = obtenerIdDeSpinner(spinnerNuevasTareas, obtenerIdsTarea());

                if (idGrupo == -1) {
                    Toast.makeText(Grupos.this, "Seleccione un grupo existente.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!nuevoNombreGrupo.isEmpty() || !nuevaDescripcionGrupo.isEmpty() || idNuevaTarea != obtenerIdTareaActualDelGrupo(idGrupo)) {
                    actualizarGrupoEnLaBaseDeDatos(idGrupo, nuevoNombreGrupo, nuevaDescripcionGrupo, idNuevaTarea);
                    etNuevoNombreGrupo.setText("");
                    etNuevaDescripcionGrupo.setText("");
                } else {
                    Toast.makeText(Grupos.this, "Realice al menos un cambio o seleccione una nueva tarea.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button btnVolverAtras = findViewById(R.id.volverAtras);
        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Grupos.this, Grupos.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private int obtenerIdTareaActualDelGrupo(int idGrupo) {
        int idTareaActual = -1;

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {"IDTarea"};
        String selection = "IDGrupo = ?";
        String[] selectionArgs = {String.valueOf(idGrupo)};

        Cursor cursor = db.query("Grupos", projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int idTareaColumnIndex = cursor.getColumnIndex("IDTarea");
            if (idTareaColumnIndex != -1) {
                idTareaActual = cursor.getInt(idTareaColumnIndex);
            }
        }

        cursor.close();
        db.close();

        return idTareaActual;
    }


    private ArrayList<String> obtenerInfoGrupos() {
        ArrayList<String> info = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT IDGrupo, NombreGrupo FROM Grupos", null);
        int idGrupoColumnIndex = cursor.getColumnIndex("IDGrupo");
        int nombreGrupoColumnIndex = cursor.getColumnIndex("NombreGrupo");

        if (idGrupoColumnIndex != -1 && nombreGrupoColumnIndex != -1) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(idGrupoColumnIndex);
                String nombre = cursor.getString(nombreGrupoColumnIndex);
                info.add(id + " - " + nombre);
            }
        }
        cursor.close();
        db.close();

        return info;
    }


    private void actualizarGrupoEnLaBaseDeDatos(int idGrupo, String nuevoNombre, String nuevaDescripcion, int idNuevaTarea) {
        DatabaseHelper dbHelper = new DatabaseHelper(Grupos.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (!nuevoNombre.isEmpty()) {
            values.put("NombreGrupo", nuevoNombre);
        }
        if (!nuevaDescripcion.isEmpty()) {
            values.put("DescripcionGrupo", nuevaDescripcion);
        }
        values.put("IDTarea", idNuevaTarea);

        int affectedRows = db.update("Grupos", values, "IDGrupo = ?", new String[]{String.valueOf(idGrupo)});
        db.close();

        if (affectedRows > 0) {
            Toast.makeText(Grupos.this, "Grupo actualizado con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Grupos.this, "Error al actualizar el grupo", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<Integer> obtenerIdsTarea() {
        ArrayList<Integer> ids = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT IDTarea FROM Tareas", null);
        int idColumnIndex = cursor.getColumnIndex("IDTarea");

        if (idColumnIndex != -1) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(idColumnIndex);
                ids.add(id);
            }
        }

        cursor.close();
        db.close();

        return ids;
    }

    private ArrayList<Integer> obtenerIdsGrupo() {
        ArrayList<Integer> ids = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT IDGrupo FROM Grupos", null);
        int idColumnIndex = cursor.getColumnIndex("IDGrupo");

        if (idColumnIndex != -1) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(idColumnIndex);
                ids.add(id);
            }
        }

        cursor.close();
        db.close();

        return ids;
    }


    private void eliminarGrupo() {
        setContentView(R.layout.eliminar_grupo);
        Spinner spinnerGrupos = findViewById(R.id.spinnerEliminarGrupo);
        Button btnEliminar = findViewById(R.id.btnEliminar);

        ArrayList<String> infoGrupos = obtenerInfoGrupos();
        llenarSpinnerConDatos(spinnerGrupos, infoGrupos);

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idGrupo = obtenerIdDeSpinner(spinnerGrupos, obtenerIdsGrupo());
                if (idGrupo != -1) {
                    eliminarGrupoDeBaseDeDatos(idGrupo);
                } else {
                    Toast.makeText(Grupos.this, "Por favor, seleccione un grupo válido.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnVolverAtras = findViewById(R.id.volverAtras);
        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Grupos.this, Grupos.class);
                startActivity(intent);
                finish();
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

                String nombreCreador = obtenerInfoUsuario(db, idCreador);

                String descripcionTarea = obtenerInfoTarea(db, idTarea);

                String grupoInfo = "ID: " + idGrupo + ", Nombre: " + nombreGrupo + ", Descripción: " + descripcionGrupo + ", Creador: " + nombreCreador + ", Tarea: " + descripcionTarea;
                gruposList.add(grupoInfo);
            }
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gruposList);
        listViewGrupos.setAdapter(adapter);

        Button btnVolverAtras = findViewById(R.id.volverAtras);
        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Grupos.this, Grupos.class);
                startActivity(intent);
                finish();
            }
        });
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
