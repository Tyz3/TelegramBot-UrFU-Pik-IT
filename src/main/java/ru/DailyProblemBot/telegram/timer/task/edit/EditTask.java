package ru.DailyProblemBot.telegram.timer.task.edit;

import ru.DailyProblemBot.enums.TaskStatus;
import ru.DailyProblemBot.models.Task;
import ru.DailyProblemBot.models.User;
import ru.DailyProblemBot.services.TaskService;

import java.util.Date;

public class EditTask {

    private final Task task;
    private final Task cloneTask = new Task();
    private boolean completed = false;
    private boolean canceled = false;

    EditTask(Task task, User user) {
        this.task = task;
        this.task.setEditor(user);
        TaskService.updateTask(this.task);

        cloneTask.setId(task.getId());
        cloneTask.setStatus(task.getStatus());
        cloneTask.setVoteCountLimit(task.getVoteCountLimit());
        cloneTask.setCost(task.getCost());
        cloneTask.setAddress(task.getAddress());
        cloneTask.setDescription(task.getDescription());
        cloneTask.setTitle(task.getTitle());
        cloneTask.setOwnerId(task.getOwnerId());
    }

    public Task getTask() {
        return cloneTask;
    }

    public Task getFinalTask() {
        return task;
    }

    public void complete() {
        task.setStatus(cloneTask.getStatus());
        task.setVoteCountLimit(cloneTask.getVoteCountLimit());
        task.setCost(cloneTask.getCost());
        task.setAddress(cloneTask.getAddress());
        task.setDescription(cloneTask.getDescription());
        task.setTitle(cloneTask.getTitle());
        if (task.getStatus() == TaskStatus.DONE) {
            task.setDoneAt(new Date());
        }
        completed = true;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void cancel() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
