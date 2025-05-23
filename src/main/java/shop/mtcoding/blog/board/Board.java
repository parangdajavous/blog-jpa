package shop.mtcoding.blog.board;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.reply.Reply;
import shop.mtcoding.blog.user.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter
@Table(name = "board_tb")
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;
    private Boolean isPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;  // ORM

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    // board = one , reply = many , mappedBy -> FK의 주인 @OneToMany -> 조회용도
    private List<Reply> replies = new ArrayList<>();  //조회할 때만 넣기위함

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public Board(Integer id, String title, String content, Boolean isPublic, User user, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
        this.user = user;
        this.createdAt = createdAt;
    }

    // 게시글 수정 Setter
    public void update(String title, String content, String isPublic) {
        this.title = title;
        this.content = content;
        this.isPublic = isPublic == null ? false : true;

    }

}