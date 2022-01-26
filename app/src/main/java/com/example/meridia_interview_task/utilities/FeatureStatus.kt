package com.example.meridia_interview_task.utilities

sealed class FeatureStatus
data class Loading(val loading: Boolean = false) : FeatureStatus()
data class Success<T : Any>(var data: T) : FeatureStatus()
data class Error(val exception: Exception) : FeatureStatus()
