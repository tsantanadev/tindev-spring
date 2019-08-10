package br.com.thiagosousa.tindev.services

import br.com.thiagosousa.tindev.controllers.dtos.DevDTO
import br.com.thiagosousa.tindev.models.Dev
import br.com.thiagosousa.tindev.repositorys.DevRepository
import org.springframework.stereotype.Service
import kotlin.math.roundToInt

@Service
class DevService(
        val repository: DevRepository,
        val gitHubService: GitHubService
) {


    fun listAll(user: String): List<DevDTO>{
        val loggedDev = repository.findById(user).get()

        val devList = repository.findAll()

        devList.removeAll {
            loggedDev._id.equals(it._id) || loggedDev.likes.contains(it._id) || loggedDev.dislikes.contains(it._id)
        }

        val devDtoList = mutableListOf<DevDTO>()

        devList.forEach {dev ->
            val devDTO = DevDTO(dev)
            devDTO.matchin = getScoreMatching(dev, loggedDev)
            devDtoList.add(devDTO)
        }

        return devDtoList.sortedBy(DevDTO::matchin).reversed()
    }

    private fun getScoreMatching(dev: Dev, loggedDev: Dev): Int {
        var matchingScore = 0

        dev.languages.forEach { (lang, _) ->
            if (loggedDev.languages.containsKey(lang)) matchingScore++
        }

        return (matchingScore * 100f / loggedDev.languages.size).toDouble().roundToInt()
    }

    fun insert(username: String): Dev {
        val registeredDev = repository.findByUser(username)

        if (registeredDev.isPresent)
            return registeredDev.get()

        return repository.save(gitHubService.getUser(username))
    }

}
