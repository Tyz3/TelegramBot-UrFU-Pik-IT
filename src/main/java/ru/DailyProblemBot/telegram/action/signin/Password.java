package ru.DailyProblemBot.telegram.action.signin;

import ru.DailyProblemBot.enums.Keyboard;
import ru.DailyProblemBot.enums.UserPhase;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.DailyProblemBot.telegram.MessageControl;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.Action;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.util.config.Messages;

public class Password extends Action {

    public Password(UserPhase phase) {
        super(phase);
    }

    @Override
    public void alert(String chatId, UserProperties properties) {
        properties.setPhase(phase);
        Messages.signin_password.send(chatId);
    }

    @Override
    public void handle(String chatId, Message message, UserProperties properties) {
        properties.setEnteredPassword(message.getText());

        MessageControl.deleteAllMessages(chatId);

        if (properties.tryLogin()) {
            ActionEngine.getAction(UserPhase.MAIN).alert(chatId, properties);
        } else {
            Messages.signin_errorSignInData.send(chatId, Keyboard.AUTH);
            ActionEngine.getAction(UserPhase.AUTH).alert(chatId, properties);
        }
    }
}
