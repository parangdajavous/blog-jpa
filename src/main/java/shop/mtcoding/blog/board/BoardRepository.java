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

    // 한방쿼리 (h2 query, om -> dto)
    public BoardResponse.DetailDTO findDetail(Integer boardId, Integer userId) {
        String sql = """
                SELECT new shop.mtcoding.blog.board.BoardResponse$DetailDTO(
                    b.id,
                    b.title,
                    b.content,
                    b.isPublic,
                    CASE WHEN b.user.id = :userId THEN true ELSE false END isOwner,
                    b.user.username,
                    b.createdAt,
                    (SELECT COUNT(l.id) FROM Love l WHERE l.board.id = :boardId),
                    (SELECT CASE WHEN COUNT(l2) > 0 THEN true ELSE false END
                     FROM Love l2
                     WHERE l2.board.id = :boardId AND l2.user.id = :userId)
                )
                FROM Board b
                WHERE b.id = :boardId
                """;

        Query query = em.createQuery(sql);
        query.setParameter("boardId", boardId);
        query.setParameter("userId", userId);
        return (BoardResponse.DetailDTO) query.getSingleResult();
    }

    public void save(Board board) {
        em.persist(board);
    }

    public List<Board> findAll(Integer userId) {  // Integer를 써야 null을 넘길 수 있다
        // 동적 query
        String s1 = "select b from Board b where b.isPublic = true or b.user.id = :userId order by b.id desc";
        String s2 = "select b from Board b where b.isPublic = true order by b.id desc";

        Query query = null;
        if (userId == null) {
            query = em.createQuery(s2, Board.class);
        } else {
            query = em.createQuery(s1, Board.class);
            query.setParameter("userId", userId);
        }

        return query.getResultList();
    }

    public Board findById(Integer id) {
        return em.find(Board.class, id);
    }

    public Board findByIdJoinUser(Integer id) {
        // b -> board에 있는 필드만 프로잭션 / fetch를 써야 board안에 있는 user 객체도 같이 프로잭션됨
        Query query = em.createQuery("select b from Board b join fetch b.user u where b.id = :id", Board.class);  // inner join (on절은 생략가능하다) -> 객체지향 쿼리
        query.setParameter("id", id);
        return (Board) query.getSingleResult();
    }
}
