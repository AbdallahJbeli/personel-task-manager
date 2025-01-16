package com.work.work.personaltaskmanager;

public class Task {
    // Déclaration des variables d'instance pour l'ID, le titre, la description et l'état de complétion
    private int id;
    private String title;
    private String description;
    private boolean isCompleted;

    // Constructeur de la classe Task pour initialiser les valeurs de l'objet
    public Task(int id, String title, String description, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    // Getter pour l'ID de la tâche
    public int getId() {
        return id;
    }

    // Getter pour le titre de la tâche
    public String getTitle() {
        return title;
    }

    // Getter pour la description de la tâche
    public String getDescription() {
        return description;
    }

    // Getter pour vérifier si la tâche est complétée
    public boolean isCompleted() {
        return isCompleted;
    }

    // Setter pour définir l'état de complétion de la tâche
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
