package me.mdjnewman.vetted

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class VettedApplication

fun main(args: Array<String>) {
    SpringApplication.run(VettedApplication::class.java, *args)
}
