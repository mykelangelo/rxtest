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
    val mockMvc: MockMvc,
    val repo: GameStateRepository
) {
    companion object {
        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            assertEquals(1, Constants.`game started`)
            assertThat(Constants.`game not started`).isLessThan(Constants.`game started`)
            assertThat(Constants.`game started`).isLessThan(Constants.`die faces`)
            assertThat(Constants.`die faces`).isLessThan(Constants.`game finished`)
        }
    }

    @Test
    fun `given any moment should get all constants when requested`() {
        mockMvc.get("/const")
            .andExpect {
                status { isOk() }
                content { contentType(APPLICATION_JSON) }
                content { jsonPath("['die faces']", `is`(Constants.`die faces`)) }
                content { jsonPath("['game started']", `is`(Constants.`game started`)) }
                content { jsonPath("['game finished']", `is`(Constants.`game finished`)) }
                content { jsonPath("['game not started']", `is`(Constants.`game not started`)) }
            }
    }

    @Test
    fun `given the game is not started should get one when game is started`() {
        repo.save(GameState(Constants.`game id`, Constants.`game not started`))
        mockMvc.get("/start")
            .andExpect {
                status { isOk() }
                content { contentType(APPLICATION_JSON) }
                content { json("{\"position\": 1}") }
            }
    }

    @Test
    fun `given the game is in progress should get less than one when game is finished`() {
        repo.save(GameState(Constants.`game id`, Constants.`game started`))
        mockMvc.get("/finish")
            .andExpect {
                status { isOk() }
                content { contentType(APPLICATION_JSON) }
                content { jsonPath("position", lessThan(1)) }
            }
    }

    @Test
    fun `given the games is started should move according to die roll when move is made`() {
        `should move ahead`(Constants.`game started`)
    }

    @Test
    fun `given the game is in progress should move according to die roll when move is made`() {
        `should move ahead`(Constants.`game started` + (Constants.`die faces` / 2))
    }

    private fun `should move ahead`(startingPosition: Int) {
        repo.save(GameState(Constants.`game id`, startingPosition))
        mockMvc.get("/move")
            .andExpect {
                status { isOk() }
                content { contentType(APPLICATION_JSON) }
                content {
                    jsonPath(
                        "position",
                        both(greaterThan(startingPosition))
                            .and(lessThanOrEqualTo(startingPosition + Constants.`die faces`))
                    )
                }
            }
    }

    @Test
    fun `given the game is not started should not move when move is attempted`() {
        `should not allow the move`(Constants.`game not started`)
    }

    @Test
    fun `given the game is finished should not move when move is attempted`() {
        `should not allow the move`(Constants.`game finished`)
    }

    private fun `should not allow the move`(position: Int) {
        repo.save(GameState(Constants.`game id`, position))
        mockMvc.get("/move")
            .andExpect {
                status { is4xxClientError() }
            }
    }
}
