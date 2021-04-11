package ru.DailyProblemBot.telegram.timer;

import ru.DailyProblemBot.telegram.timer.session.SessionTimer;
import ru.DailyProblemBot.telegram.timer.task.add.NewTaskTimer;
import ru.DailyProblemBot.telegram.timer.task.edit.EditTaskTimer;

public class Timers {

    public static void load() {
        NewTaskTimer.load();
        SessionTimer.load();
        EditTaskTimer.load();
    }
}
