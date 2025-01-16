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

    private List<Task> taskList;
    private Runnable refreshCallback;
    private OnTaskCheckedListener onTaskCheckedListener;

    public interface OnTaskCheckedListener {
        void onTaskChecked(Task task, boolean isChecked);
    }

    public TaskAdapter(List<Task> taskList, Runnable refreshCallback, OnTaskCheckedListener onTaskCheckedListener) {
        this.taskList = taskList;
        this.refreshCallback = refreshCallback;
        this.onTaskCheckedListener = onTaskCheckedListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTitle.setText(task.getTitle());
        holder.tvDescription.setText(task.getDescription());
        holder.cbTaskComplete.setChecked(task.isCompleted());

        // Set the CheckBox listener to update the task completion state
        holder.cbTaskComplete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Notify MainActivity about the checkbox state change
            if (onTaskCheckedListener != null) {
                onTaskCheckedListener.onTaskChecked(task, isChecked);
            }
        });

        // Set the Delete button listener
        holder.btnDeleteTask.setOnClickListener(v -> {
            // Delete the task from the database
            DatabaseHelper dbHelper = new DatabaseHelper(holder.itemView.getContext());
            dbHelper.deleteTask(task.getId());
            refreshCallback.run();  // Refresh the task list after deletion
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;
        CheckBox cbTaskComplete;
        Button btnDeleteTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvDescription = itemView.findViewById(R.id.tvTaskDescription);
            cbTaskComplete = itemView.findViewById(R.id.cbTaskComplete);
            btnDeleteTask = itemView.findViewById(R.id.btnDeleteTask);
        }
    }
}
