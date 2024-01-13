package com.example.bdfamilycontained;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        EditText nombreGrupoEditText = findViewById(R.id.nombreGrupo);
        EditText descripcionGrupoEditText = findViewById(R.id.descripcionGrupo);
        EditText idCreadorEditText = findViewById(R.id.idCreador);
        EditText idTareaEditText = findViewById(R.id.idTarea);
        Button submitButton = findViewById(R.id.AgregarGrupo);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreGrupo = nombreGrupoEditText.getText().toString();
                String descripcionGrupo = descripcionGrupoEditText.getText().toString();
                String idCreador = idCreadorEditText.getText().toString();
                String idTarea = idTareaEditText.getText().toString();

                if (nombreGrupo.isEmpty() || descripcionGrupo.isEmpty() || idCreador.isEmpty() || idTarea.isEmpty()) {
                    Toast.makeText(Grupos.this, "Falta completar campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseHelper dbHelper = new DatabaseHelper(Grupos.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("NombreGrupo", nombreGrupo);
                values.put("DescripcionGrupo", descripcionGrupo);
                values.put("IDCreador", idCreador);
                values.put("IDTarea", idTarea);

                long result = db.insert("Grupos", null, values);
                db.close();

                if (result == -1) {
                    Toast.makeText(Grupos.this, "Error al crear el grupo", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Grupos.this, "Grupo creado", Toast.LENGTH_SHORT).show();
                    nombreGrupoEditText.setText("");
                    descripcionGrupoEditText.setText("");
                    idCreadorEditText.setText("");
                    idTareaEditText.setText("");
                }
            }
        });
    }

    private void modificarGrupos() {

    }

    private void eliminarGrupo() {

    }

    private void listarGrupos() {

    }


}
