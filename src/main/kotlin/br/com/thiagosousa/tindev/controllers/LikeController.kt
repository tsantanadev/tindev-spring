package br.com.thiagosousa.tindev.controllers

import br.com.thiagosousa.tindev.repositorys.DevRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/devs")
class LikeController(val repository: DevRepository){

    @PostMapping("/{targetId}/likes")
    fun like(@PathVariable targetId: String, @RequestHeader user: String): ResponseEntity<Void> {
        val targetDev = repository.findById(targetId).get()
        val loggedDev = repository.findById(user).get()

        loggedDev.likes.add(targetDev._id!!)
        repository.save(loggedDev)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{targetId}/dislikes")
    fun dislike(@PathVariable targetId: String, @RequestHeader user: String): ResponseEntity<Void> {
        val targetDev = repository.findById(targetId).get()
        val loggedDev = repository.findById(user).get()

        loggedDev.dislikes.add(targetDev._id!!)
        repository.save(loggedDev)
        return ResponseEntity.ok().build()
    }
}