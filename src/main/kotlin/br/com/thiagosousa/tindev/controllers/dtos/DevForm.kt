package br.com.thiagosousa.tindev.controllers.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class DevForm(
        val name: String,
        val login: String,
        val bio: String? = "",
        val avatar_url: String
)