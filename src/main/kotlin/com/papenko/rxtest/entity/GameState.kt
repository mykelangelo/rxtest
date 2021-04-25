package com.papenko.rxtest.entity

import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed

@RedisHash("game")
data class GameState(@Indexed val id: String,
                     var position: Int) {
}
