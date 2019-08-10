package br.com.thiagosousa.tindev.controllers

import br.com.thiagosousa.tindev.controllers.dtos.DevDTO
import br.com.thiagosousa.tindev.controllers.dtos.DevRequest
import br.com.thiagosousa.tindev.models.Dev
import br.com.thiagosousa.tindev.services.DevService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/devs"])
class DevController(val service: DevService) {

    @GetMapping
    fun index(@RequestHeader("user") user: String): ResponseEntity<List<Dev>> {
        val devList = service.listAll(user)
        return ResponseEntity.ok().body(devList)
    }

    @PostMapping
    fun insert(@RequestBody devRequest: DevRequest): ResponseEntity<DevDTO>{
        val dev = service.insert(devRequest.username)
        return ResponseEntity.status(HttpStatus.CREATED).body(DevDTO(dev))
    }
}