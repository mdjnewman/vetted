package me.mdjnewman.vetted.query

import org.elasticsearch.action.ActionListener
import java.lang.Exception
import java.util.concurrent.CompletableFuture

inline fun <reified R> bindCompletableFuture(completableFuture: CompletableFuture<R>): ActionListener<R> {
    return object : ActionListener<R> {
        override fun onFailure(e: Exception?) {
            completableFuture.completeExceptionally(e)
        }

        override fun onResponse(response: R?) {
            completableFuture.complete(response)
        }
    }
}