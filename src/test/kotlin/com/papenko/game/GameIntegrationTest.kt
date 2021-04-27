package com.papenko.game

import com.papenko.game.constant.Constants
import com.papenko.game.dao.GameStateRepository
import com.papenko.game.entity.GameState
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
import org.springframework.test.web.servlet.put

@SpringBootTest
@AutoConfigureMockMvc
internal class GameIntegrationTest @Autowired constructor(
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
                content {
                    contentType(APPLICATION_JSON)
                    jsonPath("['die faces']", `is`(Constants.`die faces`))
                    jsonPath("['game started']", `is`(Constants.`game started`))
                    jsonPath("['game finished']", `is`(Constants.`game finished`))
                    jsonPath("['game not started']", `is`(Constants.`game not started`))
                }
            }
    }

    @Test
    fun `given the game is not started should get one when game is started`() {
        repo.save(GameState(Constants.`game id`, Constants.`game not started`))
        mockMvc.put("/start")
            .andExpect {
                status { isOk() }
                content {
                    contentType(APPLICATION_JSON)
                    json("{\"position\": 1}")
                }
            }
    }

    @Test
    fun `given the game is in progress should get less than one when game is finished`() {
        repo.save(GameState(Constants.`game id`, Constants.`game started`))
        mockMvc.put("/finish")
            .andExpect {
                status { isOk() }
                content {
                    contentType(APPLICATION_JSON)
                    jsonPath("position", lessThan(1))
                }
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
        mockMvc.put("/move")
            .andExpect {
                status { isOk() }
                content {
                    contentType(APPLICATION_JSON)
                    jsonPath("position", upToMaxDieFace(startingPosition))
                    jsonPath("die", upToMaxDieFace(0))
                }
            }
    }

    private fun upToMaxDieFace(startingPosition: Int) = both(greaterThan(startingPosition))
        .and(lessThanOrEqualTo(startingPosition + Constants.`die faces`))

    @Test
    fun `given the game is not started should not move when move is attempted`() {
        `should not allow the move and display error message`(Constants.`game not started`)
    }

    @Test
    fun `given the game is finished should not move when move is attempted`() {
        `should not allow the move and display error message`(Constants.`game finished`)
    }

    private fun `should not allow the move and display error message`(position: Int) {
        repo.save(GameState(Constants.`game id`, position))
        mockMvc.put("/move")
            .andExpect {
                status { is4xxClientError() }
                content { string(`is`(not(emptyOrNullString()))) }
            }
    }
}
