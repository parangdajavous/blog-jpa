package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

// 비즈니스 로직, 트랜잭션 처리, DTO 완료
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void 회원가입(UserRequest.JoinDTO joinDTO) {
        // 동일회원 있는지 검사
//        User user = userRepository.findByUsername(joinDTO.getUsername());
//        if (user != null) {
//            throw new RuntimeException("동일한 username이 존재합니다.");
//        }
        // 회원가입
        userRepository.save(joinDTO.toEntity());
    }

    public User 로그인(UserRequest.LoginDTO loginDTO) {
        // username,password 검사
        User user = userRepository.findByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new RuntimeException("해당 username이 없습니다");
        }
        if (!(user.getPassword().equals(loginDTO.getPassword()))) {
            throw new RuntimeException("해당 passward가 일치하지 않습니다");
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
}
