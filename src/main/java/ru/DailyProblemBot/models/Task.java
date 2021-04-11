package ru.DailyProblemBot.models;

import ru.DailyProblemBot.enums.TaskStatus;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String description;
    private int status;
    private String cost;
    private String address;
    private String files = "";

    @Column(name = "owner_id")
    private int ownerId;

    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Vote> votes = new ArrayList<>();

    @Column(name = "vote_count_pros")
    private int voteCountYes;

    @Column(name = "vote_count_cons")
    private int voteCountNo;

    @Column(name = "vote_count_limit")
    private int voteCountLimit = 500;

    @Column(name = "create_at")
    private Date createAt = new Date();

    @Column(name = "done_at")
    private Date doneAt;

    @Column(name = "editor_id")
    private int editorId = -1;

    public Task() {}

    public Task(String title, TaskStatus status, String description, String cost, String address, List<String> files) {
        this.title = title;
        this.description = description;
        this.status = status.ordinal();
        this.cost = cost;
        this.address = address;
        this.files = String.join(",", files);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TaskStatus getStatus() {
        return TaskStatus.matchByOrdinal(status);
    }

    public void setStatus(TaskStatus status) {
        this.status = status.ordinal();
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public boolean hasAddress() {
        return this.address != null;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getFiles() {
        return files.equals("") ? new ArrayList<>() : Arrays.asList(files.split(","));
    }

    public void addFile(String fileEntry) {
        files = files.concat(fileEntry).concat(",");
    }

    public void clearFiles() {
        files = "";
    }

    public boolean hasFiles() {
        return files.length() != 0;
    }

    public String getFirstPhotoFile() {

        // Попробуем найти фото в приложениях
        String photo = null;

        for (String file : getFiles()) {
            // Берём изображение как фото, даже если оно отправлено как документ
            if (file.startsWith("img") || file.startsWith("doc:image/png") || file.startsWith("doc:image/jpg") || file.startsWith("doc:image/jpeg")) {
                photo = file;
                break;
            }
        }

        return photo;
    }

    public void setEditor(User user) {
        this.editorId = user.getId();
    }

    public void clearEditor() {
        this.editorId = -1;
    }

    public boolean hasEditor() {
        return editorId != -1;
    }

    public void addVote(Vote vote) {
        votes.add(vote);
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void incVoteCountYes() {
        this.voteCountYes++;
    }

    public void setVoteCount(int voteCountYes) {
        this.voteCountYes = voteCountYes;
    }

    public int getVoteCountYes() {
        return voteCountYes;
    }

    public void incVoteCountNo() {
        this.voteCountNo++;
    }

    public void setVoteCountNo(int voteCountNo) {
        this.voteCountNo = voteCountNo;
    }

    public int getVoteCountNo() {
        return voteCountNo;
    }

    public void setVoteCountLimit(int voteCountLimit) {
        this.voteCountLimit = voteCountLimit;
    }

    public int getVoteCountLimit() {
        return voteCountLimit;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setDoneAt(Date doneAt) {
        this.doneAt = doneAt;
    }

    public Date getDoneAt() {
        return doneAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
