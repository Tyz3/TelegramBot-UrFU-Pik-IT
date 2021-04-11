package ru.DailyProblemBot.telegram.action.signin;

import ru.DailyProblemBot.enums.UserPhase;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.Action;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.util.config.Messages;

public class Login extends Action {

    public Login(UserPhase phase) {
        super(phase);
    }

    @Override
    public void alert(String chatId, UserProperties properties) {
        properties.setPhase(phase);
        Messages.signin_login.send(chatId);
    }

    @Override
    public void handle(String chatId, Message message, UserProperties properties) {
        String text = message.getText()
                .replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("`", "\\`")
                .replace("~", "\\~");

        properties.setEnteredLogin(text);

        ActionEngine.getAction(UserPhase.SIGNIN_PASSWORD).alert(chatId, properties);
    }
}
