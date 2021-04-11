package ru.DailyProblemBot.telegram.action.common;

import ru.DailyProblemBot.enums.Keyboard;
import ru.DailyProblemBot.enums.UserPhase;
import ru.DailyProblemBot.main.Main;
import ru.DailyProblemBot.models.User;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.DailyProblemBot.services.TaskService;
import ru.DailyProblemBot.telegram.UserProperties;
import ru.DailyProblemBot.telegram.action.Action;
import ru.DailyProblemBot.telegram.action.ActionEngine;
import ru.DailyProblemBot.util.config.Messages;

public class MainMenu extends Action {

    public MainMenu(UserPhase phase) {
        super(phase);
    }

    @Override
    public void alert(String chatId, UserProperties properties) {
        properties.setPhase(phase);

        User user = properties.getUser();
        Keyboard keyboard = null;

        switch (properties.getRole()) {
            case USER:
                keyboard = Keyboard.MAIN_USER;
                break;
            case EXPERT:
                keyboard = Keyboard.MAIN_EXPERT;
                break;
            case MODER:
                keyboard = Keyboard.MAIN_MODER;
                break;
            case ROOT:
                keyboard = Keyboard.MAIN_ROOT;
                break;
        }

        Messages.main
                .replace("{name}", user.getName())
                .replace("{roleDisplayName}", properties.getRole().getDisplayName())
                .replace("{tasksAmount}", TaskService.countTasksByOwnerId(user.getId()))
                .replace("{createAt}", Main.getSettings().makeDateByFormat(user.getCreateAt()))
                .replace("{lastUpdate}", Main.getSettings().makeDateByFormat(user.getLastSeen()))
                .send(chatId, keyboard);
    }

    @Override
    public void handle(String chatId, Message message, UserProperties properties) {

        if (message.hasText()) {

            switch (message.getText()) {
                case "Создать заявку":
                    if (properties.getRole().hasPermission("task.create"))
                        ActionEngine.getAction(UserPhase.TASK_CREATE_DESCRIPTION).alert(chatId, properties);
                    else
                        Messages.notEnoughPerms.send(chatId);
                    break;
                case "Голосование":
                    if (properties.getRole().hasPermission("task.vote.student") || properties.getRole().hasPermission("task.vote.expert"))
                        ActionEngine.getAction(UserPhase.TASK_VOTE_SHOW).alert(chatId, properties);
                    else
                        Messages.notEnoughPerms.send(chatId);
                    break;
                case "Свои заявки":
                    if (properties.getRole().hasPermission("task.see.self"))
                        ActionEngine.getAction(UserPhase.TASK_SHOW_MY).alert(chatId, properties);
                    else
                        Messages.notEnoughPerms.send(chatId);
                    break;
                case "Все заявки":
                    if (properties.getRole().hasPermission("task.see.all"))
                        ActionEngine.getAction(UserPhase.TASK_SHOW_ALL).alert(chatId, properties);
                    else
                        Messages.notEnoughPerms.send(chatId);
                    break;
                case "Управление заявками":
                    if (properties.getRole().hasPermission("task.edit.other"))
                        ActionEngine.getAction(UserPhase.TASK_EDIT_MENU).alert(chatId, properties);
                    else
                        Messages.notEnoughPerms.send(chatId);
                    break;
                case "Управление своими заявками":
                    if (properties.getRole().hasPermission("task.edit.self"))
                        ActionEngine.getAction(UserPhase.TASK_EDIT_MENU).alert(chatId, properties);
                    else
                        Messages.notEnoughPerms.send(chatId);
                    break;
                case "Редактируемые заявки":
                    if (properties.getRole().hasPermission("task.edit.editing"))
                        ActionEngine.getAction(UserPhase.TASK_EDITING_MENU).alert(chatId, properties);
                    else
                        Messages.notEnoughPerms.send(chatId);
                    break;
                case "Выйти":
                    properties.logout();
                    ActionEngine.getAction(UserPhase.START).alert(chatId, properties);
                    break;
            }

        }

    }
}
