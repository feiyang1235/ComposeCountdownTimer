/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _number = MutableLiveData("")
    val number: LiveData<String> = _number
    private val _state = MutableLiveData(BoxState.EDITABLE_STATE)
    val state: LiveData<BoxState> = _state

    fun onTextFieldChanged(newNumber: String) {
        _number.value = newNumber
    }
    fun onStateChanged(newState: BoxState) {
        _state.value = newState
    }
}
enum class BoxState {
    EDITABLE_STATE,
    COUNTDOWN_STATE
}
