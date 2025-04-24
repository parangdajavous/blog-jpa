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
        private Integer current;
        private Integer size; // 3
        private Integer totalCount;
        private Integer totalPage;
        private boolean isFirst; // current  == 0
        private boolean isLast; // 다음페이지에서 못 넘어가게 계산 필요 -> totalCount, size = 3  totalPages == current
        private List<Integer> numbers;
        private String keyword;

        public DTO(List<Board> boards, Integer current, Integer totalCount, String keyword) {
            this.boards = boards;
            this.prev = current - 1;
            this.next = current + 1;
            this.size = 3;  // 보통은 final로 따로 빼서 씀 - 그래야 수정이 적어진다
            this.totalCount = totalCount;  // given 으로 처리 후 따로 연산(given으로 test 먼저 필요)  -> test 끝나면 DB에서 들고옴

            this.totalPage = makeTotalPage(totalCount, size);

            this.isFirst = current == 0;
            this.isLast = (totalPage - 1) == current;  // totalPages는 1부터 시작하는데 current는 0부터 시작하니까 totalPages-1 필요
            System.out.println("isLast: " + isLast);
            this.numbers = makeNumbers(current, totalPage);
            this.keyword = keyword;  // null이거나 값이 있거나
        }

        // page 계산 함수
        private Integer makeTotalPage(int totalCount, int size) {
            int rest = totalCount % size > 0 ? 1 : 0;  // 나머지 -> 5 / 3 = 나머지 2 , 6 / 3 = 나머지 0   // 나머지가 0이 아니면 rest = 1을 page에 더함
            return totalCount / size + rest;  // 전체 페이지
        }

        private List<Integer> makeNumbers(int current, int totalPage) {
            List<Integer> numbers = new ArrayList<>();

            int start = (current / 5) * 5;
            int end = Math.min(start + 5, totalPage);

            for (int i = start; i < end; i++) {
                numbers.add(i);
            }

            return numbers;
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

