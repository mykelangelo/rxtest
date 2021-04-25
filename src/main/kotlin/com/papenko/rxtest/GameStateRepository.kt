package com.papenko.rxtest

import org.springframework.data.repository.CrudRepository

interface GameStateRepository : CrudRepository<GameState, String> {}
