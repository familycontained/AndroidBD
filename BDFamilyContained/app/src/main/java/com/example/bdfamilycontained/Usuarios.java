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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Usuarios extends Activity {

    private ArrayList<Integer> usuariosIds;
    private ArrayList<Integer> tareasIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_usuarios);

        Button botonRegistroUsuarios = findViewById(R.id.aniadir_usuarios);
        botonRegistroUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearUsuario();
            }
        });

        Button botonModificarUsuarios = findViewById(R.id.modificar_usuario);
        botonModificarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarUsuarios();
            }
        });

        Button botonEliminarUsuario = findViewById(R.id.eliminar_usuario);
        botonEliminarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarUsuario();
            }
        });

        Button botonListarUsuarios = findViewById(R.id.listar_usuarios); // Asegúrate de que el ID es correcto
        botonListarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listarUsuarios();
            }
        });

        Button btnVolverAtras = findViewById(R.id.volverAtras);
        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Usuarios.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }






    private void crearUsuario() {
        setContentView(R.layout.registro_usuarios);

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
                    Toast.makeText(Usuarios.this, "Falta completar campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                int edad = Integer.parseInt(edadStr);
                DatabaseHelper dbHelper = new DatabaseHelper(Usuarios.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Verificar si el usuario ya existe (opcional, depende de tu lógica de negocio)
                // ...

                ContentValues values = new ContentValues();
                values.put("nombre", nombre);
                values.put("correo_electronico", correo);
                values.put("edad", edad);

                long result = db.insert("Usuarios", null, values);
                db.close();

                if (result == -1) {
                    Toast.makeText(Usuarios.this, "Usuario ya existe", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Usuarios.this, "Usuario creado", Toast.LENGTH_SHORT).show();
                    nombreEditText.setText("");
                    correoEditText.setText("");
                    edadEditText.setText("");
                }
            }
        });

        Button btnVolverAtras = findViewById(R.id.volverAtras);
        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Usuarios.this, Usuarios.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void modificarUsuarios() {
        setContentView(R.layout.modificar_usuario);

        Spinner spinnerUsuarios = findViewById(R.id.spinnerUsuario);
        EditText etNombre = findViewById(R.id.etNombre);
        EditText etCorreo = findViewById(R.id.etCorreo);
        Button btnModificar = findViewById(R.id.btnModificar);
        Button btnVolverAtras = findViewById(R.id.volverAtras);

        // Obtener información de usuarios y llenar el Spinner
        ArrayList<String> usuariosInfo = obtenerInfoUsuarios();
        llenarSpinnerConDatos(spinnerUsuarios, usuariosInfo);

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idUsuario = obtenerIdDeSpinner(spinnerUsuarios);
                String nuevoNombre = etNombre.getText().toString();
                String nuevoCorreo = etCorreo.getText().toString();

                // Verificar si al menos uno de los campos está lleno
                if (nuevoNombre.isEmpty() && nuevoCorreo.isEmpty()) {
                    Toast.makeText(Usuarios.this, "Ingrese al menos un campo para actualizar.", Toast.LENGTH_SHORT).show();
                    return;
                }

                actualizarUsuarioEnLaBaseDeDatos(idUsuario, nuevoNombre, nuevoCorreo);
            }
        });

        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Usuarios.this, Usuarios.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private int obtenerIdDeSpinner(Spinner spinner) {
        String selectedItem = (String) spinner.getSelectedItem();
        if (selectedItem != null && !selectedItem.isEmpty()) {
            String[] parts = selectedItem.split(" - "); // Suponiendo que el formato es "ID - Nombre"
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

    private void llenarSpinnerConDatos(Spinner spinner, ArrayList<String> datos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, datos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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

    private void actualizarUsuarioEnLaBaseDeDatos(int idUsuario, String nuevoNombre, String nuevoCorreo) {
        DatabaseHelper dbHelper = new DatabaseHelper(Usuarios.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (!nuevoNombre.isEmpty()) {
            values.put("nombre", nuevoNombre);
        }
        if (!nuevoCorreo.isEmpty()) {
            values.put("correo_electronico", nuevoCorreo);
        }

        int affectedRows = db.update("Usuarios", values, "idUsuarios = ?", new String[]{String.valueOf(idUsuario)});
        db.close();

        if (affectedRows > 0) {
            Toast.makeText(Usuarios.this, "Usuario actualizado.", Toast.LENGTH_SHORT).show();
            // Puedes realizar otras acciones aquí, como limpiar los campos o regresar a la actividad anterior.
        } else {
            Toast.makeText(Usuarios.this, "Error al actualizar el usuario o el usuario no existe.", Toast.LENGTH_SHORT).show();
        }
    }


    private void eliminarUsuario() {
        setContentView(R.layout.eliminar_usuario);

        Spinner spinnerUsuarios = findViewById(R.id.spinnerEliminarUsuario);
        Button btnEliminar = findViewById(R.id.btnEliminar);

        // Obtener información de usuarios y llenar el Spinner
        ArrayList<String> infoUsuarios = obtenerInfoUsuarios();
        llenarSpinnerConDatos(spinnerUsuarios, infoUsuarios);

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idUsuario = obtenerIdDeSpinner(spinnerUsuarios);
                if (idUsuario != -1) {
                    eliminarUsuarioDeLaBaseDeDatos(String.valueOf(idUsuario));
                } else {
                    Toast.makeText(Usuarios.this, "Por favor, seleccione un usuario.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnVolverAtras = findViewById(R.id.volverAtras);
        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Usuarios.this, Usuarios.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void eliminarUsuarioDeLaBaseDeDatos(String idUsuario) {
        DatabaseHelper dbHelper = new DatabaseHelper(Usuarios.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int deletedRows = db.delete("Usuarios", "idUsuarios = ?", new String[]{idUsuario});
        db.close();

        if (deletedRows > 0) {
            Toast.makeText(Usuarios.this, "Usuario eliminado.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Usuarios.this, "No se pudo eliminar el usuario.", Toast.LENGTH_SHORT).show();
        }
    }

    private void listarUsuarios() {
        setContentView(R.layout.listar_usuario);

        ListView listView = findViewById(R.id.listaUsuarios);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consulta para obtener los datos de los usuarios
        Cursor cursor = db.rawQuery("SELECT * FROM Usuarios", null);

        // Obtiene los índices de las columnas
        int idUsuariosIndex = cursor.getColumnIndex("idUsuarios");
        int nombreIndex = cursor.getColumnIndex("nombre");
        int correoIndex = cursor.getColumnIndex("correo_electronico");
        int edadIndex = cursor.getColumnIndex("edad");

        List<String> usuarios = new ArrayList<>();
        while(cursor.moveToNext()) {
            // Verifica que los índices de las columnas sean válidos
            String idUsuario = nombreIndex != -1 ? cursor.getString(idUsuariosIndex) : "ID no disponible";
            String nombre = nombreIndex != -1 ? cursor.getString(nombreIndex) : "Nombre no disponible";
            String correo = correoIndex != -1 ? cursor.getString(correoIndex) : "Correo no disponible";
            String edad = edadIndex != -1 ? Integer.toString(cursor.getInt(edadIndex)) : "Edad no disponible";

            // Formatear la información del usuario
            usuarios.add(idUsuario + " - " + nombre + " - " + correo + " - " + edad);
        }
        cursor.close();


        // Usar un ArrayAdapter para mostrar la lista de usuarios
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usuarios);
        listView.setAdapter(adapter);

        Button btnVolverAtras = findViewById(R.id.volverAtras);
        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Usuarios.this, Usuarios.class);
                startActivity(intent);
                finish();
            }
        });
    }




}
