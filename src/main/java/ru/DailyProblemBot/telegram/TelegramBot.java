package ru.DailyProblemBot.telegram;

import ru.DailyProblemBot.enums.UserPhase;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.util.config.Messages;

public class TelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;

    public TelegramBot(String botUsername, String botToken) {
        this.botUsername = botUsername;
        this.botToken = botToken;

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {

        // Обработка сообщений пользователя
        if (update.hasMessage() && (update.getMessage().hasText() || update.getMessage().hasPhoto() || update.getMessage().hasDocument())) { //  || update.getMessage().hasPhoto()

            Message message = update.getMessage();
            User from = message.getFrom();
            String chatId = message.getChatId().toString();

            // Создаём или получаем свойства пользователя
            UserProperties properties = UserManager.getOrCreateUserProperties(from.getId());

            try {

                // Функция бана не реализована должным образом
                if (properties.isOnline()) {
                    if (properties.getUser().hasBan()) {
                        Messages.banned.send(message.getChatId().toString());
                        return;
                    }

                }

                // Записываем id сообщения для последующего удаления
                MessageControl.addMessageToHistory(chatId, message.getMessageId());

                System.out.println("Log: Принято сообщение '".concat(message.hasText() ? message.getText() : "").concat("' от ").concat(from.getUserName() == null ? "" : from.getUserName()));

                // Обращаемся к методу ожидаемого действия
                ActionEngine.getAction(properties.getPhase()).handle(chatId, message, properties);

                // TODO добавить в handle return boolean, чтобы проверять валидность введённой команды и
                //  лишний раз не обращаться к БД при обновлении last_seen
                // Метка времени последнего действия.
                properties.updateLastSeen();

            } catch (Exception e) {
                e.printStackTrace();

                Messages.fatalError.send(chatId);

                if (properties.isOnline())
                    ActionEngine.getAction(UserPhase.MAIN).alert(chatId, properties);
                else
                    ActionEngine.getAction(UserPhase.START).alert(chatId, properties);
            }

        // Обработка нажатий на inline-кнопки у собщений пользователем
        } else if (update.hasCallbackQuery()) {

            CallbackQuery callbackQuery = update.getCallbackQuery();
            Message message = callbackQuery.getMessage();
            String chatId = message.getChatId().toString();

            // Создаём или получаем свойства пользователя
            UserProperties properties = UserManager.getOrCreateUserProperties(callbackQuery.getFrom().getId());

            try {

                // Обращаемся к методу для обработки нажатия на кнопку под сообщением
                ActionEngine.getInlineAction(callbackQuery.getData()).handle(chatId, callbackQuery, properties);

                properties.updateLastSeen();

            } catch (Exception e) {
                e.printStackTrace();
                Messages.fatalError.send(chatId);

                if (properties.isOnline())
                    ActionEngine.getAction(UserPhase.MAIN).alert(chatId, properties);
                else
                    ActionEngine.getAction(UserPhase.START).alert(chatId, properties);
            }

        }
    }

}
