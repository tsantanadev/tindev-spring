package br.com.thiagosousa.tindev.controllers

import br.com.thiagosousa.tindev.controllers.dtos.DevForm
import br.com.thiagosousa.tindev.controllers.dtos.DevRequest
import br.com.thiagosousa.tindev.models.Dev
import br.com.thiagosousa.tindev.repositorys.DevRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@RestController
@RequestMapping(value = ["/devs"])
class DevController(val repository: DevRepository) {

    @GetMapping
    fun index(@RequestHeader("devId") devId: String, pageable: Pageable): Page<Dev> {
        val loggedDev = repository.findById(devId).get()

        val devs = repository.findAll(pageable)

        devs.removeAll {
            loggedDev.id.equals(it.id) || loggedDev.likes.contains(it.id) || loggedDev.dislikes.contains(it.id)
        }

        return devs
    }

    @PostMapping
    fun insert(@RequestBody devRequest: DevRequest): ResponseEntity<Dev>{
        val registeredDev = repository.findByUsername(devRequest.username)

        if (registeredDev.isPresent)
            return ResponseEntity.ok(registeredDev.get())

        val url = "https://api.github.com/users/${devRequest.username}"

        val devResponse: DevForm = RestTemplate().getForObject(url, DevForm::class)
                ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()

        val dev = repository.save(Dev(devForm = devResponse))

        return ResponseEntity.status(HttpStatus.CREATED).body(dev)
    }
}