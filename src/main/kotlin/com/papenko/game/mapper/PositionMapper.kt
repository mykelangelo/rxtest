package com.papenko.game.mapper

import com.papenko.game.dto.PositionDto
import com.papenko.game.entity.GameState
import org.springframework.stereotype.Component

@Component
object PositionMapper {
    fun toDto(gameState: GameState) = PositionDto(gameState.position)
}