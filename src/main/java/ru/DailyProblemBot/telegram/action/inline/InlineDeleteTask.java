package ru.DailyProblemBot.telegram.action.inline;

import ru.DailyProblemBot.models.Task;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.DailyProblemBot.services.TaskService;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.telegram.action.InlineAction;
import ru.DailyProblemBot.util.config.Messages;

public class InlineDeleteTask extends InlineAction {

    public InlineDeleteTask(String actionName) {
        super(actionName);
    }

    @Override
    public void handle(String chatId, CallbackQuery callbackQuery, UserProperties properties) {

        Task task = getTaskFromCallbackQuery(callbackQuery);

        // Владелец заявки
        if (task.getOwnerId() == properties.getUser().getId() && properties.getRole().hasPermission("task.remove.self")) {

            TaskService.deleteTask(task);

            Messages.tasks_inlineAnswer_deleteTask.sendAnswer(callbackQuery);

            ActionEngine.getAction(properties.getPhase()).alert(chatId, properties);
        } else if (properties.getRole().hasPermission("task.remove.other")) {
            TaskService.deleteTask(task);

            Messages.tasks_inlineAnswer_deleteTask.sendAnswer(callbackQuery);

            ActionEngine.getAction(properties.getPhase()).alert(chatId, properties);
        } else {

            Messages.tasks_inlineAnswer_deleteTaskPerm.sendAnswer(callbackQuery);

        }


    }
}
