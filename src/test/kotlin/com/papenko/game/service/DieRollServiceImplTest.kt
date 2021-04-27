package com.papenko.game.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.anyInt
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

/**
 * At least two tests are needed to verify that the value is not a hard-coded one.
 * PS: [this service][com.papenko.game.service.DieRollService] may be deleted at this point,
 * but if we will ever decide do add one or a few more dice to the game, we will need it.
 * In other words, this service provides us with more flexibility in the future.
 */
@ExtendWith(MockitoExtension::class)
internal class DieRollServiceImplTest {
    @Mock
    lateinit var random: Random

    @InjectMocks
    lateinit var service: DieRollServiceImpl

    @Test
    fun `given random is mocked should roll four when random provides three`() {
        `should roll the same number (plus one) as provided by random function`(3)
    }

    @Test
    fun `given random is mocked should roll two when random provides one`() {
        `should roll the same number (plus one) as provided by random function`(1)
    }

    /**
     * It should not return the same number, because [Random.nextInt(int)][java.util.Random.nextInt]
     * provides values from zero inclusive, which is never desired in the game
     * @return Random.nextInt(int) + 1
     */
    private fun `should roll the same number (plus one) as provided by random function`(randInt: Int) {
        given(random.nextInt(anyInt())).willReturn(randInt)

        val roll = service.roll()

        assertEquals(randInt + 1, roll)
    }
}