package ru.DailyProblemBot.dao;

import ru.DailyProblemBot.models.Vote;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.DailyProblemBot.util.HibernateSessionFactoryUtil;

import java.util.List;

public class VoteDao {

    public Vote findById(int id) {
        Session session = HibernateSessionFactoryUtil.getSession();
        Vote vote = session.get(Vote.class, id);
        session.close();
        return vote;
    }

    public void save(Vote vote) {
        Session session = HibernateSessionFactoryUtil.getSession();
        Transaction tx1 = session.beginTransaction();
        session.save(vote);
        tx1.commit();
        session.close();
    }

    public void update(Vote vote) {
        Session session = HibernateSessionFactoryUtil.getSession();
        Transaction tx1 = session.beginTransaction();
        session.update(vote);
        tx1.commit();
        session.close();
    }

    public void delete(Vote vote) {
        Session session = HibernateSessionFactoryUtil.getSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(vote);
        tx1.commit();
        session.close();
    }

    public List<Vote> findAll() {
        List<Vote> votes = (List<Vote>)  HibernateSessionFactoryUtil.getSession().createQuery("From Vote").list();
        return votes;
    }
}
