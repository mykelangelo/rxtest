package com.papenko.rxtest.controller

import com.papenko.rxtest.constant.Constants
import com.papenko.rxtest.dto.MoveDto
import com.papenko.rxtest.dto.PositionDto
import com.papenko.rxtest.mapper.PositionMapper
import com.papenko.rxtest.service.GameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController @Autowired constructor(
    val service: GameService,
    val positionMapper: PositionMapper
) {
    @GetMapping("const")
    fun const(): Constants = Constants

    @GetMapping("start")
    fun start(): PositionDto = positionMapper.toDto(service.start())

    @GetMapping("move")
    fun move(): MoveDto = service.move()

    @GetMapping("finish")
    fun finish(): PositionDto = positionMapper.toDto(service.finish())

    @ExceptionHandler(IllegalStateException::class)
    fun handle(): ResponseEntity<String> = ResponseEntity.badRequest().body("use /start first")
}
