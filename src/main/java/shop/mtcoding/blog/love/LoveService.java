package shop.mtcoding.blog.love;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.board.BoardRepository;
import shop.mtcoding.blog.user.UserRepository;

@RequiredArgsConstructor
@Service
public class LoveService {
    private final LoveRepository loveRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public LoveResponse.SaveDTO 좋아요(LoveRequest.SaveDTO reqDTO, Integer sessionUserId) {
        Love lovePs = loveRepository.save(reqDTO.toEntity(sessionUserId));
        Long loveCount = loveRepository.findByBoardId(reqDTO.getBoardId());
        return new LoveResponse.SaveDTO(lovePs.getId(), loveCount.intValue());
    }

    @Transactional
    public LoveResponse.DeleteDTO 좋아요취소(Integer id, Integer sessionUserId) {

        Love lovePs = loveRepository.findById(id);
        if (lovePs == null) throw new RuntimeException("취소할 좋아요가 없습니다.");

        // 권한체크 (lovePs.gerUser().getId() 비교 sessionUserId)
        if (!(lovePs.getUser().getId().equals(sessionUserId))) {
            throw new RuntimeException("좋아요 취소의 권한이 없습니다");
        }

        Integer boardId = lovePs.getBoard().getId();

        loveRepository.deleteById(id);

        Long loveCount = loveRepository.findByBoardId(boardId);

        return new LoveResponse.DeleteDTO(loveCount.intValue());
    }
}
