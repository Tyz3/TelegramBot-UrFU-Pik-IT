package ru.DailyProblemBot.dao;

import ru.DailyProblemBot.enums.UserRole;
import ru.DailyProblemBot.models.Task;
import ru.DailyProblemBot.models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.DailyProblemBot.util.HibernateSessionFactoryUtil;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {

    public int countTasksByOwnerId(int ownerId) {

        String sql = "SELECT COUNT(id) FROM `tasks` WHERE owner_id = :ownerId";

        Session session = HibernateSessionFactoryUtil.getSession();
        session.beginTransaction();

        Query query = session.createSQLQuery(sql)
                .setParameter("ownerId", ownerId);

        int d = Integer.parseInt(query.getSingleResult().toString());

        session.getTransaction().commit();
        session.close();

        return d;
    }

    public Task findById(int id) {
        Session session = HibernateSessionFactoryUtil.getSession();
        session.beginTransaction();
        Task task = session.get(Task.class, id);
        session.getTransaction().commit();
        session.close();
        return task;
    }

    public void save(Task task) {
        Session session = HibernateSessionFactoryUtil.getSession();
        Transaction tx1 = session.beginTransaction();
        session.save(task);
        tx1.commit();
        session.close();
    }

    public void update(Task task) {
        Session session = HibernateSessionFactoryUtil.getSession();
        Transaction tx1 = session.beginTransaction();
        session.update(task);
        tx1.commit();
        session.close();
    }

    public void delete(Task task) {
        Session session = HibernateSessionFactoryUtil.getSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(task);
        tx1.commit();
        session.close();
    }

    public List<Task> findAll() {
        Session session = HibernateSessionFactoryUtil.getSession();
        Transaction tx1 = session.beginTransaction();
        List<Task> tasks = session.createQuery("From Task").list();
        tx1.commit();
        session.close();
        return tasks;
    }

    public List<Task> findFirst(int count) {

        String sql = "SELECT * FROM `tasks` ORDER BY id ASC LIMIT :count";

        Session session = HibernateSessionFactoryUtil.getSession();
        session.beginTransaction();

        Query query = session.createSQLQuery(sql)
                .addEntity(Task.class)
                .setParameter("count", count);

        List<Task> tasks = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return tasks;
    }

    public List<Task> findEnd(int count) {

        String sql = "SELECT * FROM `tasks` ORDER BY id DESC LIMIT :count";

        Session session = HibernateSessionFactoryUtil.getSession();
        session.beginTransaction();

        Query query = session.createSQLQuery(sql)
                .addEntity(Task.class)
                .setParameter("count", count);

        List<Task> tasks = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return tasks;
    }

    public List<Task> findAFew(int count, int lastId, boolean moveLeftToRight) {

        String compare = moveLeftToRight ? ">" : "<=";

        String sql = "SELECT * FROM `tasks` WHERE id ".concat(compare).concat(" :lastId LIMIT :count");

        Session session = HibernateSessionFactoryUtil.getSession();
        session.beginTransaction();

        Query query = session.createSQLQuery(sql)
                .addEntity(Task.class)
                .setParameter("lastId", moveLeftToRight ? lastId : lastId - count)
                .setParameter("count", count);

        List<Task> tasks = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return tasks;
    }

    public List<Task> findFirstByPerms(User user, int count) {

        String sql = "SELECT * FROM `tasks` WHERE owner_id != :userId";

        UserRole role = UserRole.matchByOrdinal(user.getRole());

        // Проверяем доступ к голосованию
        if (role.hasPermission("task.vote.student")) {

            if (role.hasPermission("task.vote.expert")) {
                // Выбор всех заявок, как для эксперта, так и для студентов
                sql = sql.concat(" AND (status = 2 OR status = 3) ORDER BY id ASC LIMIT :count");
            } else {
                // Выбор всех заявок доступных для голосования студентам,
                // исключая себя, если являешься владельцем
                sql = sql.concat(" AND status = 2 ORDER BY id ASC LIMIT :count");
            }

        } else {

            if (role.hasPermission("task.vote.expert")) {
                // Выбор всех заявок доступных для голосования экспертам,
                // исключая себя, если являешься владельцем
                sql = sql.concat(" AND status = 3 ORDER BY id ASC LIMIT :count");
            } else {
                return new ArrayList<>();
            }

        }

        Session session = HibernateSessionFactoryUtil.getSession();

        try {
            session.beginTransaction();

            Query query = session.createSQLQuery(sql)
                    .addEntity(Task.class)
                    .setParameter("userId", user.getId())
                    .setParameter("count", count);

            List<Task> tasks = query.getResultList();

            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.getTransaction().commit();
            session.close();
        }

        return new ArrayList<>();
    }

    public List<Task> findEndByPerms(User user, int count) {

        String sql = "SELECT * FROM `tasks` WHERE owner_id != :userId";

        UserRole role = UserRole.matchByOrdinal(user.getRole());

        // Проверяем доступ к голосованию
        if (role.hasPermission("task.vote.student")) {

            if (role.hasPermission("task.vote.expert")) {
                // Выбор всех заявок, как для эксперта, так и для студентов
                sql = sql.concat(" AND (status = 2 OR status = 3) ORDER BY id DESC LIMIT :count");
            } else {
                // Выбор всех заявок доступных для голосования студентам,
                // исключая себя, если являешься владельцем
                sql = sql.concat(" AND status = 2 ORDER BY id DESC LIMIT :count");
            }

        } else {

            if (role.hasPermission("task.vote.expert")) {
                // Выбор всех заявок доступных для голосования экспертам,
                // исключая себя, если являешься владельцем
                sql = sql.concat(" AND status = 3 ORDER BY id DESC LIMIT :count");
            } else {
                return new ArrayList<>();
            }

        }

        Session session = HibernateSessionFactoryUtil.getSession();

        try {
            session.beginTransaction();

            Query query = session.createSQLQuery(sql)
                    .addEntity(Task.class)
                    .setParameter("userId", user.getId())
                    .setParameter("count", count);

            List<Task> tasks = query.getResultList();

            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.getTransaction().commit();
            session.close();
        }

        return new ArrayList<>();
    }

    public List<Task> findAFewByPerms(User user, int count, int lastId, boolean moveLeftToRight) {

        String compare = moveLeftToRight ? ">" : "<=";

        String sql = "SELECT * FROM `tasks` WHERE id ".concat(compare).concat(" :lastId AND owner_id != :userId");

        UserRole role = UserRole.matchByOrdinal(user.getRole());

        // Проверяем доступ к голосованию
        if (role.hasPermission("task.vote.student")) {

            if (role.hasPermission("task.vote.expert")) {
                // Выбор всех заявок, как для эксперта, так и для студентов
                sql = sql.concat(" AND (status = 2 OR status = 3) LIMIT :count");
            } else {
                // Выбор всех заявок доступных для голосования студентам,
                // исключая себя, если являешься владельцем
                sql = sql.concat(" AND status = 2 LIMIT :count");
            }

        } else {

            if (role.hasPermission("task.vote.expert")) {
                // Выбор всех заявок доступных для голосования экспертам,
                // исключая себя, если являешься владельцем
                sql = sql.concat(" AND status = 3 LIMIT :count");
            } else {
                return new ArrayList<>();
            }

        }

        Session session = HibernateSessionFactoryUtil.getSession();

        try {
            session.beginTransaction();

            Query query = session.createSQLQuery(sql)
                    .addEntity(Task.class)
                    .setParameter("userId", user.getId())
                    .setParameter("lastId", moveLeftToRight ? lastId : lastId - count)
                    .setParameter("count", count);

            List<Task> tasks = query.getResultList();

            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.getTransaction().commit();
            session.close();
        }

        return new ArrayList<>();
    }

    /*
    SELECT *
FROM
(
    SELECT *
    FROM
    	`tasks`
    WHERE
    	id > 8 AND owner_id != 10 AND (status = 2 OR status = 3)
) AS T
WHERE
	T.id IN
    	(
            SELECT
            	`votes`.`task_id`
            FROM
            	`votes`
            WHERE
            	votes.voteer_id != 10
        )
     */
    public List<Task> findFirstForEdit(int count) {

        String sql = "SELECT * FROM `tasks` WHERE editor_id = -1 ORDER BY id ASC LIMIT :count";

        Session session = HibernateSessionFactoryUtil.getSession();
        session.beginTransaction();

        Query query = session.createSQLQuery(sql)
                .addEntity(Task.class)
                .setParameter("count", count);

        List<Task> tasks = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return tasks;
    }

    public List<Task> findEndForEdit(int count) {

        String sql = "SELECT * FROM `tasks` WHERE editor_id = -1 ORDER BY id DESC LIMIT :count";

        Session session = HibernateSessionFactoryUtil.getSession();
        session.beginTransaction();

        Query query = session.createSQLQuery(sql)
                .addEntity(Task.class)
                .setParameter("count", count);

        List<Task> tasks = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return tasks;
    }

    public List<Task> findAFewForEdit(int count, int lastId, boolean moveLeftToRight) {

        String compare = moveLeftToRight ? ">" : "<=";

        String sql = "SELECT * FROM `tasks` WHERE editor_id = -1 AND id ".concat(compare).concat(" :lastId LIMIT :count");

        Session session = HibernateSessionFactoryUtil.getSession();
        session.beginTransaction();

        Query query = session.createSQLQuery(sql)
                .addEntity(Task.class)
                .setParameter("lastId", moveLeftToRight ? lastId : lastId - count)
                .setParameter("count", count);

        List<Task> tasks = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return tasks;
    }


    public List<Task> findFirstMeEditor(User user, int count) {

        String sql = "SELECT * FROM `tasks` WHERE editor_id = :userId ORDER BY id ASC LIMIT :count";

        Session session = HibernateSessionFactoryUtil.getSession();
        session.beginTransaction();

        Query query = session.createSQLQuery(sql)
                .addEntity(Task.class)
                .setParameter("userId", user.getId())
                .setParameter("count", count);

        List<Task> tasks = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return tasks;
    }

    public List<Task> findEndMeEditor(User user, int count) {

        String sql = "SELECT * FROM `tasks` WHERE editor_id = :userId ORDER BY id DESC LIMIT :count";

        Session session = HibernateSessionFactoryUtil.getSession();
        session.beginTransaction();

        Query query = session.createSQLQuery(sql)
                .setParameter("userId", user.getId())
                .addEntity(Task.class)
                .setParameter("count", count);

        List<Task> tasks = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return tasks;
    }

    public List<Task> findAFewMeEditor(User user, int count, int lastId, boolean moveLeftToRight) {

        String compare = moveLeftToRight ? ">" : "<=";

        String sql = "SELECT * FROM `tasks` WHERE editor_id = :userId AND id ".concat(compare).concat(" :lastId LIMIT :count");

        Session session = HibernateSessionFactoryUtil.getSession();
        session.beginTransaction();

        Query query = session.createSQLQuery(sql)
                .addEntity(Task.class)
                .setParameter("userId", user.getId())
                .setParameter("lastId", moveLeftToRight ? lastId : lastId - count)
                .setParameter("count", count);

        List<Task> tasks = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return tasks;
    }



    public List<Task> findFirstForMe(User user, int count) {

        String sql = "SELECT * FROM `tasks` WHERE owner_id = :userId ORDER BY id ASC LIMIT :count";

        Session session = HibernateSessionFactoryUtil.getSession();
        session.beginTransaction();

        Query query = session.createSQLQuery(sql)
                .addEntity(Task.class)
                .setParameter("userId", user.getId())
                .setParameter("count", count);

        List<Task> tasks = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return tasks;
    }

    public List<Task> findEndForMe(User user, int count) {

        String sql = "SELECT * FROM `tasks` WHERE owner_id = :userId ORDER BY id DESC LIMIT :count";

        Session session = HibernateSessionFactoryUtil.getSession();
        session.beginTransaction();

        Query query = session.createSQLQuery(sql)
                .setParameter("userId", user.getId())
                .addEntity(Task.class)
                .setParameter("count", count);

        List<Task> tasks = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return tasks;
    }

    public List<Task> findAFewForMe(User user, int count, int lastId, boolean moveLeftToRight) {

        String compare = moveLeftToRight ? ">" : "<=";

        String sql = "SELECT * FROM `tasks` WHERE owner_id = :userId AND id ".concat(compare).concat(" :lastId LIMIT :count");

        Session session = HibernateSessionFactoryUtil.getSession();
        session.beginTransaction();

        Query query = session.createSQLQuery(sql)
                .addEntity(Task.class)
                .setParameter("userId", user.getId())
                .setParameter("lastId", moveLeftToRight ? lastId : lastId - count)
                .setParameter("count", count);

        List<Task> tasks = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return tasks;
    }
}
