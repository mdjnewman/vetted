package me.mdjnewman.krafty.model

data class UnknownErrorResponse<out E>(
    override val errorCode: E
) : ApiErrorResponse<Unit, E> {
    override val details: List<Unit> = emptyList()
}