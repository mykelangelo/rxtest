package com.papenko.game.dao

import com.papenko.game.entity.GameState
import org.springframework.data.repository.CrudRepository

interface GameStateRepository : CrudRepository<GameState, String> {}
