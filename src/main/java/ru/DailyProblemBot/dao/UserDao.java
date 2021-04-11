package ru.DailyProblemBot.dao;

import ru.DailyProblemBot.models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.DailyProblemBot.util.HibernateSessionFactoryUtil;

import javax.persistence.Query;
import java.util.List;

public class UserDao {

    public User findById(int id) {
        Session session = HibernateSessionFactoryUtil.getSession();
        session.beginTransaction();
        User user = session.get(User.class, id);
        session.getTransaction().commit();
        session.close();
        return user;
    }

    public User findByEmailOrStudentId(String input) {
        String sql = "SELECT * FROM users WHERE (email LIKE :input) OR (student_id LIKE :input)";

        Session session = HibernateSessionFactoryUtil.getSession();
        session.beginTransaction();

        Query query = session.createSQLQuery(sql)
                .addEntity(User.class)
                .setParameter("input", input);
        List<User> users = query.getResultList();

        session.getTransaction().commit();

        session.close();
        return users.size() == 0 ? null : users.get(0);
    }

    public void save(User user) {
        Session session = HibernateSessionFactoryUtil.getSession();
        Transaction tx1 = session.beginTransaction();
        session.save(user);
        tx1.commit();
        session.close();
    }

    public void update(User user) {
        Session session = HibernateSessionFactoryUtil.getSession();
        Transaction tx1 = session.beginTransaction();
        session.update(user);
        tx1.commit();
        session.close();
    }

    public void delete(User user) {
        Session session = HibernateSessionFactoryUtil.getSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(user);
        tx1.commit();
        session.close();
    }

    public List<User> findAll() {
        List<User> users = (List<User>)  HibernateSessionFactoryUtil.getSession().createQuery("From User").list();
        return users;
    }
}
