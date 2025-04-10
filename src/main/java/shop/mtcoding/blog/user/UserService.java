package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.error.ex.Exception400;
import shop.mtcoding.blog._core.error.ex.Exception401;
import shop.mtcoding.blog._core.error.ex.Exception404;

import java.util.HashMap;
import java.util.Map;

// 비즈니스 로직, 트랜잭션 처리, DTO 완료
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    // 401 > 인증 403 > 권한 404 > 자원 못찾음

    @Transactional
    public void 회원가입(UserRequest.JoinDTO joinDTO) {
        // 동일회원 있는지 검사
        try {
            userRepository.save(joinDTO.toEntity());
        } catch (Exception e) {
            //Exceptiom 400 (Bad request -> 잘못된 요청입니다)
            throw new Exception400("동일한 username 이 존재합니다.");
        }

        // 회원가입
        userRepository.save(joinDTO.toEntity());
    }

    public User 로그인(UserRequest.LoginDTO loginDTO) {
        // username,password 검사
        User user = userRepository.findByUsername(loginDTO.getUsername());
        if (user == null) throw new Exception401("username 혹은 password 가 일치하지 않습니다");

        if (!user.getPassword().equals(loginDTO.getPassword())) {
            throw new Exception401("username 혹은 password 가 일치하지 않습니다");
        }

        // 로그인
        return user;
    }

    public Map<String, Object> 유저네임중복체크(String username) {
        User user = userRepository.findByUsername(username);
        Map<String, Object> dto = new HashMap<>();

        if (user == null) {
            dto.put("available", true);
        } else {
            dto.put("available", false);
        }
        return dto;
    }

    @Transactional
    public User 회원정보수정(UserRequest.UpdateDTO updateDTO, Integer userId) {
        User user = userRepository.findById(userId);

        //Exception404
        if (user == null) throw new Exception404("회원을 찾을 수 없습니다");

        user.update(updateDTO.getPassword(), updateDTO.getEmail());  // 영속화된 객체의 상태변경
        return user;
    }  // 함수 종료될 때 더티 체킹(Dirty Checking) -> 상태가 변경되면 변경된 data를 가지고 update을 한다
}
