package shop.mtcoding.blog.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.user.User;

@RequiredArgsConstructor
@Service
public class ReplyService {
    private final ReplyRepository replyRepository;

    @Transactional
    public void 댓글쓰기(ReplyRequest.SaveDTO reqDTO, User sessionUser) {
        replyRepository.save(reqDTO.toEntity(sessionUser));
    }

    @Transactional
    public Integer 댓글삭제(Integer id, Integer sessionUserId) {
        Reply replyPs = replyRepository.findById(id);
        if (replyPs == null) throw new RuntimeException("삭제할 댓글이 없습니다.");
        if (!(replyPs.getUser().getId().equals(sessionUserId))) throw new RuntimeException("삭제할 권한이 없습니다.");

        Integer boardId = replyPs.getBoard().getId();

        replyRepository.deleteById(id);

        return boardId;

    }
}
