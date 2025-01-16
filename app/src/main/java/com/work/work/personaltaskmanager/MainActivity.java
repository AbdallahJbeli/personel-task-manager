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

    private RecyclerView taskRecyclerView;
    private FloatingActionButton fabAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        fabAddTask = findViewById(R.id.fabAddTask);

        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Task> taskList = dbHelper.getAllTasks();
        TaskAdapter adapter = new TaskAdapter(taskList, this::loadTasks, this::onTaskChecked);
        taskRecyclerView.setAdapter(adapter);
    }

    private void onTaskChecked(Task task, boolean isChecked) {
        // Handle the checkbox action here
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        task.setCompleted(isChecked);
        dbHelper.updateTaskCompletion(task.getId(), task.isCompleted()); // Update task in the database
        String status = isChecked ? "completed" : "not completed";
        Toast.makeText(this, "Task \"" + task.getTitle() + "\" marked as " + status, Toast.LENGTH_SHORT).show();
    }
}
