package shop.mtcoding.blog.board;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;


    public void save(Board board) {
        em.persist(board);
    }

    // locahost:8080?page=0
    public List<Board> findAll(int page) {
        String sql = "select b from Board b where b.isPublic = true order by b.id desc";
        Query query = em.createQuery(sql, Board.class);
        query.setFirstResult(page * 3);
        query.setMaxResults(3);

        return query.getResultList();
    }

    public List<Board> findAll(Integer userId, int page) {
        String sql = "select b from Board b where b.isPublic = true or b.user.id = :userId order by b.id desc";
        Query query = em.createQuery(sql, Board.class);
        query.setParameter("userId", userId);
        query.setFirstResult(page * 3);
        query.setMaxResults(3);
        return query.getResultList();
    }

//    public List<Board> findAll(Integer userId) {  // Integer를 써야 null을 넘길 수 있다
//        // 동적 query
//        String s1 = "select b from Board b where b.isPublic = true or b.user.id = :userId order by b.id desc";
//        String s2 = "select b from Board b where b.isPublic = true order by b.id desc";
//
//        Query query = null;
//        if (userId == null) {
//            query = em.createQuery(s2, Board.class);
//        } else {
//            query = em.createQuery(s1, Board.class);
//            query.setParameter("userId", userId);
//        }
//
//        return query.getResultList();
//    }


    public Board findById(Integer id) {
        return em.find(Board.class, id);
    }

    public Board findByIdJoinUser(Integer id) {
        // b -> board에 있는 필드만 프로잭션 / fetch를 써야 board안에 있는 user 객체도 같이 프로잭션됨
        Query query = em.createQuery("select b from Board b join fetch b.user u where b.id = :id", Board.class);  // inner join (on절은 생략가능하다) -> 객체지향 쿼리
        query.setParameter("id", id);
        return (Board) query.getSingleResult();
    }


    public Board findByIdJoinUserAndReplies(Integer id) {
        Query query = em.createQuery("select b from Board b join fetch b.user u left join fetch b.replies r left join fetch r.user where b.id = :id order by r.id desc", Board.class);  // left join (on절은 생략가능하다) -> 객체지향 쿼리
        query.setParameter("id", id);
        return (Board) query.getSingleResult();
    }

    public void deleteById(Integer id) {
        Board board = em.find(Board.class, id); // 영속성 컨텍스트에 넣기
        em.remove(board); // 연관된 Reply들도 함께 삭제됨 (Cascade 동작)
    }
}
