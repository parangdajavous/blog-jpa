package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void 회원가입(UserRequest.JoinDTO joinDTO) {
        // 동일회원 있는지 검사
        User user = userRepository.findByUsername(joinDTO.getUsername());
        if (user != null) {
            throw new RuntimeException("동일한 username이 존재합니다.");
        }

        // 회원가입
        userRepository.save(joinDTO.getUsername(), joinDTO.getPassword(), joinDTO.getEmail());
    }

    public User 로그인(UserRequest.LoginDTO loginDTO) {
        // username,password 검사
        User user = userRepository.findByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new RuntimeException("해당 username이 없습니다");
        }
        if (!(user.getPassword().equals(loginDTO.getPassword()))) {
        }

        // 로그인
        return user;
    }
}
