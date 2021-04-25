package com.papenko.rxtest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RxtestApplication

fun main(args: Array<String>) {
    runApplication<RxtestApplication>(*args)
}
