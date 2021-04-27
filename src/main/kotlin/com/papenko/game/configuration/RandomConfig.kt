package com.papenko.game.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.SecureRandom

@Configuration
class RandomConfig {
    @Bean
    fun random() = SecureRandom()
}