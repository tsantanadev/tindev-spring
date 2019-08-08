package br.com.thiagosousa.tindev.repositorys

import br.com.thiagosousa.tindev.models.Dev
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface DevRepository : MongoRepository<Dev, String> {

    fun findByUser(username: String): Optional<Dev>
}