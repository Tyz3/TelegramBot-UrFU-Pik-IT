package ru.DailyProblemBot.telegram.action.task.edit;

import ru.DailyProblemBot.enums.Keyboard;
import ru.DailyProblemBot.enums.UserPhase;
import ru.DailyProblemBot.models.Task;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.DailyProblemBot.telegram.MessageControl;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.Action;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.telegram.timer.task.edit.EditTaskTimer;
import ru.DailyProblemBot.util.config.Messages;

public class ChangeCost extends Action {

    public ChangeCost(UserPhase phase) {
        super(phase);
    }

    @Override
    public void alert(String chatId, UserProperties properties) {
        properties.setPhase(phase);
        Task task = EditTaskTimer.getEditTask(chatId).getTask();
        Messages.tasks_edit_cost.replace("{cost}", task.getCost()).send(chatId, Keyboard.TASK_EDIT_COST);
    }

    @Override
    public void handle(String chatId, Message message, UserProperties properties) {

        Task task = EditTaskTimer.getEditTask(chatId).getTask();

        if (message.hasText()) {

            switch (message.getText()) {
                case "Пропустить":
                    ActionEngine.getAction(UserPhase.TASK_EDIT_ADDRESS).alert(chatId, properties);
                    break;
                case "Пропустить всё":
                    ActionEngine.getAction(UserPhase.TASK_EDIT_CONFIRM).alert(chatId, properties);
                    break;
                default:
                    String text = message.getText();
                    task.setCost(MessageControl.replaceSpecialEntities(text));
                    ActionEngine.getAction(UserPhase.TASK_EDIT_ADDRESS).alert(chatId, properties);
                    break;
            }

        }

    }
}
