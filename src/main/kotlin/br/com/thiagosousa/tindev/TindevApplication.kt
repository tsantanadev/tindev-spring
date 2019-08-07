package br.com.thiagosousa.tindev

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TindevApplication

fun main(args: Array<String>) {
	runApplication<TindevApplication>(*args)
}
