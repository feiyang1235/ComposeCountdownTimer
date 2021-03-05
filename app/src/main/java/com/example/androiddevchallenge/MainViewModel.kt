package com.example.androiddevchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel() {
    private val _number = MutableLiveData("")
    val number: LiveData<String> = _number
    private val _state = MutableLiveData(BoxState.EDITABLE_STATE)
    val state: LiveData<BoxState> = _state

    fun onTextFieldChanged(newNumber: String) {
        _number.value = newNumber
    }
    fun onStateChanged(newState:BoxState){
        _state.value = newState
    }
}
enum class BoxState {
    EDITABLE_STATE,
    COUNTDOWN_STATE
}