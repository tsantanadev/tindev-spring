package br.com.thiagosousa.tindev.models

import br.com.thiagosousa.tindev.controllers.dtos.DevForm
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Dev(
        @Id
        var _id: String? = null,
        val name: String,
        val user: String,
        val bio: String? = null,
        val avatar: String,
        val likes: MutableList<String> = mutableListOf(),
        val dislikes: MutableList<String> = mutableListOf(),
        val languages: MutableMap<String, Long> = hashMapOf()
){
        constructor(devForm: DevForm, languages: MutableMap<String, Long>) : this(
                name = devForm.name,
                user = devForm.login,
                avatar = devForm.avatar_url,
                bio = devForm.bio,
                languages = languages

        )
}