package com.example.meridia_interview_task.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meridia_interview_task.data.network.FeatureApiService
import com.example.meridia_interview_task.utilities.Error
import com.example.meridia_interview_task.utilities.FeatureStatus
import com.example.meridia_interview_task.utilities.Loading
import com.example.meridia_interview_task.utilities.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MapsFragmentViewModel : ViewModel() {
    val featureStatus = MutableLiveData<FeatureStatus>()

    init {
        featureStatus.postValue(Loading(true))

        getFeatures()
    }
    private fun getFeatures() {
        val apiService = FeatureApiService()

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val featureResponse = apiService.getFeatures().await()
                featureStatus.postValue(Success(featureResponse.features))
            }
            catch (e: Exception){
                featureStatus.postValue(Error(e))
            }
        }
    }
}
