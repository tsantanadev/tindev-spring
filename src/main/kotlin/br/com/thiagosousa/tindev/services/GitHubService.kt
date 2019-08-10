package br.com.thiagosousa.tindev.services

import br.com.thiagosousa.tindev.controllers.dtos.DevForm
import br.com.thiagosousa.tindev.models.Dev
import br.com.thiagosousa.tindev.models.Language
import br.com.thiagosousa.tindev.models.Repository
import br.com.thiagosousa.tindev.repositorys.LanguageRepository
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.security.InvalidParameterException

@Service
class GitHubService(val languageRepository: LanguageRepository) {

    private val restTemplate: RestTemplate = RestTemplate()

    fun getUser(username: String): Dev {
        val url = "https://api.github.com/users/$username"

        val devResponse: DevForm = restTemplate.getForObject(url, DevForm::class)
                ?: throw InvalidParameterException("User not found")

        return Dev(devForm = devResponse, languages = getLanguages(devResponse.repos_url))
    }

    fun getLanguages(url: String): MutableMap<String, Long> {
        val response: ResponseEntity<List<Repository>> = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<List<Repository>>() {}
        )

        val languages: MutableMap<String, Long> = hashMapOf()

        response.body?.forEach { repository ->
            if (!repository.fork) {
                val mapResponse: HashMap<String, Long>? = restTemplate.getForObject(repository.languages_url, HashMap::class)

                mapResponse?.forEach { langResponse ->
                    val langOptional = languageRepository.findByName(langResponse.key)

                    val language = if (langOptional.isPresent) langOptional.get()
                    else languageRepository.save(Language(name = langResponse.key))

                    println("Repo: ${repository.name}, lang:$language, value: ${langResponse.value}")

                    if (languages.contains(language.id))
                        languages[language.id!!] = languages[language.id]!!.plus(langResponse.value)
                    else {
                        languages[language.id!!] = langResponse.value
                    }
                }

            }
        }

        return languages
    }
}
