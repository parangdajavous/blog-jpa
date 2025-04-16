package shop.mtcoding.blog.board;

import lombok.Data;
import shop.mtcoding.blog.reply.Reply;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BoardResponse {

    @Data
    public static class DTO {
        private List<Board> boards;
        private Integer prev;
        private Integer next;
        private boolean isFirst;
        private boolean isLast;

        public DTO(List<Board> boards, Integer prev, Integer next) {
            this.boards = boards;
            this.prev = prev;
            this.next = next;
        }
    }

    // 깊은 복사
    @Data
    public static class DetailDTO {
        private Integer id;
        private String title;
        private String content;
        private Boolean isPublic;
        private Boolean isOwner;  // 대문자는 값을 안 넣으면 null, 소문자는 false
        private Boolean isLove;
        private Integer loveCount;
        private String username;
        private Timestamp createdAt;
        private Integer loveId;

        private List<ReplyDTO> replies;

        @Data
        // DetailDTO안에 있기 때문에 외부 클래스가 아닌 내부클래스
        public class ReplyDTO {
            private Integer id;
            private String content;
            private String username;
            private Boolean isOwner;

            public ReplyDTO(Reply reply, Integer sessionUserId) {
                this.id = reply.getId();
                this.content = reply.getContent();
                this.username = reply.getUser().getUsername();
                this.isOwner = reply.getUser().getId().equals(sessionUserId);
            }
        }


        // 템플릿엔진이 조건문 비교를 허용해주지 않기 때문에 필요함
        public DetailDTO(Board board, Integer sessionUserId, Boolean isLove, Integer loveCount, Integer loveId) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.isPublic = board.getIsPublic();
            this.isOwner = sessionUserId == board.getUser().getId();   // 같으면 true 같지 않으면 false
            this.username = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt();
            this.isLove = isLove;
            this.loveCount = loveCount;
            this.loveId = loveId;

            List<ReplyDTO> repliesDTO = new ArrayList<>();
            for (Reply reply : board.getReplies()) {
                ReplyDTO replyDTO = new ReplyDTO(reply, sessionUserId);
                repliesDTO.add(replyDTO);
            }
            this.replies = repliesDTO;
        }
    }
}

