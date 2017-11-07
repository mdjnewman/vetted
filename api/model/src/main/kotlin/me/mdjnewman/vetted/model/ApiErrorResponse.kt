package me.mdjnewman.vetted.model

interface ApiErrorResponse<out T> {
    val httpStatus: Int
    val details: List<T>
}