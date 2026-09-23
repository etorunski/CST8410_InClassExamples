package com.example.cst8410_inclassexamples

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = MainRepository(application.applicationContext)

    private val _nameData = MutableStateFlow(repository.getData())
    val nameData: StateFlow<String> = _nameData.asStateFlow()

    fun setFirstName(name: String) {
        repository.setFirstName(name)
        _nameData.value = repository.getData()
    }
}
