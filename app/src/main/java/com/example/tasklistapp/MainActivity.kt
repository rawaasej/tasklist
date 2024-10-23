package com.example.tasklistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasklistapp.model.Task
import com.example.tasklistapp.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: TaskViewModel = viewModel()
            TaskListScreen(viewModel)
        }
    }
}

@Composable
fun TaskListScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn {
            this.items(tasks) { task ->
                TaskItem(
                    task = task,
                    onStatusChange = { newStatus -> viewModel.updateTaskStatus(task.id, newStatus) },
                    onDelete = { viewModel.deleteTask(task.id) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        var newTaskTitle by remember { mutableStateOf("") }
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = newTaskTitle,
                onValueChange = { newTaskTitle = it },
                label = { Text("Nouvelle Tâche") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (newTaskTitle.isNotBlank()) {
                    viewModel.addTask(newTaskTitle)
                    newTaskTitle = ""
                }
            }) {
                Text("Ajouter")
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onStatusChange: (Boolean) -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = task.title + if (task.isCompleted) " (Terminé)" else "", modifier = Modifier.weight(1f))
        Button(onClick = { onStatusChange(!task.isCompleted) }) {
            Text(if (task.isCompleted) "Marquer comme non terminé" else "Marquer comme terminé")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onDelete) {
            Text("Supprimer")
        }
    }
}
