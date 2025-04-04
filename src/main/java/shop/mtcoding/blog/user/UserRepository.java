package shop.mtcoding.blog.user;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepository {
    private final EntityManager em;

    /*
        1. createNativeQuery -> 기본 쿼리
        2. createQuery -> JPA가 제공해주는 객체지향 쿼리
        3. NamedQuery -> QueryMethod는 함수 이름으로 쿼리 생성 - 사용X
        4. EntityGraph -> 지금 이해못함
     */


    public User findByUsername(String username) {
        return em.createQuery("select u from User u where u.username = :username", User.class) //객체지향 쿼리
                .setParameter("username", username)
                .getSingleResult();

    }

    public void save(User user) {
        em.persist(user);  // insert query 발동
    }
}
