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
//        User user = userRepository.findByUsername(joinDTO.getUsername());
//        if (user != null) {
//            throw new RuntimeException("동일한 username이 존재합니다.");
//        }
        User user = joinDTO.toEntity();  // 1. 비영속객체
        System.out.println("비영속 user: " + user.getId());
        // 회원가입
        userRepository.save(user);
        // user 객체
        System.out.println("영속/동기화 user: " + user.getId());

        // TODO: 정리하기
//        System.out.println("===============================");
//        userRepository.findById(3);  // pc에서 찾는다 -> 캐싱
//        System.out.println("===============================");
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
}
