package ru.DailyProblemBot.main;

import ru.DailyProblemBot.enums.TaskStatus;
import org.apache.commons.io.FileUtils;
import ru.DailyProblemBot.telegram.TelegramBot;
import ru.DailyProblemBot.enums.UserRole;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.telegram.timer.Timers;
import ru.DailyProblemBot.util.JSONConfig;
import ru.DailyProblemBot.util.config.Config;
import ru.DailyProblemBot.util.config.InitializedSettings;
import ru.DailyProblemBot.util.config.Messages;

import java.io.*;

public class Main {

    private static TelegramBot bot;
    private static InitializedSettings settings;

    public static void main(String[] args) throws IOException {

        File fileC = new File("config.json");
        if (!fileC.exists()){
            FileUtils.copyInputStreamToFile(Main.class.getClassLoader().getResourceAsStream("config.json"), fileC);
            System.out.println("Log: Создан файл конфигурации config.json");
        }

        File fileM = new File("messages.json");
        if (!fileM.exists()) {
            FileUtils.copyInputStreamToFile(Main.class.getClassLoader().getResourceAsStream("messages.json"), fileM);
            System.out.println("Log: Создан файл конфигурации messages.json");
        }

        JSONConfig cfg = new JSONConfig("./config.json");
        JSONConfig msgs = new JSONConfig("./messages.json");

        Messages.load(msgs);
        Config.load(cfg);
        UserRole.load(cfg);
        TaskStatus.load(cfg);

        settings = new InitializedSettings(cfg);

        Timers.load();

        ActionEngine.load();

        bot = new TelegramBot(Config.botUsername.get(), Config.botToken.get());
    }

    public static TelegramBot getTelegramBot() {
        return bot;
    }

    public static InitializedSettings getSettings() {
        return settings;
    }

}
