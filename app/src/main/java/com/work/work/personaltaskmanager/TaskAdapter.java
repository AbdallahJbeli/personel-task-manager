package com.work.work.personaltaskmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    // Liste des tâches à afficher
    private List<Task> taskList;
    // Callback pour rafraîchir la liste des tâches après une suppression
    private Runnable refreshCallback;
    // Interface pour écouter les changements d'état de la case à cocher
    private OnTaskCheckedListener onTaskCheckedListener;

    // Interface pour gérer les événements de coche de la tâche
    public interface OnTaskCheckedListener {
        void onTaskChecked(Task task, boolean isChecked);
    }

    // Constructeur de l'adaptateur qui prend une liste de tâches, un callback de rafraîchissement et un écouteur pour les changements de case à cocher
    public TaskAdapter(List<Task> taskList, Runnable refreshCallback, OnTaskCheckedListener onTaskCheckedListener) {
        this.taskList = taskList;
        this.refreshCallback = refreshCallback;
        this.onTaskCheckedListener = onTaskCheckedListener;
    }

    // Méthode pour créer la vue d'un item de la liste
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate la vue pour chaque élément de la liste
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    // Méthode pour lier chaque vue d'élément avec les données des tâches
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTitle.setText(task.getTitle()); // Titre de la tâche
        holder.tvDescription.setText(task.getDescription()); // Description de la tâche
        holder.cbTaskComplete.setChecked(task.isCompleted()); // Etat de la case à cocher

        // Définir le comportement du listener de la case à cocher
        holder.cbTaskComplete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Notifie MainActivity du changement de l'état de la case à cocher
            if (onTaskCheckedListener != null) {
                onTaskCheckedListener.onTaskChecked(task, isChecked);
            }
        });

        // Définir le comportement du bouton de suppression
        holder.btnDeleteTask.setOnClickListener(v -> {
            // Supprimer la tâche de la base de données
            DatabaseHelper dbHelper = new DatabaseHelper(holder.itemView.getContext());
            dbHelper.deleteTask(task.getId());
            refreshCallback.run();  // Rafraîchir la liste des tâches après suppression
        });
    }

    // Retourner le nombre d'éléments dans la liste
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // Classe interne pour la vue de chaque élément de la liste (un item de tâche)
    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;
        CheckBox cbTaskComplete;
        Button btnDeleteTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTaskTitle); // Récupérer le TextView pour le titre
            tvDescription = itemView.findViewById(R.id.tvTaskDescription); // Récupérer le TextView pour la description
            cbTaskComplete = itemView.findViewById(R.id.cbTaskComplete); // Récupérer la CheckBox
            btnDeleteTask = itemView.findViewById(R.id.btnDeleteTask); // Récupérer le bouton de suppression
        }
    }
}
