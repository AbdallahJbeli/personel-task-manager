package com.work.work.personaltaskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nom et version de la base de données
    private static final String DATABASE_NAME = "TaskManager.db";
    private static final int DATABASE_VERSION = 5;

    // Nom de la table et des colonnes
    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IS_COMPLETED = "is_completed";

    // Constructeur de la classe
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Méthode appelée lors de la création de la base de données
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Création de la table des tâches avec les colonnes nécessaires
        String createTable = "CREATE TABLE " + TABLE_TASKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_IS_COMPLETED + " INTEGER DEFAULT 0)";
        db.execSQL(createTable); // Exécution de la requête SQL pour créer la table
    }

    // Méthode appelée lors de la mise à jour de la base de données
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Suppression de la table existante et recréation de la table mise à jour
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    // Méthode pour ajouter une tâche à la base de données
    public long addTask(String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);  // Ajoute le titre
        values.put(COLUMN_DESCRIPTION, description);  // Ajoute la description
        return db.insert(TABLE_TASKS, null, values);  // Insère les valeurs dans la table
    }

    // Méthode pour mettre à jour le statut d'une tâche (complétée ou non)
    public void updateTaskCompletion(int taskId, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_COMPLETED, isCompleted ? 1 : 0);  // Si la tâche est complétée, mettre à 1, sinon à 0
        db.update(TABLE_TASKS, values, COLUMN_ID + "=?", new String[]{String.valueOf(taskId)});  // Mise à jour de la tâche
    }

    // Méthode pour supprimer une tâche de la base de données
    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_ID + "=?", new String[]{String.valueOf(taskId)});  // Suppression de la tâche par ID
    }

    // Méthode pour récupérer toutes les tâches de la base de données
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();  // Liste pour stocker toutes les tâches
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, null, null, null, null, null);  // Récupère toutes les tâches

        // Vérifie si le curseur contient des données
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Récupère les valeurs de chaque colonne pour chaque tâche
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                boolean isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1;  // Convertit le statut en boolean
                taskList.add(new Task(id, title, description, isCompleted));  // Ajoute la tâche à la liste
            } while (cursor.moveToNext());  // Passe à la tâche suivante
            cursor.close();  // Ferme le curseur
        }
        return taskList;  // Retourne la liste de toutes les tâches
    }
}
