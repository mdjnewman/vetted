package me.mdjnewman.vetted.importer

import com.healthmarketscience.jackcess.DatabaseBuilder
import com.healthmarketscience.jackcess.Row
import com.healthmarketscience.jackcess.Table
import me.mdjnewman.vetted.client.VettedClientResource
import me.mdjnewman.vetted.model.AddressDTO
import me.mdjnewman.vetted.model.CreateClientCommandDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.feign.EnableFeignClients
import org.springframework.context.annotation.Bean
import java.io.File
import java.util.UUID
import java.util.concurrent.CompletableFuture

@SpringBootApplication
@EnableFeignClients(basePackageClasses = arrayOf(VettedClientResource::class))
class Application {

    @Bean
    fun init(
        clientResource: VettedClientResource,
        @Value("\${vetted-web.ribbon.listOfServers}") servers: List<String>
    ) = CommandLineRunner {
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
            .map { c -> clientResource.create(c) }
            .toTypedArray()

        CompletableFuture.allOf(*futures)
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
