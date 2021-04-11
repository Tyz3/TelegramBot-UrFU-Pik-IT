package ru.DailyProblemBot.enums;

import ru.DailyProblemBot.util.JSONConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum UserRole {

    USER("Пользователь", "task.create",
            "task.remove.self",
            "task.vote.student",
            "task.see.self",
            "task.see.all"),
    EXPERT("Эксперт", "task.vote.expert",
            "task.see.all"),
    MODER("Модератор", "task.remove.other",
            "task.edit.other",
            "task.edit.editing",
            "task.see.all"),
    ROOT("Супер-пользователь", "*");

    private String displayName;
    private final List<String> permissions = new ArrayList<>();

    UserRole(String displayName, String... permissions) {
        this.displayName = displayName;

        if (permissions.length != 0)
            this.permissions.addAll(Arrays.asList(permissions));
    }

    public static void load(JSONConfig config) {

        for (UserRole role : values()) {
            role.displayName = config.getString("role.".concat(role.name().toLowerCase()).concat(".displayName"), role.displayName);

            List<String> perms = config.getStringList("role.".concat(role.name().toLowerCase()).concat(".permissions"));

            if (perms != null && !perms.isEmpty()) {
                role.permissions.clear();
                role.permissions.addAll(perms);
            }
        }

    }

    public static UserRole matchByOrdinal(int ordinal) {

        return ordinal > values().length ? USER : values()[ordinal];
    }

    public boolean hasPermission(String perm) {
        return permissions.contains(perm) || permissions.contains("*");
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "displayName='" + displayName + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}
