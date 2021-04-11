package ru.DailyProblemBot.telegram.action.task.create;

import ru.DailyProblemBot.enums.Keyboard;
import ru.DailyProblemBot.enums.UserPhase;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.DailyProblemBot.telegram.MessageControl;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.Action;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.telegram.timer.task.add.NewTask;
import ru.DailyProblemBot.telegram.timer.task.add.NewTaskTimer;
import ru.DailyProblemBot.util.Downloader;
import ru.DailyProblemBot.util.config.Messages;

public class Files extends Action {

    public Files(UserPhase phase) {
        super(phase);
    }

    @Override
    public void alert(String chatId, UserProperties properties) {
        properties.setPhase(phase);
        Messages.tasks_create_files.send(chatId, Keyboard.TASK_CREATE_FILES);
    }

    @Override
    public void handle(String chatId, Message message, UserProperties properties) {

        if (message.hasText()) {

            String text = message.getText();

            switch (text) {

                case "Удалить заявку":
                    MessageControl.deleteAllMessages(chatId);
                    NewTask newTask = NewTaskTimer.getNewTask(chatId);
                    newTask.delete();
                    ActionEngine.getAction(UserPhase.MAIN).alert(chatId, properties);
                    break;
                case "Пропустить/Продолжить":
                    ActionEngine.getAction(UserPhase.TASK_CREATE_ADDRESS).alert(chatId, properties);
                    break;
            }

        } else {
            NewTask newTask = NewTaskTimer.getNewTask(chatId);

            String fileEntry = "";
            if (message.hasPhoto()) {

                fileEntry = Downloader.makeDownloadableEntryFromPhoto(message.getPhoto());

            } else if (message.hasDocument()) {

                fileEntry = Downloader.makeDownloadableEntryFromDocument(message.getDocument());

            }

            if (fileEntry.equals("")) {
                MessageControl.deleteLastMessage(chatId);
                Messages.tasks_create_fileTypeOrSizeNotAllowed.send(chatId);
                return;
            }

            Messages.tasks_create_fileAdded.send(chatId);

            newTask.getTask().addFile(fileEntry);
        }


    }
}
