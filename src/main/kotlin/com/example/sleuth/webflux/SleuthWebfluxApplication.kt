package com.example.sleuth.webflux

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import kotlin.random.Random

@SpringBootApplication
class SleuthWebfluxApplication

fun main(args: Array<String>) {
    runApplication<SleuthWebfluxApplication>(*args)
}

@RestController
@RequestMapping("api")
class ApiController(val service: ToStringService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping
    suspend fun get() {
        logger.info("Starting number conversion")

        service.execute().subscribe {
            logger.info("Received $it converted from ToStringService")
        }

        logger.info("Number conversion finished")
    }

}

@Service
class ToStringService(val randomNumberService: RandomNumberService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun execute(): Mono<String> {

        logger.info("Requesting random number")

        return randomNumberService.execute().map { randomNumber ->
            logger.info("Converting random number: $randomNumber")
            "$randomNumber"
        }

    }

}

@Service
class RandomNumberService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun execute(): Mono<Int> {
        logger.info("Generating random number")
        val randomNumber = Random.nextInt()

        logger.info("Random number generated: $randomNumber")
        return Mono.just(randomNumber)

    }

}