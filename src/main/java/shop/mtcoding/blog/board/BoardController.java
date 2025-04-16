package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.mtcoding.blog.user.User;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;
    private final HttpSession session;


    @GetMapping("/")
    public String list(HttpServletRequest request, @RequestParam(required = false, value = "page", defaultValue = "0") Integer page) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            request.setAttribute("model", boardService.글목록보기(null, page));
        } else {
            request.setAttribute("model", boardService.글목록보기(sessionUser.getId(), page));
        }

        return "board/list";
    }

    @PostMapping("/board/save")
    public String save(@Valid BoardRequest.SaveDTO saveDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        boardService.글쓰기(saveDTO, sessionUser);

        return "redirect:/";
    }

    @GetMapping("/board/save-form")
    public String saveForm() {
        return "board/save-form";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable("id") Integer id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 비로그인 시 상세보기
        Integer sessionUserId = (sessionUser == null ? null : sessionUser.getId());

        BoardResponse.DetailDTO detailDTO = boardService.글상세보기(id, sessionUserId);
        request.setAttribute("model", detailDTO);
        return "board/detail";
    }

    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable("id") Integer id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        Board board = boardService.업데이트글보기(id, sessionUser.getId());
        request.setAttribute("model", board);
        return "board/update-form";
    }

    @PostMapping("/board/{id}/update")
    public String update(@PathVariable("id") Integer id, @Valid BoardRequest.UpdateDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        boardService.게시글수정(reqDTO, id, sessionUser.getId());

        return "redirect:/board/" + id;
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        boardService.게시글삭제(id, sessionUser.getId());
        return "redirect:/";
    }
}
