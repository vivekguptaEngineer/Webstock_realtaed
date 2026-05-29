package com.example.recall_dialogue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Custom_dailogue : ViewModel() {
    private val _socketStatus = MutableLiveData(false)
    val socketStatus: LiveData<Boolean> = _socketStatus

    private val _message = MutableLiveData<Pair<Boolean, String>>()
    val message: LiveData<Pair<Boolean, String>> = _message

    fun setStatus(status: Boolean) = GlobalScope.launch(Dispatchers.Main) {
        _socketStatus.value = status
    }

    fun setMessage(message: Pair<Boolean, String>) = GlobalScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {
            _message.value = message
        }
    }
}
