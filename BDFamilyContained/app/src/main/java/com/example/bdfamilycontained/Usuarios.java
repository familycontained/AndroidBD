package com.example.bdfamilycontained;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    }

    private void modificarUsuarios() {

    }

    private void eliminarUsuario() {

    }

    private void listarUsuarios() {

    }

}
