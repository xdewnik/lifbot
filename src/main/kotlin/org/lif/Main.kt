package org.lif

import org.lif.bots.LostInFutureBot
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.logging.BotLogger
import java.util.logging.ConsoleHandler
import java.util.logging.Level


    private val TAG = "MAIN"

    var savedTime: String? = null

    fun main() {
        BotLogger.setLevel(Level.ALL)
        BotLogger.registerLogger(ConsoleHandler())

        ApiContextInitializer.init()

        initBot()


    }

    private fun initBot() {
        val botsApi = TelegramBotsApi()

        // Register our bot
        try {
            botsApi.registerBot(LostInFutureBot());
        } catch (e: TelegramApiException) {
            BotLogger.error(TAG, e)
        }
    }

