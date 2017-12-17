package me.mdjnewman.krafty.model

data class ResourceNotFoundResponse<out E>(
    override val details: List<ResourceNotFoundDetails>,
    override val errorCode: E
) : ApiErrorResponse<ResourceNotFoundDetails, E>
