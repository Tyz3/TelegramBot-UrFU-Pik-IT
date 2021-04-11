package ru.DailyProblemBot.util.config;

import ru.DailyProblemBot.util.JSONConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class InitializedSettings {

    private final Pattern namePattern;
    private final Pattern emailPattern;
    private final Pattern passwordPattern;
    private final Pattern studentIdPattern;

    private final SimpleDateFormat sdf;

    public final int DESCRIPTION_MIN_LENGTH;
    public final int DESCRIPTION_MAX_LENGTH;
    public final int TITLE_MIN_LENGTH;
    public final int TITLE_AUTO_MAX_LENGTH;
    public final int LOADED_FILE_MAX_SIZE;
    public final long AFK_MINUTES_THRESHOLD;

    public final long NEW_TASK_TIMER_PERIOD;
    public final long SESSION_TIMER_PERIOD;
    public final long EDIT_TASK_TIMER_PERIOD;

    public final int SHOW_VOTE_TASKS_AT_ONCE;
    public final int SHOWALL_TASKS_AT_ONCE;
    public final int SHOWMY_TASKS_AT_ONCE;
    public final int SHOWEDIT_TASKS_AT_ONCE;
    public final int SHOWEDITING_TASKS_AT_ONCE;

    public final List<String> LOADED_FILES_WHITELIST;

    public InitializedSettings(JSONConfig cfg) {

        namePattern = Pattern.compile(Config.patterns_name.get());
        emailPattern = Pattern.compile(Config.patterns_email.get());
        passwordPattern = Pattern.compile(Config.patterns_password.get());
        studentIdPattern = Pattern.compile(Config.patterns_studentId.get());
        sdf = new SimpleDateFormat(Config.simpleDateFormat.get());

        DESCRIPTION_MIN_LENGTH = cfg.getInt("patterns.descriptionMinLength", 20);
        DESCRIPTION_MAX_LENGTH = cfg.getInt("patterns.descriptionMaxLength", 500);
        TITLE_MIN_LENGTH = cfg.getInt("patterns.titleMinLength", 3);
        TITLE_AUTO_MAX_LENGTH = cfg.getInt("patterns.titleAutoMaxLength", 16);

        NEW_TASK_TIMER_PERIOD = cfg.getLong("timers.newTaskTimerPeriodMillis", 10000L);
        SESSION_TIMER_PERIOD = cfg.getLong("timers.sessionTimerPeriodMillis", 5L*60L*1000L);
        AFK_MINUTES_THRESHOLD = cfg.getLong("timers.afkThresholdMinutes", 70L);
        EDIT_TASK_TIMER_PERIOD = cfg.getLong("timers.editTaskTimerPeriodMillis", 15L*1000L);

        SHOW_VOTE_TASKS_AT_ONCE = cfg.getInt("showVoteTasksAtOnce", 1);
        SHOWALL_TASKS_AT_ONCE = cfg.getInt("showAllTasksAtOnce", 2);
        SHOWMY_TASKS_AT_ONCE = cfg.getInt("showMyTasksAtOnce", 2);
        SHOWEDIT_TASKS_AT_ONCE = cfg.getInt("showEditTasksAtOnce", 2);
        SHOWEDITING_TASKS_AT_ONCE = cfg.getInt("showEditingTasksAtOnce", 2);
        LOADED_FILE_MAX_SIZE = cfg.getInt("loadedFileMaxSizeBytes", 4096);

        LOADED_FILES_WHITELIST = cfg.getStringList("loadedFilesWhitelist");
    }

    public boolean checkPasswordByPattern(String text) {
        return passwordPattern.matcher(text).matches();
    }

    public boolean checkEmailByPattern(String text) {
        return emailPattern.matcher(text).matches();
    }

    public boolean checkNameByPattern(String text) {
        return namePattern.matcher(text).matches();
    }

    public boolean checkStudentIdByPattern(String text) {
        return studentIdPattern.matcher(text).matches();
    }

    public String makeDateByFormat(Date date) {
        return date != null ? sdf.format(date) : "";
    }

}
