package com.example.bdfamilycontained;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Usuario_Tareas extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_usuario_tarea);

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

    }

    private void modificarUsuario_Tareas() {

    }

    private void eliminarUsuario_Tarea() {

    }

    private void listarUsuario_Tareas() {

    }


}
