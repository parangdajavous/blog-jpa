package shop.mtcoding.blog.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.mtcoding.blog._core.util.Resp;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;
    private final HttpSession session;

    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO joinDTO) {
        userService.회원가입(joinDTO);
        return "redirect:/login-form";
    }

    @GetMapping("/api/check-username-available/{username}")
    public @ResponseBody Resp<?> checkUsernameAvailable(@PathVariable("username") String username) {
        Map<String, Object> dto = userService.유저네임중복체크(username);
        return Resp.ok(dto);
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }

    @PostMapping("/login")
    public String login(UserRequest.LoginDTO loginDTO, HttpServletResponse response) {
        User sessionUser = userService.로그인(loginDTO);
        session.setAttribute("sessionUser", sessionUser);

        if (loginDTO.getRememberMe() == null) {
            Cookie cookie = new Cookie("username", null);
            cookie.setMaxAge(0); // 즉시 삭제
            response.addCookie(cookie);

        } else {
            Cookie cookie = new Cookie("username", loginDTO.getUsername());
            cookie.setMaxAge(60 * 60 * 24 * 7);  // cookie 하루동안 유지
            response.addCookie(cookie);
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/login-form";
    }


    @GetMapping("/user/update-form")
    public String updateForm() {

        // ViewResolver -> prefix = /templates/  suffix = .mustache
        return "user/update-form";
    }

    @PostMapping("/user/update")
    public String update(UserRequest.UpdateDTO updateDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        User user = userService.회원정보수정(updateDTO, sessionUser.getId());

        // session 동기화 -> 동기화 안해주면 바꾸기 전 정보를 보게 된다
        session.setAttribute("sessionUser", user);

        return "redirect:/";

    }
}
