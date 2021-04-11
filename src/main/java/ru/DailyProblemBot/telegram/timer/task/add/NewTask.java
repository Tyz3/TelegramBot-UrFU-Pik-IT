package ru.DailyProblemBot.telegram.timer.task.add;

import ru.DailyProblemBot.enums.TaskStatus;
import ru.DailyProblemBot.models.Task;
import ru.DailyProblemBot.models.User;

public class NewTask {

    private final Task task = new Task();
    private boolean completed = false;
    private boolean deleted = false;

    NewTask(User user) {
        task.setOwnerId(user.getId());
        task.setStatus(TaskStatus.NEW);
    }

    public Task getTask() {
        return task;
    }

    public void complete() {
        completed = true;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void delete() {
        deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
