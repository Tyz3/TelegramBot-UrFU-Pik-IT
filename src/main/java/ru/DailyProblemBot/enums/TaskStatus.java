package ru.DailyProblemBot.enums;

import ru.DailyProblemBot.util.JSONConfig;

public enum TaskStatus {

    NEW("Новая"),
    MODERATING("Модерация"),
    VOTING_STUDENTS("Голосование (студенческий состав)"),
    VOTING_EXPERTS("Голосование (экспертный состав)"),
    IN_PROCESS("Осуществление работ"),
    DONE("Выполнена"),
    NON_ACTIVE("Неактивна");

    private String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public static void load(JSONConfig config) {

        for (TaskStatus status : values()) {
            status.displayName = config.getString("taskStatus.".concat(status.name().toLowerCase()).concat(".displayName"),
                    status.displayName);
        }

    }

    public static TaskStatus matchByOrdinal(int ordinal) {

        return ordinal > values().length ? NEW : values()[ordinal];
    }

//    public static TaskStatus match(String name) {
//
//        for (TaskStatus status : values()) {
//            if (status.name().equalsIgnoreCase(name)) {
//                return status;
//            }
//        }
//
//        return TaskStatus.NEW;
//    }

    public String getDisplayName() {
        return displayName;
    }
}
