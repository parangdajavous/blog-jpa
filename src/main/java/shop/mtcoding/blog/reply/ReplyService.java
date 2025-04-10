package shop.mtcoding.blog.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.board.Board;
import shop.mtcoding.blog.board.BoardRepository;
import shop.mtcoding.blog.user.User;

@RequiredArgsConstructor
@Service
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void 댓글쓰기(ReplyRequest.SaveDTO saveDTO, User sessionUser) {
        Board board = boardRepository.findById(saveDTO.getBoardId());
        if (board == null) throw new RuntimeException("게시글을 찾을 수 없습니다");
        Reply reply = saveDTO.toEntity(sessionUser, board);

        replyRepository.save(reply);
    }

    @Transactional
    public void 댓글삭제(Integer boardId, Integer id) {
        Board board = boardRepository.findById(boardId);
        if (board == null) throw new RuntimeException("게시글을 찾을 수 없습니다");

        Reply reply = replyRepository.findById(id);
        if (reply == null) throw new RuntimeException("댓글을 찾을 수 없습니다");

        replyRepository.delete(reply);
    }
}
