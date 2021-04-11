package ru.DailyProblemBot.telegram;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {

    private static final Map<Integer, UserProperties> usersProperties = new ConcurrentHashMap<>();

    public static UserProperties getOrCreateUserProperties(Integer tgId) {

        if (!usersProperties.containsKey(tgId)) {
            UserProperties properties = new UserProperties(tgId);
            usersProperties.put(tgId, properties);
            System.out.println("Log: Создание новых свойств пользователя > " + properties);
            return properties;
        }

        UserProperties properties = usersProperties.get(tgId);
        System.out.println("Log: Получаем свойства пользователя > " + properties);

        return properties;
    }

    public static void removeUserProperties(Integer tdId) {
        usersProperties.remove(tdId);
    }

    public static Collection<UserProperties> getUserProperties() {
        return usersProperties.values();
    }
}
