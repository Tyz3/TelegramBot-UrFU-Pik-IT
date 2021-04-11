package ru.DailyProblemBot.telegram.action.task.editing;

import ru.DailyProblemBot.enums.InlineKeyboard;
import ru.DailyProblemBot.enums.Keyboard;
import ru.DailyProblemBot.enums.UserPhase;
import ru.DailyProblemBot.main.Main;
import ru.DailyProblemBot.models.Task;
import ru.DailyProblemBot.models.User;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.DailyProblemBot.services.TaskService;
import ru.DailyProblemBot.services.UserService;
import ru.DailyProblemBot.telegram.MessageControl;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.Action;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.util.config.Messages;

import java.util.List;

public class EditingMenu extends Action {

    public EditingMenu(UserPhase phase) {
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
            // Отображаются первые 3 записи
            showTasks(chatId, properties, true);
            return;
        }

        String text = message.getText();

        if (text.equals("След. заявка")) {
            showTasks(chatId, properties, true);
        } else if (text.equals("Пред. заявка")) {
            showTasks(chatId, properties, false);
        } else if (text.equals("В главное меню")) {
            properties.setLastShowTaskId(-1);
            ActionEngine.getAction(UserPhase.MAIN).alert(chatId, properties);
        }
    }

    private static void showTasks(String chatId, UserProperties properties, boolean moveLeftToRight) {

        int count = Main.getSettings().SHOWEDITING_TASKS_AT_ONCE;

        List<Task> tasks = TaskService.findAFewMeEditor(properties.getUser(), count, properties.getLastShowTaskId(), moveLeftToRight);

        // Если заявок на голосование совсем нет
        if (tasks.size() == 0) {

            if (moveLeftToRight) {
                tasks = TaskService.findFirstMeEditor(properties.getUser(), count);
            } else {
                tasks = TaskService.findEndMeEditor(properties.getUser(), count);
            }

        }

        sendTask(chatId, properties, tasks);
    }

    private static void sendTask(String chatId, UserProperties properties, List<Task> tasks) {

        if (tasks.size() == 0) {
            Messages.tasks_editing_nothingToShow.send(chatId);
            ActionEngine.getAction(UserPhase.MAIN).alert(chatId, properties);
            return;
        }

        // Запоминаем id последней выведенной заявки, чтобы потом не выводить тоже самое
        properties.setLastShowTaskId(tasks.get(tasks.size() - 1).getId());

        Messages.tasks_editing_intro.send(chatId, Keyboard.TASK_EDITING);

        for (Task task : tasks) {

            float countYes = task.getVoteCountYes();
            float countNo = task.getVoteCountNo();

            User user = UserService.findUser(task.getOwnerId());

            Messages.tasks_editing_taskFormat
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
                    .replace("{ownerName}", MessageControl.replaceSpecialEntities(user.getName()))
                    .replace("{ownerEmail}", MessageControl.replaceSpecialEntities(user.getEmail()))
                    .sendPhoto(chatId, task.getFirstPhotoFile(),
                            InlineKeyboard.TASK_EDITING
                                    .replace("{taskId}", task.getId())

                    );

        }

    }
}
