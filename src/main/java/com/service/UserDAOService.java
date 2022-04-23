package com.service;

import com.entities.User;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class UserDAOService {
    @PersistenceContext
    private EntityManager entityManager;

    public void insert(User user) throws Exception {
        String q = "INSERT INTO user (name,role) VALUES (?1,?2)";
        this.entityManager.createNativeQuery(q)
                .setParameter(1, user.getName())
                .setParameter(2, user.getRole())
                .executeUpdate();
    }

    public boolean insertBulk(ArrayList<User> users) {
        String q = "INSERT INTO user (name,role) VALUES ";
        int size = users.size();
        for (int i = 0; i < size; i++) {
            User user = users.get(i);
            q += "(" + "'" + user.getName() + "'" + "," + "'" + user.getRole() + "'" + ")" +
                    (i + 1 == size ? "" : ",");
        }
        try {
            this.entityManager.createNativeQuery(q).executeUpdate();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<User> getAllUsers() {
        @SuppressWarnings("unchecked")
        List<User> response = this.entityManager.createNativeQuery(
                "SELECT * FROM user",
                User.class)
                .getResultList();

        return response;
    }

    public User findById(int id) {
        try {
            List<?> response = this.entityManager.createNativeQuery(
                    "SELECT * FROM user " +
                            "WHERE id=?1",
                    User.class)
                    .setParameter(1, id)
                    .getResultList();

            return (User) response.get(0);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void deleteUser(int id) throws Exception {
        this.entityManager.createNativeQuery("DELETE FROM user WHERE id=" + id)
                .executeUpdate();
    }
}
