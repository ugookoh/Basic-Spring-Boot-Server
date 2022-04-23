package com.service;

import com.entities.Post;
import org.springframework.stereotype.Repository;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class PostDAOService {
    @PersistenceContext
    private EntityManager entityManager;

    public void createPost(int userId, String description) throws Exception {
        String q = "INSERT INTO post (user_id,description) VALUES (?1,?2)";
        this.entityManager.createNativeQuery(q)
                .setParameter(1, userId)
                .setParameter(2, description)
                .executeUpdate();
    }

    public Post getOnePost(int postId) {
        try {
            List<?> response = this.entityManager.createNativeQuery(
                    "SELECT * FROM post " +
                            "WHERE post.id=?1",
                    Post.class) 
                    .setParameter(1, postId)
                    .getResultList();

            return (Post) response.get(0);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public List<Post> getAllUserPost(int userId) {
        try {
            @SuppressWarnings("unchecked")
            List<Post> response = this.entityManager.createNativeQuery(
                    "SELECT * FROM post " +
                            "WHERE post.user_id=?1",
                    Post.class)
                    .setParameter(1, userId)
                    .getResultList();
            return response;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void deletePost(int id) {
        this.entityManager.createNativeQuery("DELETE FROM post WHERE id=" + id)
                .executeUpdate();
    }

    public void updatePost(int id, String description) {
        this.entityManager.createNativeQuery("UPDATE post " +
                "SET description=?1" +
                " WHERE post.id=?2")
                .setParameter(1, description)
                .setParameter(2, id)
                .executeUpdate();
    }
}
