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

    private val _textState = MutableStateFlow(repository.getText())
    val textState: StateFlow<String> = _textState.asStateFlow()

    fun onTextChanged(newText: String) {
        repository.updateText(newText)
        _textState.value = repository.getText()
    }
}
