package br.com.thiagosousa.tindev.controllers

import br.com.thiagosousa.tindev.controllers.dtos.DevDTO
import br.com.thiagosousa.tindev.controllers.dtos.DevForm
import br.com.thiagosousa.tindev.controllers.dtos.DevRequest
import br.com.thiagosousa.tindev.models.Dev
import br.com.thiagosousa.tindev.models.Language
import br.com.thiagosousa.tindev.models.Repository
import br.com.thiagosousa.tindev.repositorys.DevRepository
import br.com.thiagosousa.tindev.repositorys.LanguageRepository
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@RestController
@RequestMapping(value = ["/devs"])
class DevController(val repository: DevRepository, val langRepository: LanguageRepository) {

    @GetMapping
    fun index(@RequestHeader("user") user: String): ResponseEntity<List<Dev>> {
        val loggedDev = repository.findById(user).get()

        val devs = repository.findAll()

        devs.removeAll {
            loggedDev._id.equals(it._id) || loggedDev.likes.contains(it._id) || loggedDev.dislikes.contains(it._id)
        }

        return ResponseEntity.ok().body(devs)
    }

    @PostMapping
    fun insert(@RequestBody devRequest: DevRequest): ResponseEntity<DevDTO>{
        val registeredDev = repository.findByUser(devRequest.username)

        if (registeredDev.isPresent)
            return ResponseEntity.ok(DevDTO(registeredDev.get()))

        val url = "https://api.github.com/users/${devRequest.username}"

        val restTemplate = RestTemplate()

        val devResponse: DevForm = restTemplate.getForObject(url, DevForm::class)
                ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()

        var dev = Dev(devForm = devResponse)

        val reposResponse: ResponseEntity<List<Repository>> = restTemplate.exchange(
                devResponse.repos_url,
                HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<List<Repository>>(){}
                )

        reposResponse.body?.forEach {repo ->
            if (!repo.fork){
                val langsMap: HashMap<String, Long>? = restTemplate.getForObject(repo.languages_url, HashMap::class)

                langsMap?.forEach { langResponse ->
                    val langOptional = langRepository.findByName(langResponse.key)
                    val lang: Language

                    lang = if (langOptional.isPresent)
                        langOptional.get()
                    else
                        langRepository.save(Language(name = langResponse.key))

                    println("Repo: ${repo.name}, lang:$lang, value: ${langResponse.value}")

                    if (dev.languages.contains(lang.id))
                        dev.languages[lang.id!!] = dev.languages[lang.id]!!.plus(langResponse.value)
                    else {
                        dev.languages[lang.id!!] = langResponse.value
                    }
                }

            }
        }

        dev = repository.save(dev)

        return ResponseEntity.status(HttpStatus.CREATED).body(DevDTO(dev))
    }
}