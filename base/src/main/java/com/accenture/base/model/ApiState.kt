package com.accenture.base.model

sealed class ApiState<T> {
    class Success<T> : ApiState<T>()
    class Error<T>(val error: T) : ApiState<T>()
}