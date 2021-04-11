package ru.DailyProblemBot.telegram.timer.session;

import ru.DailyProblemBot.enums.UserPhase;
import ru.DailyProblemBot.main.Main;
import ru.DailyProblemBot.telegram.MessageControl;
import ru.DailyProblemBot.telegram.UserManager;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.ActionEngine;

import java.util.Timer;
import java.util.TimerTask;

public class SessionTimer extends TimerTask {


    private static final Timer timer = new Timer(true);

    public static void load() {
        timer.scheduleAtFixedRate(new SessionTimer(), 0L, Main.getSettings().SESSION_TIMER_PERIOD);
    }

    @Override
    public void run() {

        for (UserProperties properties : UserManager.getUserProperties()) {

            if (properties.isAFK(Main.getSettings().AFK_MINUTES_THRESHOLD)) {

                String chatId = String.valueOf(properties.getTgId());

                MessageControl.deleteAllMessages(chatId);
                UserManager.removeUserProperties(properties.getTgId());
                ActionEngine.getAction(UserPhase.START).alert(chatId, properties);

            }

        }
    }
}
