package com.papenko.rxtest.mapper

import com.papenko.rxtest.dto.MoveDto
import com.papenko.rxtest.entity.GameState
import org.springframework.stereotype.Component

@Component
object MoveMapper {
    fun toDto(gameState: GameState, dieRoll: Int) = MoveDto(gameState.position, dieRoll)
}