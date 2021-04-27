package com.papenko.game.service

import com.papenko.game.constant.Constants
import com.papenko.game.dao.GameStateRepository
import com.papenko.game.entity.GameState
import com.papenko.game.mapper.MoveMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@ExtendWith(MockitoExtension::class)
internal class GameServiceTest @Autowired constructor(
    val repo: GameStateRepository,
    val moveMapper: MoveMapper
) {
    @Test
    fun `given die can only roll two and position is previous to the last should not move when attempted to`() {
        val die = Mockito.mock(DieRollService::class.java)
        val service = GameService(repo, die, moveMapper)
        given(die.roll()).willReturn(2)
        val previousToTheLastPosition = Constants.`game finished` - 1
        repo.save(GameState(Constants.`game id`, previousToTheLastPosition))

        service.move()

        val findById = repo.findById(Constants.`game id`)
        assertTrue(findById.isPresent)
        assertEquals(previousToTheLastPosition, findById.get().position)
    }
}
