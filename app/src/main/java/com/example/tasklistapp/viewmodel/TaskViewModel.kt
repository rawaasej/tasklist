package com.example.tasklistapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tasklistapp.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(generateInitialTasks())
    val tasks: StateFlow<List<Task>> get() = _tasks

    private companion object {
        fun generateInitialTasks(): List<Task> {
            return List(100) { index -> Task(id = index, title = "Tâche ${index + 1}") }
        }
    }

    fun addTask(title: String) {
        val newId = _tasks.value.size
        val newTask = Task(newId, title)
        _tasks.value = _tasks.value + newTask
    }

    fun deleteTask(id: Int) {
        _tasks.value = _tasks.value.filter { it.id != id }
    }

    fun updateTaskStatus(id: Int, isCompleted: Boolean) {
        _tasks.value = _tasks.value.map {
            if (it.id == id) it.copy(isCompleted = isCompleted) else it
        }
    }
}
