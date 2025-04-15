package shop.mtcoding.blog.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.mtcoding.blog._core.error.ex.Exception400;
import shop.mtcoding.blog._core.util.Resp;

import java.util.List;
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
    public String join(@Valid UserRequest.JoinDTO joinDTO, Errors errors) {  // @Valid 가 붙으면 invoke 하기 전에 리플렉션 타고 DTO 내부의 어노테이션을 확인해서 터지면 errors에 넣어준다

        // 부가로직 (유효성 검사) - 공통모듈 / 공통모듈화가 되어야지 함수화해서 재활용 가능
        if (errors.hasErrors()) {
            List<FieldError> fErrors = errors.getFieldErrors();

            for (FieldError fieldError : fErrors) {
                throw new Exception400(fieldError.getField() + ":" + fieldError.getDefaultMessage());
            }

        }


        // 유효성 검사
//        boolean r1 = Pattern.matches("^[a-zA-Z0-9]{2,20}$", joinDTO.getUsername());
//        boolean r2 = Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()])[a-zA-Z\\d!@#$%^&*()]{6,20}$", joinDTO.getPassword());
//        boolean r3 = Pattern.matches("^[a-zA-Z0-9.]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$", joinDTO.getEmail());
//
//        if (!r1) throw new Exception400("유저네임은 2-20자이며, 특수문자,한글이 포함될 수 없습니다");
//        if (!r2) throw new Exception400("패스워드는 4-20자이며, 특수문자,영어 대문자,소문자, 숫자가 포함되어야 하며, 공백이 있을 수 없습니다");
//        if (!r3) throw new Exception400("이메일 형식에 맞게 적어주세요");

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
    public String login(@Valid UserRequest.LoginDTO loginDTO, Errors errors, HttpServletResponse response) {  // 어노테이션 붙은 매개변수 옆에 Errors가 있어야 DTO에서 터지면 errors에 넣어줌. 그 사이에 다른 매개변수가 있으면 그 매개변수를 확인하기 때문에 자리 잘 확인하기

        // 부가로직(유효성 검사) - 공통모듈 / 공통모듈화가 되어야지 함수화해서 재활용 가능
        if (errors.hasErrors()) {
            List<FieldError> fErrors = errors.getFieldErrors();

            for (FieldError fieldError : fErrors) {
                throw new Exception400(fieldError.getField() + ":" + fieldError.getDefaultMessage());
            }

        }

        // 핵심로직 -> seesionUser ~  cookie
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
