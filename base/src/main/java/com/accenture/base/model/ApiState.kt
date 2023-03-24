package com.accenture.base.model

sealed class ApiState<T> {
    class Success<T> : ApiState<T>()
    class Error<T>(val exception: T) : ApiState<T>()
}

sealed class ResultApiState<T, Q> {
    class Success<T, Q>(val value: T) : ResultApiState<T, Q>()
    class Error<T, Q>(val exception: Q) : ResultApiState<T, Q>()
}