package com.papenko.rxtest.service

import com.papenko.rxtest.constant.Constants
import com.papenko.rxtest.dao.GameStateRepository
import com.papenko.rxtest.entity.GameState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GameService @Autowired constructor(
    val repo: GameStateRepository,
    val die: DieRollService
) {
    fun start(): GameState = repo.save(GameState(Constants.`game id`, Constants.`game started`))

    fun move(): GameState {
        val previousGameStateOptional = repo.findById(Constants.`game id`)
        if (!previousGameStateOptional.isPresent ||
            previousGameStateOptional.get().position == Constants.`game not started` ||
            previousGameStateOptional.get().position == Constants.`game finished`
        ) {
            throw IllegalStateException()
        }
        val newPosition = previousGameStateOptional.get().position + die.roll()
        return if (newPosition <= Constants.`game finished`) repo.save(GameState(Constants.`game id`, newPosition))
        else previousGameStateOptional.get()
    }

    fun finish(): GameState = repo.save(GameState(Constants.`game id`, Constants.`game not started`))
}
