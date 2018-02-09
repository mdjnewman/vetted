package me.mdjnewman.vetted.query.client

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id

@Repository
interface ClientEntryRepository : JpaRepository<ClientEntry, UUID>

@Entity
class ClientEntry(
    @Id
    val id: UUID
)