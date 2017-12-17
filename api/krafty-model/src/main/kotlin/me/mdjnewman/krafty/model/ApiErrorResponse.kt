package me.mdjnewman.krafty.model

interface ApiErrorResponse<out D, out E> {
    val errorCode: E
    val details: List<D>
}
