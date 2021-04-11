package ru.DailyProblemBot.telegram.action.task.showmy;

import ru.DailyProblemBot.enums.InlineKeyboard;
import ru.DailyProblemBot.enums.Keyboard;
import ru.DailyProblemBot.enums.TaskStatus;
import ru.DailyProblemBot.enums.UserPhase;
import ru.DailyProblemBot.main.Main;
import ru.DailyProblemBot.models.Task;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.DailyProblemBot.services.TaskService;
import ru.DailyProblemBot.telegram.MessageControl;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.Action;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.util.config.Messages;

import java.util.List;

public class ShowMy extends Action {

    public ShowMy(UserPhase phase) {
        super(phase);
    }

    @Override
    public void alert(String chatId, UserProperties properties) {
        properties.setPhase(phase);
        handle(chatId, null, properties);
    }

    @Override
    public void handle(String chatId, Message message, UserProperties properties) {

        MessageControl.deleteAllMessages(chatId);

        if (message == null) {
            showTasks(chatId, properties, true);
            return;
        }

        String text = message.getText();

        if (text.equals("След. страница")) {
            showTasks(chatId, properties, true);
        } else if (text.equals("Пред. страница")) {
            showTasks(chatId, properties, false);
        } else if (text.equals("В главное меню")) {
            properties.setLastShowTaskId(-1);
            ActionEngine.getAction(UserPhase.MAIN).alert(chatId, properties);
        }
    }

    private static void showTasks(String chatId, UserProperties properties, boolean moveLeftToRight) {

        int count = Main.getSettings().SHOWMY_TASKS_AT_ONCE;

        List<Task> tasks = TaskService.findAFewForMe(properties.getUser(), count, properties.getLastShowTaskId(), moveLeftToRight);

        // Если заявок на голосование совсем нет
        if (tasks.size() == 0) {

            if (moveLeftToRight) {
                tasks = TaskService.findFirstForMe(properties.getUser(), count);
            } else {
                tasks = TaskService.findEndForMe(properties.getUser(), count);
            }

        }

        sendTask(chatId, properties, tasks);
    }

    private static void sendTask(String chatId, UserProperties properties, List<Task> tasks) {

        if (tasks.size() == 0) {
            Messages.tasks_showMy_nothingToShow.send(chatId);
            ActionEngine.getAction(UserPhase.MAIN).alert(chatId, properties);
            return;
        }

        // Запоминаем id последней выведенной заявки, чтобы потом не выводить тоже самое
        properties.setLastShowTaskId(tasks.get(tasks.size() - 1).getId());

        Messages.tasks_showMy_intro.send(chatId, Keyboard.TASK_SHOWMY);

        for (Task task : tasks) {

            float countYes = task.getVoteCountYes();
            float countNo = task.getVoteCountNo();
            int sharePlus = countYes + countNo == 0 ? 0 : (int) (countYes / (countYes + countNo) * 100F);
            int shareMinus = countYes + countNo == 0 ? 0 : 100 - sharePlus;

            TaskStatus status = task.getStatus();

            Messages.tasks_showMy_taskFormat
                    .replace("{taskId}", task.getId())
                    .replace("{status}", status.getDisplayName())
                    .replace("{title}", task.getTitle())
                    .replace("{description}", task.getDescription())
                    .replace("{cost}", task.getCost())
                    .replace("{filesAmount}", task.getFiles().size())
                    .replace("{address}", task.getAddress())
                    .replace("{createAt}", Main.getSettings().makeDateByFormat(task.getCreateAt()))
                    .replace("{doneAt}", Main.getSettings().makeDateByFormat(task.getDoneAt()))
                    .replace("{votesNow}", (int)(countNo + countYes))
                    .replace("{votesLimit}", task.getVoteCountLimit())
                    .replace("_", "\\_")
                    .replace("*", "\\*")
                    .replace("[", "\\[")
                    .replace("`", "\\`")
                    .replace("~", "\\~")
                    .sendPhoto(chatId, task.getFirstPhotoFile(),
                            status.ordinal() == 0 ?
                            InlineKeyboard.TASK_SHOWMY_1  // С кнопкой Удалить - только если заявка Новая
                                    .replace("{share+}", sharePlus)
                                    .replace("{share-}", shareMinus)
                                    .replace("{taskId}", task.getId()) :
                            InlineKeyboard.TASK_SHOWMY_2  // Без кнопки Удалить - все остальные заявки
                                    .replace("{share+}", sharePlus)
                                    .replace("{share-}", shareMinus)
                                    .replace("{taskId}", task.getId())
                    );

        }


    }
}
