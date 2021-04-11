package ru.DailyProblemBot.telegram.action.signup;

import ru.DailyProblemBot.enums.UserPhase;
import ru.DailyProblemBot.main.Main;
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
        Messages.signup_password.send(chatId);
    }

    @Override
    public void handle(String chatId, Message message, UserProperties properties) {

        String text = message.getText();

        if (!Main.getSettings().checkPasswordByPattern(text)) {

            MessageControl.deleteLastMessage(chatId);
            Messages.signup_errorPattern.send(chatId);

        } else {

            properties.setEnteredPassword(text);

            MessageControl.deleteAllMessages(chatId);

            // Пробуем зарегистрировать, а потом авторизовать
            if (properties.tryRegister() && properties.tryLogin()) {
                Messages.signup_success.send(chatId);
                ActionEngine.getAction(UserPhase.MAIN).alert(chatId, properties);
            } else {
                ActionEngine.getAction(UserPhase.AUTH).alert(chatId, properties);
            }

        }

    }
}
