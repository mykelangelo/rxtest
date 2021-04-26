package com.papenko.rxtest.service

import com.papenko.rxtest.constant.Constants
import com.papenko.rxtest.dao.GameStateRepository
import com.papenko.rxtest.dto.MoveDto
import com.papenko.rxtest.entity.GameState
import com.papenko.rxtest.mapper.MoveMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GameService @Autowired constructor(
    val repo: GameStateRepository,
    val die: DieRollService,
    val moveMapper: MoveMapper
) {
    fun start(): GameState = repo.save(GameState(Constants.`game id`, Constants.`game started`))

    fun move(): MoveDto {
        val previousGameStateOptional = repo.findById(Constants.`game id`)
        if (!previousGameStateOptional.isPresent ||
            previousGameStateOptional.get().position == Constants.`game not started` ||
            previousGameStateOptional.get().position == Constants.`game finished`
        ) {
            throw IllegalStateException()
        }
        val roll = die.roll()
        val newPosition = previousGameStateOptional.get().position + roll
        return if (newPosition <= Constants.`game finished`) moveMapper.toDto(repo.save(GameState(Constants.`game id`, newPosition)), roll)
        else moveMapper.toDto(previousGameStateOptional.get(), roll)
    }

    fun finish(): GameState = repo.save(GameState(Constants.`game id`, Constants.`game not started`))
}
