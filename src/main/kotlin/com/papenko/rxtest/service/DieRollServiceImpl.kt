package com.papenko.rxtest.service

import com.papenko.rxtest.constant.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class DieRollServiceImpl @Autowired constructor(
    private val random: Random
): DieRollService {
    override fun roll(): Int = random.nextInt(Constants.`die faces`) + 1
}