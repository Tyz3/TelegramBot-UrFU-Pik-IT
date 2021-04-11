package ru.DailyProblemBot.telegram.action.task.create;

import ru.DailyProblemBot.enums.Keyboard;
import ru.DailyProblemBot.enums.UserPhase;
import ru.DailyProblemBot.main.Main;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.DailyProblemBot.telegram.MessageControl;
import ru.DailyProblemBot.telegram.timer.task.add.NewTaskTimer;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.Action;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.util.config.Messages;

public class Description extends Action {

    public Description(UserPhase phase) {
        super(phase);
    }

    @Override
    public void alert(String chatId, UserProperties properties) {
        properties.setPhase(phase);

        MessageControl.deleteAllMessages(chatId);

        Messages.tasks_create_common.send(chatId);
        Messages.tasks_create_description.send(chatId, Keyboard.TASK_CREATE_DESCRIPTION);
    }

    @Override
    public void handle(String chatId, Message message, UserProperties properties) {


        if (message.hasText()) {

            String text = message.getText();

            switch (text) {

                case "Удалить заявку":
                    MessageControl.deleteAllMessages(chatId);
                    ActionEngine.getAction(UserPhase.MAIN).alert(chatId, properties);

                    break;
                default:
                    if (text.length() < Main.getSettings().DESCRIPTION_MIN_LENGTH || text.length() > Main.getSettings().DESCRIPTION_MAX_LENGTH) {

//                        MessageControl.deleteLastMessage(chatId);
                        Messages.tasks_create_descriptionPattern.send(chatId);

                    } else {
                        text = MessageControl.replaceSpecialEntities(text);

                        // Создаём новую заявку и ставим описание
                        NewTaskTimer.createAndGetNewTask(chatId, properties.getUser()).getTask().setDescription(text);

                        ActionEngine.getAction(UserPhase.TASK_CREATE_TITLE).alert(chatId, properties);

                    }

            }

        }





    }
}
