package ru.DailyProblemBot.telegram.timer.task.edit;

import ru.DailyProblemBot.main.Main;
import ru.DailyProblemBot.models.Task;
import ru.DailyProblemBot.models.User;
import ru.DailyProblemBot.services.TaskService;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class EditTaskTimer extends TimerTask {

    static final Map<String, EditTask> editTasks = new ConcurrentHashMap<>();

    private static final Timer timer = new Timer(true);

    public static void load() {
        timer.scheduleAtFixedRate(new EditTaskTimer(), 0L, Main.getSettings().EDIT_TASK_TIMER_PERIOD);
    }

    @Override
    public void run() {

        for (Map.Entry<String, EditTask> entry : EditTaskTimer.editTasks.entrySet()) {

            try {

                if (entry.getValue().isCanceled()) {

                    EditTaskTimer.editTasks.remove(entry.getKey());

                } else if (entry.getValue().isCompleted()) {

                    Task task = entry.getValue().getFinalTask();
                    task.clearEditor();
                    TaskService.updateTask(task);
                    EditTaskTimer.editTasks.remove(entry.getKey());

                }

            } catch (Exception e) {
                System.err.println("Не удалось произвести сохранение заявки из-за ошибки обращения к БД.");
                e.printStackTrace();
            }

        }
    }

    public static EditTask getEditTask(String chatId) {
        return editTasks.get(chatId);
    }

    public static void createEditTask(String chatId, User editor, Task task) {
        EditTask editTask = new EditTask(task, editor);
        editTasks.put(chatId, editTask);
    }
}
