package ru.DailyProblemBot.telegram.action.inline;

import ru.DailyProblemBot.models.Task;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.InlineAction;
import ru.DailyProblemBot.util.config.Messages;

public class InlineSeeVotesYes extends InlineAction {

    public InlineSeeVotesYes(String actionName) {
        super(actionName);
    }

    @Override
    public void handle(String chatId, CallbackQuery callbackQuery, UserProperties properties) {
        Task task = getTaskFromCallbackQuery(callbackQuery);
        Messages.tasks_inlineAnswer_seeVotesYes
                .replace("{voteCountYes}", task.getVoteCountYes())
                .sendAnswer(callbackQuery);
    }
}
