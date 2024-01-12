package com.example.bdfamilycontained;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    }

    private void crearTarea() {

    }

    private void modificarTareas() {

    }

    private void eliminarTarea() {

    }

    private void listarTareas() {

    }


}
