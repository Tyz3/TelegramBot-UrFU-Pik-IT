package ru.DailyProblemBot.telegram.action;

import ru.DailyProblemBot.enums.UserPhase;
import ru.DailyProblemBot.telegram.action.common.AuthMenu;
import ru.DailyProblemBot.telegram.action.common.MainMenu;
import ru.DailyProblemBot.telegram.action.common.StartMenu;
import ru.DailyProblemBot.telegram.action.inline.*;
import ru.DailyProblemBot.telegram.action.signin.Login;
import ru.DailyProblemBot.telegram.action.signin.Password;
import ru.DailyProblemBot.telegram.action.signup.Email;
import ru.DailyProblemBot.telegram.action.signup.Name;
import ru.DailyProblemBot.telegram.action.signup.StudentId;
import ru.DailyProblemBot.telegram.action.task.create.*;
import ru.DailyProblemBot.telegram.action.task.edit.*;
import ru.DailyProblemBot.telegram.action.task.editing.EditingMenu;
import ru.DailyProblemBot.telegram.action.task.showall.ShowAll;
import ru.DailyProblemBot.telegram.action.task.showmy.ShowMy;
import ru.DailyProblemBot.telegram.action.task.vote.ShowTask;

import java.util.HashMap;
import java.util.Map;

public class ActionEngine {

    private static final Map<UserPhase, Action> ACTIONS = new HashMap<>();
    private static final Map<String, InlineAction> INLINE_ACTIONS = new HashMap<>();

    public static Action getAction(UserPhase userPhase) {
        System.out.println("Log: Выполнен запрос действия " + userPhase);
        return ACTIONS.get(userPhase);
    }

    public static void putAction(UserPhase userPhase, Action action) {
        ACTIONS.put(userPhase, action);
    }

    public static InlineAction getInlineAction(String callbackData) {
        System.out.println("Log: Выполнен запрос inline-действия " + callbackData);
        //{taskId}:{inlineAction}
        return INLINE_ACTIONS.get(callbackData.split(":")[1]);
    }

    public static void putInlineAction(String actionName, InlineAction inlineAction) {
        INLINE_ACTIONS.put(actionName, inlineAction);
    }

    public static void load() {

        // Common
        new StartMenu(UserPhase.START);
        new AuthMenu(UserPhase.AUTH);
        new MainMenu(UserPhase.MAIN);

        // SignIn
        new Login(UserPhase.SIGNIN_LOGIN);
        new Password(UserPhase.SIGNIN_PASSWORD);

        // SignUp
        new Name(UserPhase.SIGNUP_NAME);
        new StudentId(UserPhase.SIGNUP_STUDENT_ID);
        new Email(UserPhase.SIGNUP_EMAIL);
        new ru.DailyProblemBot.telegram.action.signup.Password(UserPhase.SIGNUP_PASSWORD);

        // Task
        // .Create
        new Description(UserPhase.TASK_CREATE_DESCRIPTION);
        new Title(UserPhase.TASK_CREATE_TITLE);
        new Cost(UserPhase.TASK_CREATE_COST);
        new Files(UserPhase.TASK_CREATE_FILES);
        new Address(UserPhase.TASK_CREATE_ADDRESS);
        new Confirm(UserPhase.TASK_CREATE_CONFIRM);
        // .Edit
        new EditMenu(UserPhase.TASK_EDIT_MENU);
        new ChangeStatus(UserPhase.TASK_EDIT_STATUS);
        new ChangeVotesLimit(UserPhase.TASK_EDIT_VOTES_LIMIT);
        new ChangeCost(UserPhase.TASK_EDIT_COST);
        new ChangeAddress(UserPhase.TASK_EDIT_ADDRESS);
        new ChangeDescription(UserPhase.TASK_EDIT_DESCRIPTION);
        new ChangeTitle(UserPhase.TASK_EDIT_TITLE);
        new ChangeConfirm(UserPhase.TASK_EDIT_CONFIRM);
        // .Editing
        new EditingMenu(UserPhase.TASK_EDITING_MENU);
        // .ShowAll
        new ShowAll(UserPhase.TASK_SHOW_ALL);
        // .ShowMy
        new ShowMy(UserPhase.TASK_SHOW_MY);
        // .Vote
        new ShowTask(UserPhase.TASK_VOTE_SHOW);

        // .Inline
        new InlineDeleteTask("delete_task");
        new InlineEditTask("edit_task");
        new InlineDownloadFiles("download_files");
        new InlineVoteYes("vote_yes");
        new InlineVoteNo("vote_no");
        new InlineSeeVotesNo("count_votes_no");
        new InlineSeeVotesYes("count_votes_yes");
    }


}
