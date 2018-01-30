package me.mdjnewman.vetted.importer

import com.fasterxml.jackson.databind.ObjectMapper
import com.healthmarketscience.jackcess.DatabaseBuilder
import com.healthmarketscience.jackcess.Row
import com.healthmarketscience.jackcess.Table
import me.mdjnewman.vetted.client.ClientClient
import me.mdjnewman.vetted.model.AddressDTO
import me.mdjnewman.vetted.model.CreateClientCommandDTO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import retrofit2.Retrofit
import retrofit2.adapter.java8.Java8CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.File
import java.util.UUID
import java.util.concurrent.CompletableFuture

@SpringBootApplication
class Application {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @Bean
    fun init(mapper: ObjectMapper) = CommandLineRunner {

        val clientResource = Retrofit.Builder()
            .baseUrl("http://localhost:9001") // TODO
            .addCallAdapterFactory(Java8CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .build()
            .create(ClientClient::class.java)

        val database = DatabaseBuilder.open(File("/Users/mnewman/projects/mdbtools/main.accdb"))

//        database.tableNames.forEach { println(it) }
//        database.queries.forEach { println(it.name) }
//        ExportUtil.exportAll(database, File("/Users/mnewman/Desktop/foo"), "csv", true)

        val futures = database.getTable("Clients")
            .map {
                CreateClientCommandDTO(
                    clientId = UUID.randomUUID(),
                    name = getConcatenatedName(it["First Name"] as String?, it["Last Name/Trading Name"] as String?),
                    address = AddressDTO(
                        addressLineOne = it["Street Address/PO Box"] as String,
                        addressLineTwo = null,
                        postcode = getPostcode(it["Town"] as String, database.getTable("Postcodes")),
                        town = it["Town"] as String,
                        state = getState(it["Town"] as String, database.getTable("Postcodes"))
                    )
                )
            }
            .map { c ->
                val future = clientResource.create(c)
                logger.info("Created $c")
                future
            }
            .toTypedArray()

        CompletableFuture.allOf(*futures).get()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

private fun getConcatenatedName(fname: String?, lname: String?): String =
    listOf(fname, lname)
        .filter { it != null && it.trim().isNotEmpty() }
        .joinToString(" ")

private fun getPostcode(town: String, table: Table): String =
    findRowForTown(table, town)["Postcode"] as String

private fun getState(town: String, table: Table): String =
    findRowForTown(table, town)["State"] as String

private fun findRowForTown(table: Table, town: String): Row =
    table.firstOrNull { town.equals(it["Town"] as String, true) }
        ?: throw RuntimeException("No row for town: $town")
