package me.mdjnewman.vetted.web.controller

import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.responsetypes.MultipleInstancesResponseType
import java.util.concurrent.CompletableFuture

inline fun <Q, reified R> QueryGateway.query(q: Q): CompletableFuture<R> {
    return this.query(q, R::class.java)
}

inline fun <Q, reified R> QueryGateway.queryMany(q: Q): CompletableFuture<Collection<R>> {
    return this.query(q, MultipleInstancesResponseType<R>(R::class.java))
        .thenApply { it!! }
}
