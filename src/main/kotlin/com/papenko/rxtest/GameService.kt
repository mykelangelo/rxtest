package com.papenko.rxtest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.security.SecureRandom

@Service
class GameService @Autowired constructor(val repo: GameStateRepository) {
    private val random = SecureRandom()

    fun start(): GameState = repo.save(GameState(Constants.`game id`, Constants.`game started`))

    fun move(): GameState {
        val findById = repo.findById(Constants.`game id`)
        if (!findById.isPresent ||
            findById.get().position == Constants.`game not started` ||
            findById.get().position == Constants.`game finished`) {
            throw IllegalStateException()
        }
        return repo.save(GameState(Constants.`game id`, findById.get().position + roll()))
    }

    fun finish(): GameState = repo.save(GameState(Constants.`game id`, Constants.`game not started`))

    private fun roll(): Int = random.nextInt(Constants.`die faces`) + 1
}
