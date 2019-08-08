package br.com.thiagosousa.tindev.controllers

import br.com.thiagosousa.tindev.repositorys.DevRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/devs")
class LikeController(repository: DevRepository){



}