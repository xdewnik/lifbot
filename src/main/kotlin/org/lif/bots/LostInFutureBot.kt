package org.lif.bots

import org.lif.commands.Commands
import org.lif.savedTime

import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.logging.BotLogger

class LostInFutureBot : TelegramLongPollingCommandBot("Michael") {

    private val TAG = "LiFBot"


    override fun processNonCommandUpdate(update: Update?) {
        if (update?.hasMessage()!! && update.message.hasText()) {

            var messageText: String? = null
            val chatId: String? = update.message.chatId.toString()

            if (isCommandForMe(update.message.text)) {
                when (update.message.text) {
                    Commands.reptime, Commands.reptimeGroup -> messageText =
                        executeRepTimeCommand(update.message.from, update.message.chat)
                    Commands.setreptime, Commands.setreptimeGroup -> messageText =
                        executeSetTimeCommand(update.message.from, update.message.chat)
                }
                sendMessage(messageText, chatId)
            } else if (update.message.text.split("\\s".toRegex(), 2)[0].toLowerCase() == Commands.time) {
                savedTime = update.message.text.toLowerCase().substringAfter(Commands.time)
                messageText = "новое время репетиции $savedTime"

                sendMessage(messageText, chatId)
            } //else if( update.message.text.toLowerCase().contains("артем")
//                ||  update.message.text.toLowerCase().contains("артём")){
//                sendMessage("ПИДОР " + Emoji.HEAVY_BLACK_HEART, chatId)
//            }
        }
    }

    private fun sendMessage(messageText: String?, chatId: String?) {
        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = messageText
        try {
            execute(message)
            BotLogger.debug(TAG, "${message.chatId}  ${message.text}")
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    private fun executeSetTimeCommand(user: User?, chat: Chat?): String? {
        var userName: String? = chat?.userName
        if (userName == null || userName.isEmpty()) {
            userName = user?.firstName + " " + user?.lastName
        }
        return StringBuilder(userName)
            .append(" Напишите 'время' и после время для репетиции").toString()

    }

    private fun executeRepTimeCommand(user: User, chat: Chat): String? {
        var userName: String? = chat.userName
        if (userName == null || userName.isEmpty()) {
            userName = user.firstName + " " + user.lastName
        }

        if (!savedTime.isNullOrEmpty()) {
            return StringBuilder(userName)
                .append(" время репы $savedTime").toString()
        } else {
            return StringBuilder(userName)
                .append(" ")
                .append("Ленивые пидарасы не запланировали репетиций :(").toString()
        }

    }

    private fun isCommandForMe(command: String): Boolean {
        return command == Commands.reptime
                || command == Commands.reptimeGroup
                || command == Commands.setreptime
                || command == Commands.setreptimeGroup
    }


    override fun getBotToken() = "673071455:AAFENAZa4PGIWZzFEfCPMLy5HROBOPGnoeM"


}

