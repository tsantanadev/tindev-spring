package br.com.thiagosousa.tindev.controllers

import br.com.thiagosousa.tindev.controllers.dtos.DevForm
import br.com.thiagosousa.tindev.controllers.dtos.DevRequest
import br.com.thiagosousa.tindev.models.Dev
import br.com.thiagosousa.tindev.repositorys.DevRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@RestController
@RequestMapping(value = ["/devs"])
class DevController(val repository: DevRepository) {

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
    fun insert(@RequestBody devRequest: DevRequest): ResponseEntity<Dev>{
        val registeredDev = repository.findByUser(devRequest.username)

        if (registeredDev.isPresent)
            return ResponseEntity.ok(registeredDev.get())

        val url = "https://api.github.com/users/${devRequest.username}"

        val devResponse: DevForm = RestTemplate().getForObject(url, DevForm::class)
                ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()

        val dev = repository.save(Dev(devForm = devResponse))

        return ResponseEntity.status(HttpStatus.CREATED).body(dev)
    }
}