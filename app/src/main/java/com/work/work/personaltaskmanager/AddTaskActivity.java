package com.work.work.personaltaskmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    // Déclaration des variables pour les champs de saisie et les boutons
    private EditText etTaskTitle, etTaskDescription;
    private Button btnSaveTask, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);  // Initialise le layout de l'activité

        // Initialisation des vues à partir du fichier XML
        etTaskTitle = findViewById(R.id.etTaskTitle);
        etTaskDescription = findViewById(R.id.etTaskDescription);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnBack = findViewById(R.id.btnBack);

        // Écouteur d'événements pour le bouton "Save Task"
        btnSaveTask.setOnClickListener(v -> {
            // Récupère les textes entrés par l'utilisateur dans les champs de saisie
            String title = etTaskTitle.getText().toString().trim();
            String description = etTaskDescription.getText().toString().trim();

            // Vérifie si les champs sont vides
            if (title.isEmpty() || description.isEmpty()) {
                // Si un champ est vide, afficher un message d'erreur
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else {
                // Si les champs sont remplis, ajouter la tâche à la base de données
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                long result = dbHelper.addTask(title, description);

                // Vérifie si l'ajout de la tâche a réussi
                if (result != -1) {
                    // Affiche un message de succès et termine l'activité
                    Toast.makeText(this, "Tâche enregistrée !", Toast.LENGTH_SHORT).show();
                    finish(); // Ferme l'activité actuelle et retourne à l'écran précédent
                } else {
                    // Si l'ajout échoue, affiche un message d'erreur
                    Toast.makeText(this, "Erreur lors de l'enregistrement de la tâche", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Écouteur d'événements pour le bouton "Retour"
        btnBack.setOnClickListener(v -> finish());  // Ferme l'activité et retourne à l'écran précédent
    }
}
