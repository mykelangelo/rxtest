package com.papenko.rxtest

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class GameIntegrationTest @Autowired constructor(
    var mockMvc: MockMvc,
    var repo: GameStateRepository
) {
    companion object {
        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            assertEquals(1, Constants.GAME_STARTED)
            assertThat(Constants.GAME_NOT_STARTED).isLessThan(Constants.GAME_STARTED)
            assertThat(Constants.GAME_STARTED).isLessThan(Constants.DIE_FACES)
            assertThat(Constants.DIE_FACES).isLessThan(Constants.GAME_FINISHED)
        }
    }

    @Test
    fun `given any moment should get all constants when requested`() {
        mockMvc.get("/const")
            .andExpect {
                status { isOk() }
                content { contentType(APPLICATION_JSON) }
                content { jsonPath("DIE_FACES", `is`(Constants.DIE_FACES)) }
                content { jsonPath("GAME_STARTED", `is`(Constants.GAME_STARTED)) }
                content { jsonPath("GAME_FINISHED", `is`(Constants.GAME_FINISHED)) }
                content { jsonPath("GAME_NOT_STARTED", `is`(Constants.GAME_NOT_STARTED)) }
            }
    }

    @Test
    fun `given the game is not started should get one when game is started`() {
        repo.save(GameState(Constants.GAME_ID, Constants.GAME_NOT_STARTED))
        mockMvc.get("/start")
            .andExpect {
                status { isOk() }
                content { contentType(APPLICATION_JSON) }
                content { json("{\"position\": 1}") }
            }
    }

    @Test
    fun `given the game is in progress should get less than one when game is finished`() {
        repo.save(GameState(Constants.GAME_ID, Constants.GAME_STARTED))
        mockMvc.get("/finish")
            .andExpect {
                status { isOk() }
                content { contentType(APPLICATION_JSON) }
                content { jsonPath("position", lessThan(1)) }
            }
    }

    @Test
    fun `given the games is started should move according to die roll when move is made`() {
        `should move ahead`(Constants.GAME_STARTED)
    }

    @Test
    fun `given the game is in progress should move according to die roll when move is made`() {
        `should move ahead`(Constants.GAME_STARTED + (Constants.DIE_FACES / 2))
    }

    private fun `should move ahead`(startingPosition: Int) {
        repo.save(GameState(Constants.GAME_ID, startingPosition))
        mockMvc.get("/move")
            .andExpect {
                status { isOk() }
                content { contentType(APPLICATION_JSON) }
                content {
                    jsonPath(
                        "position",
                        both(greaterThan(startingPosition))
                            .and(lessThanOrEqualTo(startingPosition + Constants.DIE_FACES))
                    )
                }
            }
    }

    @Test
    fun `given the game is not started should not move when move is attempted`() {
        `should not allow the move`(Constants.GAME_NOT_STARTED)
    }

    @Test
    fun `given the game is finished should not move when move is attempted`() {
        `should not allow the move`(Constants.GAME_FINISHED)
    }

    private fun `should not allow the move`(position: Int) {
        repo.save(GameState(Constants.GAME_ID, position))
        mockMvc.get("/move")
            .andExpect {
                status { is4xxClientError() }
            }
    }
}
