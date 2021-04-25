package com.papenko.rxtest.dao

import com.papenko.rxtest.entity.GameState
import org.springframework.data.repository.CrudRepository

interface GameStateRepository : CrudRepository<GameState, String> {}
