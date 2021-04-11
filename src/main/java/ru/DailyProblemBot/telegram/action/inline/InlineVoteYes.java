package ru.DailyProblemBot.telegram.action.inline;

import ru.DailyProblemBot.models.Task;
import ru.DailyProblemBot.models.Vote;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.DailyProblemBot.services.TaskService;
import ru.DailyProblemBot.services.VoteService;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.telegram.action.InlineAction;
import ru.DailyProblemBot.util.config.Messages;

public class InlineVoteYes extends InlineAction {

    public InlineVoteYes(String actionName) {
        super(actionName);
    }

    @Override
    public void handle(String chatId, CallbackQuery callbackQuery, UserProperties properties) {

        Task task = getTaskFromCallbackQuery(callbackQuery);

        // Проверить наличие голоса за эту заявку
        if (VoteService.hasVoteFromUser(task, properties.getUser())) {
            Messages.tasks_vote_alreadyVoted.sendAnswer(callbackQuery);
            return;
        }

        // Создать голос
        Vote vote = new Vote(properties.getUser(), task, true);

        // Сохранить голос
        VoteService.saveVote(vote);

        // Добавить голос в заявку
        task.addVote(vote);
        task.incVoteCountYes();

        // Сохранить заявку
        TaskService.updateTask(task);

        // Отправить alert пользователю аля ваш голос засчитан как За
        Messages.tasks_inlineAnswer_vote
                .replace("{approved}", "За")
                .sendAnswer(callbackQuery);

        ActionEngine.getAction(properties.getPhase()).alert(chatId, properties);
    }
}
