package ru.DailyProblemBot.models;

import ru.DailyProblemBot.enums.UserRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(name = "tg_id")
    private int tgId;

    @Column(name = "student_id")
    private String studentId;

    private int role;

    private String email;
    private String password;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "last_seen")
    private Date lastSeen;

    private boolean ban = false;

//    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private final List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Vote> votes = new ArrayList<>();

    public User() {}

    public User(Integer tgId, String name, String studentId, String email, String password, UserRole role) {
        this.tgId = tgId;
        this.name = name;
        this.studentId = studentId;
        this.email = email;
        this.password = password;
        this.role = role.ordinal();

        createAt = new Date();
        lastSeen = new Date();
    }

    public int getId() {
        return id;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setTgId(int tgId) {
        this.tgId = tgId;
    }

    public int getTgId() {
        return tgId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public boolean hasBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(UserRole role) {
        this.role = role.ordinal();
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getRole() {
        return role;
    }

//    public void addTask(Task task) {
//        tasks.add(task);
//    }
//
//    public void removeTask(Task task) {
//        tasks.remove(task);
//    }
//
//    public List<Task> getTasks() {
//        return tasks;
//    }

    public void addVote(Vote vote) {
        votes.add(vote);
    }

    public void removeVote(Vote vote) {
        votes.remove(vote);
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void updateLastSeen() {
        this.lastSeen = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tgId=" + tgId +
                ", studentId=" + studentId +
                ", role='" + UserRole.matchByOrdinal(role) + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", createAt=" + createAt +
                ", lastSeen=" + lastSeen +
                ", ban=" + ban +
//                ", tasksAmount=" + tasks.size() +
                ", votesAmount=" + votes.size() +
                '}';
    }
}
