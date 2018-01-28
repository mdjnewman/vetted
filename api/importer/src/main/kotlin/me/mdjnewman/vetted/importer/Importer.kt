package me.mdjnewman.vetted.importer

import com.healthmarketscience.jackcess.DatabaseBuilder
import com.healthmarketscience.jackcess.util.ExportUtil
import me.mdjnewman.vetted.client.VettedClientResource
import me.mdjnewman.vetted.model.AddClientNoteCommandDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.feign.EnableFeignClients
import org.springframework.context.annotation.Bean
import java.io.File
import java.util.UUID

@SpringBootApplication
@EnableFeignClients(basePackageClasses = arrayOf(VettedClientResource::class))
class Application {

    @Bean
    fun init(
        clientResource: VettedClientResource,
        @Value("\${vetted-web.ribbon.listOfServers}") servers: List<String>
    ) = CommandLineRunner {
        print(servers)
        clientResource.addNote(AddClientNoteCommandDTO(UUID.randomUUID(), "asd"))
        print(clientResource.toString())
    }
}

fun main(args: Array<String>) {
    val database = DatabaseBuilder.open(File("/Users/mnewman/projects/mdbtools/main.accdb"))
    database.tableNames.forEach { println(it) }
    database.queries.forEach { println(it.name) }

    ExportUtil.exportAll(database, File("/Users/mnewman/Desktop/foo"), ".csv", true)

    val table = database.getTable("Transactions")
    table.last().let { println("Column 'TransactionID' has value: " + it["TransactionID"]) }
}
