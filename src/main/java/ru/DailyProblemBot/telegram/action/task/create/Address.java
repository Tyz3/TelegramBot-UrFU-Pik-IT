package ru.DailyProblemBot.telegram.action.task.create;

import ru.DailyProblemBot.enums.Keyboard;
import ru.DailyProblemBot.enums.UserPhase;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.DailyProblemBot.telegram.MessageControl;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.Action;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.telegram.timer.task.add.NewTask;
import ru.DailyProblemBot.telegram.timer.task.add.NewTaskTimer;
import ru.DailyProblemBot.util.config.Messages;

public class Address extends Action {

    public Address(UserPhase phase) {
        super(phase);
    }

    @Override
    public void alert(String chatId, UserProperties properties) {
        properties.setPhase(phase);
        Messages.tasks_create_address.send(chatId, Keyboard.TASK_CREATE_ADDRESS);
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
                case "Пропустить":
                    newTask.getTask().setAddress("Неуказано");
                    ActionEngine.getAction(UserPhase.TASK_CREATE_CONFIRM).alert(chatId, properties);
                    break;
                default:
                    newTask.getTask().setAddress(MessageControl.replaceSpecialEntities(text));
                    ActionEngine.getAction(UserPhase.TASK_CREATE_CONFIRM).alert(chatId, properties);
            }



        }


    }
}
