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

public class ChangeVotesLimit extends Action {

    public ChangeVotesLimit(UserPhase phase) {
        super(phase);
    }

    @Override
    public void alert(String chatId, UserProperties properties) {
        properties.setPhase(phase);
        Task task = EditTaskTimer.getEditTask(chatId).getTask();
        Messages.tasks_edit_votesLimit.replace("{votesLimit}", task.getVoteCountLimit()).send(chatId, Keyboard.TASK_EDIT_VOTES_LIMIT);
    }

    @Override
    public void handle(String chatId, Message message, UserProperties properties) {

        Task task = EditTaskTimer.getEditTask(chatId).getTask();

        if (message.hasText()) {

            switch (message.getText()) {
                case "Пропустить":
                    ActionEngine.getAction(UserPhase.TASK_EDIT_COST).alert(chatId, properties);
                    break;
                case "Пропустить всё":
                    ActionEngine.getAction(UserPhase.TASK_EDIT_CONFIRM).alert(chatId, properties);
                    break;
                default:
                    String text = message.getText();

                    try {

                        int votesLimit = Integer.parseInt(text);
                        task.setVoteCountLimit(votesLimit);
                        ActionEngine.getAction(UserPhase.TASK_EDIT_COST).alert(chatId, properties);

                    } catch (NumberFormatException e) {
                        MessageControl.deleteLastMessage(chatId);
                        Messages.tasks_edit_votesPattern.send(chatId);
                    }

                    break;
            }

        }

    }
}
