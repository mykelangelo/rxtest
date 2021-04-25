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
        val findById = repo.findById(Constants.`game id`)
        if (!findById.isPresent ||
            findById.get().position == Constants.`game not started` ||
            findById.get().position == Constants.`game finished`
        ) {
            throw IllegalStateException()
        }
        return repo.save(GameState(Constants.`game id`, findById.get().position + die.roll()))
    }

    fun finish(): GameState = repo.save(GameState(Constants.`game id`, Constants.`game not started`))
}
