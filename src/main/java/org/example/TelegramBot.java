package org.example;

import org.example.service.GetStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private GetStats getStats;

    @Value("${bot_name}")
    private String botName;

    public TelegramBot(@Value("${bot_token}") String botToken) {
        super(botToken);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            String currentMessage = message.getText();
            if (currentMessage.equals("/start")) {
                SendMessage answer = new SendMessage(chatId, "Hello, I'm a bot for finding statistics in Dota 2. Enter your Steam ID.");
                try {
                    execute(answer);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if (currentMessage.matches("\\d+") && currentMessage.length() == 17) {
                getStats.getId(currentMessage);
                SendMessage answer = new SendMessage(chatId, "The statistics are ready. Enter /stats to see it.");
                try {
                    execute(answer);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if (currentMessage.equals("/stats")) {
                SendMessage answer = new SendMessage(chatId, getStats.statsToString());
                try {
                    execute(answer);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}