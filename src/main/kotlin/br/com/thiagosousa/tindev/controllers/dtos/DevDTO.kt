package br.com.thiagosousa.tindev.controllers.dtos

import br.com.thiagosousa.tindev.models.Dev

data class DevDTO(
        var _id: String? = null,
        val name: String,
        val user: String,
        val bio: String? = "",
        val avatar: String,
        var matchin: Int = 0
        ){
    constructor(dev: Dev) : this(
            dev._id,
            dev.name,
            dev.user,
            dev.bio,
            dev.avatar
    )
}
