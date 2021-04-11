package ru.DailyProblemBot.telegram.action.task.showall;

import ru.DailyProblemBot.enums.InlineKeyboard;
import ru.DailyProblemBot.enums.Keyboard;
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

public class ShowAll extends Action {

    public ShowAll(UserPhase phase) {
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

        // Если нужно отобразить заявки слева-направо и полученных заявок 0, то
        // создаём запрос на получение первых 3-х заявок: SELECT * FROM 'tasks' ORDER BY id ASC LIMIT 3,
        // если снова 0, то пишем nothingToShow
        // Если нужно идти справа-налево и полученных заявок 0, то
        // создаём запрос на получение последних 3-х заявок: SELECT * FROM 'tasks' ORDER BY id DESC LIMIT 3,
        // если снова 0, то пишем nothingToShow

        if (message == null) {
            // Отображаются первые 3 записи
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

        int count = Main.getSettings().SHOWALL_TASKS_AT_ONCE;

        List<Task> tasks = TaskService.findAFew(count, properties.getLastShowTaskId(), moveLeftToRight);

        // Если заявок на голосование совсем нет
        if (tasks.size() == 0) {

            if (moveLeftToRight) {
                tasks = TaskService.findFirst(count);
            } else {
                tasks = TaskService.findEnd(count);
            }

        }

        sendTask(chatId, properties, tasks);
    }

    private static void sendTask(String chatId, UserProperties properties, List<Task> tasks) {

        if (tasks.size() == 0) {
            Messages.tasks_showAll_nothingToShow.send(chatId);
            ActionEngine.getAction(UserPhase.MAIN).alert(chatId, properties);
            return;
        }

        // Запоминаем id последней выведенной заявки, чтобы потом не выводить тоже самое
        properties.setLastShowTaskId(tasks.get(tasks.size() - 1).getId());

        Messages.tasks_showAll_intro.send(chatId, Keyboard.TASK_SHOWALL);

        for (Task task : tasks) {

            float countYes = task.getVoteCountYes();
            float countNo = task.getVoteCountNo();
            int sharePlus = countYes + countNo == 0 ? 0 : (int) (countYes / (countYes + countNo) * 100F);
            int shareMinus = countYes + countNo == 0 ? 0 : 100 - sharePlus;

            Messages.tasks_showAll_taskFormat
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
                    .replace("_", "\\_")
                    .replace("*", "\\*")
                    .replace("[", "\\[")
                    .replace("`", "\\`")
                    .replace("~", "\\~")
                    .sendPhoto(chatId, task.getFirstPhotoFile(),
                            InlineKeyboard.TASK_SHOWALL
                                    .replace("{share+}", sharePlus)
                                    .replace("{share-}", shareMinus)
                                    .replace("{taskId}", task.getId())

                    );

        }


    }
}
