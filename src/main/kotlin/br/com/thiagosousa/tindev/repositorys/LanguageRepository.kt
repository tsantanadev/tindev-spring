package br.com.thiagosousa.tindev.repositorys

import br.com.thiagosousa.tindev.models.Language
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface LanguageRepository : MongoRepository<Language, String> {

    fun findByName(name: String): Optional<Language>
}