package br.com.thiagosousa.tindev.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Language(
        @Id
        val id: String? = null,
        val name: String
)