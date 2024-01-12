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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Usuarios extends Activity {

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
    }

    private void modificarUsuarios() {
        setContentView(R.layout.modificar_usuario);
    }

    private void eliminarUsuario() {
        setContentView(R.layout.eliminar_usuario);
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
    }

}
