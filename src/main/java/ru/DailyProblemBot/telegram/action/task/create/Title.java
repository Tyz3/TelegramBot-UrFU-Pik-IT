package ru.DailyProblemBot.telegram.action.task.create;

import ru.DailyProblemBot.enums.Keyboard;
import ru.DailyProblemBot.enums.UserPhase;
import ru.DailyProblemBot.main.Main;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.DailyProblemBot.telegram.MessageControl;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.Action;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.telegram.timer.task.add.NewTask;
import ru.DailyProblemBot.telegram.timer.task.add.NewTaskTimer;
import ru.DailyProblemBot.util.config.Messages;

public class Title extends Action {

    public Title(UserPhase phase) {
        super(phase);
    }

    @Override
    public void alert(String chatId, UserProperties properties) {
        properties.setPhase(phase);
        Messages.tasks_create_title.send(chatId, Keyboard.TASK_CREATE_TITLE);
    }

    @Override
    public void handle(String chatId, Message message, UserProperties properties) {


        if (message.hasText()) {
            String text = message.getText();
            NewTask newTask = NewTaskTimer.getNewTask(chatId);

            switch (text) {

                case "Удалить заявку":
                    MessageControl.deleteAllMessages(chatId);
                    newTask.delete();
                    ActionEngine.getAction(UserPhase.MAIN).alert(chatId, properties);
                    break;
                case "Авто-название":
                    String desc = newTask.getTask().getDescription();
                    newTask.getTask().setTitle(desc.substring(0, Math.min(desc.length(), Main.getSettings().TITLE_AUTO_MAX_LENGTH)).concat("..."));
                    ActionEngine.getAction(UserPhase.TASK_CREATE_COST).alert(chatId, properties);
                    break;

                default:

                    if (text.length() < Main.getSettings().TITLE_MIN_LENGTH) {

                        MessageControl.deleteLastMessage(chatId);
                        Messages.tasks_create_titlePattern.send(chatId);

                    } else {

                        newTask.getTask().setTitle(MessageControl.replaceSpecialEntities(text));
                        ActionEngine.getAction(UserPhase.TASK_CREATE_COST).alert(chatId, properties);

                    }
            }
        }

    }
}
