package com.example.cst8410_inclassexamples

class MainRepository {

    private var textValue: String = "Hello world"

    fun getText(): String {
        return textValue
    }

    fun updateText(newText: String) {
        textValue = newText
    }
}
