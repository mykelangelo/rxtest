package com.papenko.game.mapper

import com.papenko.game.dto.MoveDto
import com.papenko.game.entity.GameState
import org.springframework.stereotype.Component

@Component
object MoveMapper {
    fun toDto(gameState: GameState, dieRoll: Int) = MoveDto(gameState.position, dieRoll)
}