package ru.DailyProblemBot.telegram.action.task.create;

import ru.DailyProblemBot.enums.Keyboard;
import ru.DailyProblemBot.enums.UserPhase;
import ru.DailyProblemBot.models.Task;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.DailyProblemBot.telegram.MessageControl;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.Action;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.telegram.timer.task.add.NewTask;
import ru.DailyProblemBot.telegram.timer.task.add.NewTaskTimer;
import ru.DailyProblemBot.util.config.Messages;

public class Confirm extends Action {

    public Confirm(UserPhase phase) {
        super(phase);
    }

    @Override
    public void alert(String chatId, UserProperties properties) {
        properties.setPhase(phase);
        showSummary(chatId);
        Messages.tasks_create_confirm.send(chatId, Keyboard.TASK_CREATE_CONFIRM);
    }

    @Override
    public void handle(String chatId, Message message, UserProperties properties) {


        if (message.hasText()) {

            String text = message.getText();
            NewTask newTask = NewTaskTimer.getNewTask(chatId);

            MessageControl.deleteAllMessages(chatId);

            switch (text) {

                case "Нет, удаляем":
                    newTask.delete();
                    Messages.tasks_create_cancel.send(chatId);
                    ActionEngine.getAction(UserPhase.MAIN).alert(chatId, properties);
                    break;
                case "Подтвердить":
                    newTask.complete();
                    Messages.tasks_create_created.send(chatId);
                    ActionEngine.getAction(UserPhase.MAIN).alert(chatId, properties);
                    break;
            }

        }

    }

    private void showSummary(String chatId) {

        Task task = NewTaskTimer.getNewTask(chatId).getTask();

        // Попробуем найти фото в приложениях
        String photo = task.getFirstPhotoFile();

        Messages.tasks_create_summary
                .replace("{title}", task.getTitle())
                .replace("{description}", task.getDescription())
                .replace("{cost}", task.getCost())
                .replace("{filesAmount}", task.getFiles().size())
                .replace("{address}", task.getAddress())
                .replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("`", "\\`")
                .replace("~", "\\~")
                // Если photo = null, то произойдёт переброс на send()
                .sendPhoto(chatId, photo);
    }
}
