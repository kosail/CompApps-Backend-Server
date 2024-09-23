package com.korealm.compApp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class CompAppApplication

fun main(args: Array<String>) {
	runApplication<CompAppApplication>(*args)
}
