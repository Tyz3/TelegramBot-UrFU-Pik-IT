package ru.DailyProblemBot.telegram.action.common;

import ru.DailyProblemBot.enums.Keyboard;
import ru.DailyProblemBot.enums.UserPhase;
import ru.DailyProblemBot.main.Main;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.DailyProblemBot.telegram.MessageControl;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.Action;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.util.config.Messages;

public class StartMenu extends Action {

    public StartMenu(UserPhase phase) {
        super(phase);
    }

    @Override
    public void alert(String chatId, UserProperties properties) {
        properties.setPhase(phase);

        MessageControl.deleteAllMessages(chatId);

        if (properties.isAFK(Main.getSettings().AFK_MINUTES_THRESHOLD)) {

            Messages.afkUnLogin.replace("{afkThreshold}", Main.getSettings().AFK_MINUTES_THRESHOLD).send(chatId, Keyboard.START);

        } else {

            Messages.unlogin.send(chatId, Keyboard.START);

        }
    }

    @Override
    public void handle(String chatId, Message message, UserProperties properties) {

        MessageControl.deleteAllMessages(chatId);


        // Отдельная отправка сообщения без записи в MessageHistory, нужно чтобы в мобильной
        // версии не удалялись все сообщение, иначе чат закрывается, и нужно заново входить.
        SendMessage msg = new SendMessage();
        msg.setReplyMarkup(null);
        msg.enableMarkdown(true);
        msg.setChatId(chatId);
        msg.setText(Messages.start.get());
        try {
            Main.getTelegramBot().execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        ActionEngine.getAction(UserPhase.AUTH).alert(chatId, properties);
    }
}
