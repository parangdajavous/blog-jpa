package shop.mtcoding.blog._core.error;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import shop.mtcoding.blog._core.error.ex.Exception400;

import java.util.List;

// Aspect, PointCut, Advice
@Aspect  // 관점관리
@Component
public class GlobalValidationHandler {

    // 관심사를 분리시킴 -> AOP
    // PostMapping,PutMapping 이 들어있는 메서드를 실행하기 직전에 Advice 를 호출하라
    @Before("@annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping)")
    // pointcut
    public void badRequestAdvice(JoinPoint jp) {  // 대리인  / jp는 실제 실행될 메서드의 모든 것을 투영하고 있다(리플렉션)
        Object[] args = jp.getArgs();  // 메서드의 매개변수들 (매개변수의 갯수와 상관없이 배열로 return)
        for (Object arg : args) {  // 매개변수의 갯수만큼 반복 (annotation은 매개변수의 갯수에 포함되지 않는다)

            // Errors 타입이 매개변수에 존재하고,
            if (arg instanceof Errors) {  // instanceof -> 타입검증 (다형성을 만족한다)
                System.out.println("error 400 처리 필요함");
                Errors errors = (Errors) arg;

                // 실제 에러가 존재한다면
                // 공통모듈 -> 관심사 분리
                if (errors.hasErrors()) {
                    List<FieldError> fErrors = errors.getFieldErrors();

                    for (FieldError fieldError : fErrors) {
                        throw new Exception400(fieldError.getField() + ":" + fieldError.getDefaultMessage());
                    }

                }
            }
        }
    }

}
