package ru.DailyProblemBot.telegram;

import ru.DailyProblemBot.enums.UserPhase;
import ru.DailyProblemBot.enums.UserRole;
import ru.DailyProblemBot.models.User;
import ru.DailyProblemBot.services.UserService;

import java.util.Objects;

public class UserProperties {

    private final int tgId;
    private User user;

    private String enteredName;
    private String enteredStudentId;
    private String enteredEmail;
    private String enteredPassword;
    // Используется при входе (содержит почту или номер студенческого)
    private String enteredLogin;

    // Используется для запоминания id последней заявки при её показе
    // где?: action.task.edit.EditTask, action.task.showall.ShowAll,
    // action.task.showmy.ShowMy, action.task.vote.ShowTask, action.task.editing.EditingTask
    private int lastShowTaskId = -1;

    private UserPhase phase;

    public UserProperties(int tgId) {
        this.tgId = tgId;
        this.phase = UserPhase.START;
    }

    public boolean tryLogin() {
        user = UserService.findUser(enteredLogin, enteredPassword);

        return user != null;
    }

    public boolean tryRegister() {

        try {
            enteredLogin = enteredEmail;
            UserService.saveUser(new User(tgId, enteredName, enteredStudentId, enteredEmail, enteredPassword, UserRole.USER));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void logout() {
        UserService.updateUser(user);
        UserManager.removeUserProperties(tgId);
    }

    public boolean isAFK(long minutes) {
        return isOnline() && user.getLastSeen().getTime() + minutes * 60 * 1000 < System.currentTimeMillis();
    }

    public void updateLastSeen() {
        if (isOnline()) {
            user.updateLastSeen();
            UserService.updateUser(user);
        }
    }

    public boolean isOnline() {
        return user != null;
    }

    public int getTgId() {
        return tgId;
    }

    public UserRole getRole() {
        return UserRole.matchByOrdinal(user.getRole());
    }

    public User getUser() {
        return user;
    }

    public UserPhase getPhase() {
        return phase;
    }

    public void setPhase(UserPhase phase) {
        this.phase = phase;
    }

    public void setEnteredName(String enteredName) {
        this.enteredName = enteredName;
    }

    public void setEnteredPassword(String enteredPassword) {
        this.enteredPassword = enteredPassword;
    }

    public void setEnteredLogin(String enteredLogin) {
        this.enteredLogin = enteredLogin;
    }

    public void setEnteredEmail(String enteredEmail) {
        this.enteredEmail = enteredEmail;
    }

    public void setEnteredStudentId(String enteredStudentId) {
        this.enteredStudentId = enteredStudentId;
    }

    public void setLastShowTaskId(int lastShowTaskId) {
        this.lastShowTaskId = lastShowTaskId;
    }

    public int getLastShowTaskId() {
        return lastShowTaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProperties that = (UserProperties) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }

    @Override
    public String toString() {
        return "UserProperties{" +
                "tgId=" + tgId +
                ", phase=" + phase +
                ", isOnline()=" + isOnline() +
                '}';
    }
}
