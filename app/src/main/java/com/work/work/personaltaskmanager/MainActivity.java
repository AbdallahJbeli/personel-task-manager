package com.work.work.personaltaskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Déclaration des vues (RecyclerView et FloatingActionButton)
    private RecyclerView taskRecyclerView;
    private FloatingActionButton fabAddTask;

    // Méthode appelée lors de la création de l'activité
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Charge le layout de l'activité principale

        // Initialisation des vues
        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        fabAddTask = findViewById(R.id.fabAddTask);

        // Configuration du RecyclerView pour afficher la liste des tâches
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Action du bouton flottant pour ajouter une nouvelle tâche
        fabAddTask.setOnClickListener(v -> {
            // Crée une nouvelle activité pour ajouter une tâche
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);  // Démarre l'activité AddTaskActivity
        });
    }

    // Méthode appelée lorsque l'activité est reprise après avoir été mise en pause
    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();  // Charge la liste des tâches à chaque reprise de l'activité
    }

    // Méthode pour charger et afficher les tâches dans le RecyclerView
    private void loadTasks() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);  // Création d'un objet DatabaseHelper pour accéder aux données
        List<Task> taskList = dbHelper.getAllTasks();  // Récupère toutes les tâches de la base de données
        TaskAdapter adapter = new TaskAdapter(taskList, this::loadTasks, this::onTaskChecked);  // Crée un adaptateur avec la liste des tâches
        taskRecyclerView.setAdapter(adapter);  // Définit l'adaptateur pour le RecyclerView
    }

    // Méthode pour gérer l'action sur la case à cocher de la tâche (complétée ou non)
    private void onTaskChecked(Task task, boolean isChecked) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);  // Création d'un objet DatabaseHelper
        task.setCompleted(isChecked);  // Met à jour l'état de complétion de la tâche
        dbHelper.updateTaskCompletion(task.getId(), task.isCompleted());  // Met à jour la tâche dans la base de données
        String status = isChecked ? "completed" : "not completed";  // Message pour afficher le statut de la tâche
        Toast.makeText(this, "Task \"" + task.getTitle() + "\" marked as " + status, Toast.LENGTH_SHORT).show();  // Affiche un toast
    }
}
