package br.com.thiagosousa.tindev.services

import br.com.thiagosousa.tindev.models.Dev
import br.com.thiagosousa.tindev.repositorys.DevRepository
import org.springframework.stereotype.Service

@Service
class DevService(
        val repository: DevRepository,
        val gitHubService: GitHubService
) {


    fun listAll(user: String): List<Dev>{
        val loggedDev = repository.findById(user).get()

        val devList = repository.findAll()

        devList.removeAll {
            loggedDev._id.equals(it._id) || loggedDev.likes.contains(it._id) || loggedDev.dislikes.contains(it._id)
        }

        return devList
    }

    fun insert(username: String): Dev {
        val registeredDev = repository.findByUser(username)

        if (registeredDev.isPresent)
            return registeredDev.get()

        return repository.save(gitHubService.getUser(username))
    }

}
