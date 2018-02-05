package me.mdjnewman.vetted.web

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class VettedWebApplication

fun main(args: Array<String>) {
    SpringApplication.run(VettedWebApplication::class.java, *args)
}
