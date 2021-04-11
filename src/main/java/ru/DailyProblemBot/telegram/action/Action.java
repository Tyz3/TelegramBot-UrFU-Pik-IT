package ru.DailyProblemBot.telegram.action;

import ru.DailyProblemBot.enums.UserPhase;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.DailyProblemBot.telegram.UserProperties;

public abstract class Action {

    protected final UserPhase phase;

    protected Action(UserPhase phase) {
        this.phase = phase;

        ActionEngine.putAction(phase, this);
    }

    public abstract void alert(String chatId, UserProperties properties);
    public abstract void handle(String chatId, Message message, UserProperties properties);

}
