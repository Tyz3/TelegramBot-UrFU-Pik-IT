package ru.DailyProblemBot.telegram.action.inline;

import ru.DailyProblemBot.models.Task;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.InlineAction;
import ru.DailyProblemBot.util.Sender;
import ru.DailyProblemBot.util.config.Messages;

public class InlineDownloadFiles extends InlineAction {

    public InlineDownloadFiles(String actionName) {
        super(actionName);
    }

    @Override
    public void handle(String chatId, CallbackQuery callbackQuery, UserProperties properties) {

        Task task = getTaskFromCallbackQuery(callbackQuery);

        for (String fileEntry : task.getFiles()) {

            Messages.tasks_inlineAnswer_downloadFiles.sendAnswer(callbackQuery);

            if (fileEntry.startsWith("img")) {

                new Sender().sendPhoto(chatId, fileEntry);

            } else if (fileEntry.startsWith("doc")) {

                new Sender().sendDocument(chatId, fileEntry);

            }

        }
    }
}
