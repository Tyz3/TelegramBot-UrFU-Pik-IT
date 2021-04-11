package ru.DailyProblemBot.telegram.action;

import ru.DailyProblemBot.models.Task;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.DailyProblemBot.services.TaskService;
import ru.DailyProblemBot.telegram.UserProperties;

public abstract class InlineAction {

    protected final String actionName;

    public InlineAction(String actionName) {
        this.actionName = actionName;

        ActionEngine.putInlineAction(actionName, this);
    }

    public abstract void handle(String chatId, CallbackQuery callbackQuery, UserProperties properties);

    protected Task getTaskFromCallbackQuery(CallbackQuery callbackQuery) {
        // "{taskId}:yes"
        // "{taskId}:no"
        String taskId = callbackQuery.getData().split(":")[0];
        return TaskService.findTask(Integer.parseInt(taskId));
    }
}
