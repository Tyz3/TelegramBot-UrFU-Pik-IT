package ru.DailyProblemBot.telegram.action.inline;

import ru.DailyProblemBot.enums.UserPhase;
import ru.DailyProblemBot.models.Task;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.telegram.action.InlineAction;
import ru.DailyProblemBot.telegram.timer.task.edit.EditTaskTimer;
import ru.DailyProblemBot.util.config.Messages;

public class InlineEditTask extends InlineAction {

    public InlineEditTask(String actionName) {
        super(actionName);
    }

    @Override
    public void handle(String chatId, CallbackQuery callbackQuery, UserProperties properties) {

        Task task = getTaskFromCallbackQuery(callbackQuery);

        if (task.getOwnerId() == properties.getUser().getId() && properties.getRole().hasPermission("task.edit.self")) {

            // TODO доступно только moder и root, а должно быть и обычному юзеру

            // Указываем того, кто редактирует заявку, чтобы не выдавать её другим
            task.setEditor(properties.getUser());

            EditTaskTimer.createEditTask(chatId, properties.getUser(), task);

            Messages.tasks_inlineAnswer_editTask.sendAnswer(callbackQuery, true);
            ActionEngine.getAction(UserPhase.TASK_EDIT_STATUS).alert(chatId, properties);

        } else if (properties.getRole().hasPermission("task.edit.other")) {

            // Указываем того, кто редактирует заявку, чтобы не выдавать её другим
            task.setEditor(properties.getUser());

            EditTaskTimer.createEditTask(chatId, properties.getUser(), task);

            Messages.tasks_inlineAnswer_editTask.sendAnswer(callbackQuery, true);
            ActionEngine.getAction(UserPhase.TASK_EDIT_STATUS).alert(chatId, properties);

        } else {

            Messages.tasks_inlineAnswer_editTaskPerm.sendAnswer(callbackQuery);

        }
    }
}
