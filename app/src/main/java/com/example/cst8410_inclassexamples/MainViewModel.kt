package com.example.cst8410_inclassexamples

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(
    private val repository: MainRepository = MainRepository()
) : ViewModel() {

    private val _textState = MutableStateFlow(repository.getText())
    val textState: StateFlow<String> = _textState.asStateFlow()

    fun onTextChanged(newText: String) {
        repository.updateText(newText)
        _textState.value = repository.getText()
    }
}
