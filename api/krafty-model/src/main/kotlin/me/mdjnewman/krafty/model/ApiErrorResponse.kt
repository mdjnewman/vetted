package me.mdjnewman.krafty.model

interface ApiErrorResponse<out T> {
    val httpStatus: Int
    val details: List<T>
}