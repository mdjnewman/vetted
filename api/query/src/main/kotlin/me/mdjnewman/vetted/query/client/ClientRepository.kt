package me.mdjnewman.vetted.query.client

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.Entity

@Repository
interface ClientRepository : JpaRepository<ClientEntry, String>

@Entity
class ClientEntry
