package ru.DailyProblemBot.telegram.timer.task.add;

import ru.DailyProblemBot.main.Main;
import ru.DailyProblemBot.models.Task;
import ru.DailyProblemBot.models.User;
import ru.DailyProblemBot.services.TaskService;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class NewTaskTimer extends TimerTask {

    static final Map<String, NewTask> newTasks = new ConcurrentHashMap<>();

    private static final Timer timer = new Timer(true);

    public static void load() {
        timer.scheduleAtFixedRate(new NewTaskTimer(), 0L, Main.getSettings().NEW_TASK_TIMER_PERIOD);
    }

    @Override
    public void run() {

        for (Map.Entry<String, NewTask> entry : NewTaskTimer.newTasks.entrySet()) {

            try {

                if (entry.getValue().isDeleted()) {

                    NewTaskTimer.newTasks.remove(entry.getKey());

                } else if (entry.getValue().isCompleted()) {

                    Task task = entry.getValue().getTask();
                    TaskService.saveTask(task);
                    NewTaskTimer.newTasks.remove(entry.getKey());

                }

            } catch (Exception e) {
                System.err.println("Не удалось произвести сохранение заявки из-за ошибки обращения к БД.");
                e.printStackTrace();
            }

        }
    }

    public static NewTask getNewTask(String chatId) {
        return newTasks.get(chatId);
    }

    public static NewTask createAndGetNewTask(String chatId, User user) {
        NewTask newTask = new NewTask(user);
        newTasks.put(chatId, newTask);
        return newTask;
    }
}
