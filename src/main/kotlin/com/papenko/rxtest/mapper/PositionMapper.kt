package com.papenko.rxtest.mapper

import com.papenko.rxtest.dto.PositionDto
import com.papenko.rxtest.entity.GameState
import org.springframework.stereotype.Component

@Component
object PositionMapper {
    fun toDto(gameState: GameState) = PositionDto(gameState.position)
}