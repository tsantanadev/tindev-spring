package br.com.thiagosousa.tindev.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Repository(
        val id: Long,
        val languages_url: String,
        val fork: Boolean,
        val language: String?,
        val name: String
)