package shop.mtcoding.blog._core.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Resp<T> {
    private Integer status;
    private String msg;
    private T body;


    public static <T> Resp<?> ok(T body) {
        return new Resp<>(200, "성공", body);
    }

    public static <T> Resp<?> fail(Integer status, String msg) {
        return new Resp(status, msg, null);
    }
}