package shop.mtcoding.blog.board;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.error.ex.Exception403;
import shop.mtcoding.blog._core.error.ex.Exception404;
import shop.mtcoding.blog.love.Love;
import shop.mtcoding.blog.love.LoveRepository;
import shop.mtcoding.blog.reply.ReplyRepository;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final LoveRepository loveRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public void 글쓰기(BoardRequest.SaveDTO saveDTO, User sessionUser) {
        Board board = saveDTO.toEntity(sessionUser);
        boardRepository.save(board);
    }

    public List<Board> 글목록보기(Integer userId) {
        return boardRepository.findAll(userId);
    }

    public BoardResponse.DetailDTO 글상세보기(Integer id, Integer userId) {
        Board board = boardRepository.findByIdJoinUserAndReplies(id);

        Love love = loveRepository.findByUserIdAndBoardId(userId, id);
        Boolean isLove = love == null ? false : true;

        Integer loveId = love == null ? null : love.getId();

        Long loveCount = loveRepository.findByBoardId(board.getId());


        BoardResponse.DetailDTO detailDTO = new BoardResponse.DetailDTO(board, userId, isLove, loveCount.intValue(), loveId);
        return detailDTO;
    }

    public Board 업데이트글보기(Integer id, Integer sessionUserId) {
        Board boardPs = boardRepository.findById(id);
        if (boardPs == null) throw new Exception404("게시글을 찾을 수 없습니다.");

        if (!boardPs.getUser().getId().equals(sessionUserId)) throw new Exception403("권한이 없습니다.");

        return boardPs;
    }

    @Transactional
    public Board 게시글수정(BoardRequest.UpdateDTO reqDTO, Integer id, Integer sessionUserId) {
        Board board = boardRepository.findById(id);
        if (board == null) throw new Exception404("게시글을 찾을 수 없습니다");

        if (!board.getUser().getId().equals(sessionUserId)) throw new Exception403("권한이 없습니다.");

        board.update(reqDTO.getTitle(), reqDTO.getContent(), reqDTO.getIsPublic());

        return board;
    }

    @Transactional
    public void 게시글삭제(Integer id, Integer sessionUserId) {
        // 게시글 존재 확인
        Board boardPs = boardRepository.findById(id);
        if (boardPs == null) throw new Exception404("게시글을 찾을 수 없습니다");

        if (!boardPs.getUser().getId().equals(sessionUserId)) throw new Exception403("권한이 없습니다.");

        // 좋아요 삭제
        loveRepository.deleteByBoardId(boardPs.getId());

        boardRepository.deleteById(id);

    }
}
