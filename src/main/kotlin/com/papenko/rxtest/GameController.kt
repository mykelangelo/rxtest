package com.papenko.rxtest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController @Autowired constructor(val service: GameService) {
    @GetMapping("const")
    fun const(): Constants = Constants

    @GetMapping("start")
    fun start(): GameState = service.start()

    @GetMapping("move")
    fun move(): GameState = service.move()

    @GetMapping("finish")
    fun finish(): GameState = service.finish()

    @ExceptionHandler(value = [(IllegalStateException::class)])
    fun handleConflict(): ResponseEntity<Any> = ResponseEntity.badRequest().build()
}
