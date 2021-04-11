package ru.DailyProblemBot.services;

import ru.DailyProblemBot.dao.TaskDao;
import ru.DailyProblemBot.models.Task;
import ru.DailyProblemBot.models.User;

import java.util.List;
import java.util.stream.Collectors;

public class TaskService {

    private static final TaskDao tasksDao = new TaskDao();

    public static int countTasksByOwnerId(int ownerId) {
        return tasksDao.countTasksByOwnerId(ownerId);
    }

    public static Task findTask(int id) {
        return tasksDao.findById(id);
    }

    public static void saveTask(Task task) {
        tasksDao.save(task);
    }

    public static void deleteTask(Task task) {
        tasksDao.delete(task);
    }

    public static void updateTask(Task task) {
        tasksDao.update(task);
    }

    public static List<Task> findAllTasks() {
        return tasksDao.findAll();
    }

    public static List<Task> findAFew(int count, int lastId, boolean moveLeftToRight) {
        return tasksDao.findAFew(count, lastId, moveLeftToRight);
    }

    public static List<Task> findFirst(int count) {
        return tasksDao.findFirst(count);
    }

    public static List<Task> findEnd(int count) {
        return tasksDao.findEnd(count);
    }

    public static List<Task> findAFewByPerms(User user, int count, int lastId, boolean moveLeftToRight) {
        List<Task> tasks = tasksDao.findAFewByPerms(user, count, lastId, moveLeftToRight);

        // TODO лучше сделать это на SQL, ибо при получении заявок, где count > 1 может произойти фильтрация и дойдёт,
        //  напр, 1 заявка, вместо ожидаемых count
        // Фильтрация по наличию голоса за эти заявку
        tasks = tasks.stream().filter(task -> task.getVotes().stream()
                .noneMatch(vote -> user.getId() == vote.getUser().getId())
        ).collect(Collectors.toList());

        return tasks;
    }

    public static List<Task> findFirstByPerms(User user, int count) {
        List<Task> tasks = tasksDao.findFirstByPerms(user, count);

        // Фильтрация по наличию голоса за эти заявку
        tasks = tasks.stream().filter(task -> task.getVotes().stream()
                .noneMatch(vote -> user.getId() == vote.getUser().getId())
        ).collect(Collectors.toList());

        return tasks;
    }

    public static List<Task> findEndByPerms(User user, int count) {
        List<Task> tasks = tasksDao.findEndByPerms(user, count);

        // Фильтрация по наличию голоса за эти заявку
        tasks = tasks.stream().filter(task -> task.getVotes().stream()
                .noneMatch(vote -> user.getId() == vote.getUser().getId())
        ).collect(Collectors.toList());

        return tasks;
    }


    public static List<Task> findAFewForMe(User user, int count, int lastId, boolean moveLeftToRight) {
        return tasksDao.findAFewForMe(user, count, lastId, moveLeftToRight);
    }

    public static List<Task> findFirstForMe(User user, int count) {
        return tasksDao.findFirstForMe(user, count);
    }

    public static List<Task> findEndForMe(User user, int count) {
        return tasksDao.findEndForMe(user, count);
    }


    public static List<Task> findAFewForEdit(int count, int lastId, boolean moveLeftToRight) {
        return tasksDao.findAFewForEdit(count, lastId, moveLeftToRight);
    }

    public static List<Task> findFirstForEdit(int count) {
        return tasksDao.findFirstForEdit(count);
    }

    public static List<Task> findEndForEdit(int count) {
        return tasksDao.findEndForEdit(count);
    }


    public static List<Task> findAFewMeEditor(User user, int count, int lastId, boolean moveLeftToRight) {
        return tasksDao.findAFewMeEditor(user, count, lastId, moveLeftToRight);
    }

    public static List<Task> findFirstMeEditor(User user, int count) {
        return tasksDao.findFirstMeEditor(user, count);
    }

    public static List<Task> findEndMeEditor(User user, int count) {
        return tasksDao.findEndMeEditor(user, count);
    }
}
