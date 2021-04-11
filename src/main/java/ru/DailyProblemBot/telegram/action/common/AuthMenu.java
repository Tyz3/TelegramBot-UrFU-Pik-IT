package ru.DailyProblemBot.telegram.action.common;

import ru.DailyProblemBot.enums.Keyboard;
import ru.DailyProblemBot.enums.UserPhase;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.DailyProblemBot.telegram.MessageControl;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.Action;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.util.config.Messages;

public class AuthMenu extends Action {

    public AuthMenu(UserPhase phase) {
        super(phase);
    }

    @Override
    public void alert(String chatId, UserProperties properties) {
        properties.setPhase(phase);
        Messages.auth.send(chatId, Keyboard.AUTH);
    }

    @Override
    public void handle(String chatId, Message message, UserProperties properties) {

        if (!message.hasText()) {
            return;
        }

        switch (message.getText()) {
            case "Войти":
                MessageControl.deleteAllMessages(chatId);
                ActionEngine.getAction(UserPhase.SIGNIN_LOGIN).alert(chatId, properties);
                break;
            case "Регистрация":
                MessageControl.deleteAllMessages(chatId);
                ActionEngine.getAction(UserPhase.SIGNUP_NAME).alert(chatId, properties);
                break;
        }
    }
}
