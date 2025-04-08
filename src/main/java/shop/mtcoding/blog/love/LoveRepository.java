package shop.mtcoding.blog.love;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LoveRepository {
    private final EntityManager em;


    public Love findByUserIdAndBoardId(Integer userId, Integer boardId) {
        Query query = em.createQuery("select lo from Love lo where lo.user.id = :userId and lo.board.id = :boardId", Love.class);
        query.setParameter("userId", userId);
        query.setParameter("boardId", boardId);

        try {
            return (Love) query.getSingleResult();  // unique 제약조건이기 때문에 SingleResult
        } catch (Exception e) {
            return null;
        }
    }

    public Long findByLoveCount(int boardId) {
        Query query = em.createQuery("select count(lo) from Love lo where lo.board.id = :boardId");
        query.setParameter("boardId", boardId);
        Long count = (Long) query.getSingleResult();
        try {
            return count;  // unique 제약조건이기 때문에 SingleResult
        } catch (Exception e) {
            return null;
        }
    }

}
