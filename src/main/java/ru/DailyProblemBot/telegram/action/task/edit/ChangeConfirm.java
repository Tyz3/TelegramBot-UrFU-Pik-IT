package ru.DailyProblemBot.telegram.action.task.edit;

import ru.DailyProblemBot.enums.Keyboard;
import ru.DailyProblemBot.enums.TaskStatus;
import ru.DailyProblemBot.enums.UserPhase;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.DailyProblemBot.telegram.MessageControl;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.Action;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.telegram.timer.task.edit.EditTask;
import ru.DailyProblemBot.telegram.timer.task.edit.EditTaskTimer;
import ru.DailyProblemBot.util.config.Messages;

public class ChangeConfirm extends Action {

    public ChangeConfirm(UserPhase phase) {
        super(phase);
    }

    @Override
    public void alert(String chatId, UserProperties properties) {
        properties.setPhase(phase);
        Messages.tasks_edit_confirm.send(chatId, Keyboard.TASK_EDIT_CONFIRM);
    }

    @Override
    public void handle(String chatId, Message message, UserProperties properties) {

        MessageControl.deleteAllMessages(chatId);

        EditTask task = EditTaskTimer.getEditTask(chatId);

        if (message.hasText()) {

            switch (message.getText()) {
                case "Отменить":
                    Messages.tasks_edit_changesCancel.send(chatId);
                    task.cancel();
                    ActionEngine.getAction(UserPhase.MAIN).alert(chatId, properties);
                    break;
                case "Применить":
                    Messages.tasks_edit_changesApply.send(chatId);
                    task.complete();
                    ActionEngine.getAction(UserPhase.MAIN).alert(chatId, properties);
                    break;
            }
        }

    }
}
