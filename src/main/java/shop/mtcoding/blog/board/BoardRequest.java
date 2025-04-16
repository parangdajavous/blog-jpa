package shop.mtcoding.blog.board;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import shop.mtcoding.blog.user.User;

public class BoardRequest {

    @Data
    public static class SaveDTO {
        // title=제목1&content=내용1 -> isPublic은 null이다
        // title=제목1&content=내용1&isPublic -> isPublic은 ""(빈문자열)이다
        // title=제목1&content=내용1&isPublic=(공백)  -> isPublic은 space(공백이 들어간 문자열)이다
        @NotEmpty(message = "제목을 입력하세요")  // null, space(공백이 들어간 문자열), ""(빈 문자열) 불가
        private String title;
        @NotEmpty(message = "내용을 입력하세요")
        private String content;
        @NotEmpty
        private String isPublic;

        public Board toEntity(User user) {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .isPublic(isPublic == null ? false : true)
                    .user(user)   // user 객체 필요
                    .build();
        }

    }

    @Data
    public static class UpdateDTO {
        private String title;
        private String content;
        private String isPublic;


    }
}
