package ru.DailyProblemBot.telegram.action.task.edit;

import ru.DailyProblemBot.enums.InlineKeyboard;
import ru.DailyProblemBot.enums.Keyboard;
import ru.DailyProblemBot.enums.TaskStatus;
import ru.DailyProblemBot.enums.UserPhase;
import ru.DailyProblemBot.main.Main;
import ru.DailyProblemBot.models.Task;
import ru.DailyProblemBot.models.User;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.DailyProblemBot.services.UserService;
import ru.DailyProblemBot.telegram.MessageControl;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.Action;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.telegram.timer.task.edit.EditTaskTimer;
import ru.DailyProblemBot.util.config.Messages;

public class ChangeStatus extends Action {

    public ChangeStatus(UserPhase phase) {
        super(phase);
    }

    @Override
    public void alert(String chatId, UserProperties properties) {
        properties.setPhase(phase);

        MessageControl.deleteAllMessages(chatId);

        Task task = EditTaskTimer.getEditTask(chatId).getTask();

        User user = UserService.findUser(task.getOwnerId());

        float countYes = task.getVoteCountYes();
        float countNo = task.getVoteCountNo();

        Messages.tasks_edit_taskFormat
                .replace("{taskId}", task.getId())
                .replace("{status}", task.getStatus().getDisplayName())
                .replace("{title}", task.getTitle())
                .replace("{description}", task.getDescription())
                .replace("{cost}", task.getCost())
                .replace("{filesAmount}", task.getFiles().size())
                .replace("{address}", task.getAddress())
                .replace("{createAt}", Main.getSettings().makeDateByFormat(task.getCreateAt()))
                .replace("{doneAt}", Main.getSettings().makeDateByFormat(task.getDoneAt()))
                .replace("{votesNow}", (int)(countNo + countYes))
                .replace("{votesLimit}", task.getVoteCountLimit())
                .replace("{ownerName}", user.getName())
                .replace("{ownerEmail}", user.getEmail())
                .sendPhoto(chatId, task.getFirstPhotoFile(),
                        InlineKeyboard.TASK_EDIT
                                .replace("{taskId}", task.getId())

                );

        Messages.tasks_edit_status.replace("{status}", task.getStatus().getDisplayName()).send(chatId, Keyboard.TASK_EDIT_STATUS);
    }

    @Override
    public void handle(String chatId, Message message, UserProperties properties) {

        Task task = EditTaskTimer.getEditTask(chatId).getTask();

        if (message.hasText()) {

            switch (message.getText()) {
                case "Пропустить":
                    ActionEngine.getAction(UserPhase.TASK_EDIT_VOTES_LIMIT).alert(chatId, properties);
                    break;
                case "Пропустить всё":
                    ActionEngine.getAction(UserPhase.TASK_EDIT_CONFIRM).alert(chatId, properties);
                    break;
                case "Голосование (студенты)":
                    task.setStatus(TaskStatus.VOTING_STUDENTS);
                    ActionEngine.getAction(UserPhase.TASK_EDIT_VOTES_LIMIT).alert(chatId, properties);
                    break;
                case "Модерация":
                    task.setStatus(TaskStatus.MODERATING);
                    ActionEngine.getAction(UserPhase.TASK_EDIT_VOTES_LIMIT).alert(chatId, properties);
                    break;
                case "Голосование (эксперты)":
                    task.setStatus(TaskStatus.VOTING_EXPERTS);
                    ActionEngine.getAction(UserPhase.TASK_EDIT_VOTES_LIMIT).alert(chatId, properties);
                    break;
                case "Неактивна":
                    task.setStatus(TaskStatus.NON_ACTIVE);
                    ActionEngine.getAction(UserPhase.TASK_EDIT_VOTES_LIMIT).alert(chatId, properties);
                    break;
                case "Новая":
                    task.setStatus(TaskStatus.NEW);
                    ActionEngine.getAction(UserPhase.TASK_EDIT_VOTES_LIMIT).alert(chatId, properties);
                    break;
                case "Осуществление работ":
                    task.setStatus(TaskStatus.IN_PROCESS);
                    ActionEngine.getAction(UserPhase.TASK_EDIT_VOTES_LIMIT).alert(chatId, properties);
                    break;
                case "Выполнена":
                    task.setStatus(TaskStatus.DONE);
                    ActionEngine.getAction(UserPhase.TASK_EDIT_VOTES_LIMIT).alert(chatId, properties);
                    break;
            }

        }

    }
}
