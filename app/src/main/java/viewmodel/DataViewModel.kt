package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class DataViewModel:ViewModel() {
    val backResponse: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

}