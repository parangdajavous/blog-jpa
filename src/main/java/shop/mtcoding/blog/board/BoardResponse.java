package shop.mtcoding.blog.board;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

public class BoardResponse {

    // 깊은 복사
    @AllArgsConstructor
    @Data
    public static class DetailDTO {
        private Integer id;
        private String title;
        private String content;
        private Boolean isPublic;
        private Boolean isOwner;  // 대문자는 값을 안 넣으면 null, 소문자는 false
        private String username;
        private Timestamp createdAt;
        private Long loveCount;  // 그룹함수로 리턴되는 숫자는 Long 타입
        private Boolean isLove;


    }
}
