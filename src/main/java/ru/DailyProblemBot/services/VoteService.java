package ru.DailyProblemBot.services;

import ru.DailyProblemBot.dao.VoteDao;
import ru.DailyProblemBot.models.Task;
import ru.DailyProblemBot.models.User;
import ru.DailyProblemBot.models.Vote;

import java.util.List;

public class VoteService {

    private static final VoteDao votesDao = new VoteDao();

    public static Vote findVote(int id) {
        return votesDao.findById(id);
    }

    public static void saveVote(Vote vote) {
        votesDao.save(vote);
    }

    public static void deleteVote(Vote vote) {
        votesDao.delete(vote);
    }

    public static void updateVote(Vote vote) {
        votesDao.update(vote);
    }

    public static List<Vote> findAllVotes() {
        return votesDao.findAll();
    }

    public static boolean hasVoteFromUser(Task task, User user) {
        return task.getVotes().stream().anyMatch(vote -> vote.getUser().getId() == user.getId());
    }
}
