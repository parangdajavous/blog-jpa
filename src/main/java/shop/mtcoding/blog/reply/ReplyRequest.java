package shop.mtcoding.blog.reply;

import lombok.Data;
import shop.mtcoding.blog.board.Board;
import shop.mtcoding.blog.user.User;

public class ReplyRequest {

    @Data
    public static class SaveDTO {
        private Integer id;
        private String content;
        private Integer boardId;


        public Reply toEntity(User user, Board board) {
            return Reply.builder()
                    .id(id)
                    .content(content)
                    .user(user)   // user 객체 필요
                    .board(board)
                    .build();
        }
    }
}
