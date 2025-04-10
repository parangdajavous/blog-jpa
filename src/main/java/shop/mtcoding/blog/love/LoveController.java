package shop.mtcoding.blog.love;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.Resp;
import shop.mtcoding.blog.user.User;

@RequiredArgsConstructor
@RestController   // 파일이 아닌 데이터 리턴하는 컨트롤러 (Ajax)
public class LoveController {
    private final LoveService loveService;
    private final HttpSession session;

    @PostMapping("/love")
    public Resp<?> saveLove(@RequestBody LoveRequest.SaveDTO reqDTO) {
        // 인증로직
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("인증이 필요합니다.");

        LoveResponse.SaveDTO respDTO = loveService.좋아요(reqDTO, sessionUser.getId());

        return Resp.ok(respDTO);
    }

    @DeleteMapping("/love/{id}")
    public Resp<?> deleteLove(@PathVariable("id") Integer id) {
        // 인증로직
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("인증이 필요합니다.");

        LoveResponse.DeleteDTO respDTO = loveService.좋아요취소(id, sessionUser.getId());   // loveId

        return Resp.ok(respDTO);
    }
}
