package shop.mtcoding.blog._core.error;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class GlobalValidationHandler {  // GlobalValidationHandler -> 관점에 따라 다르게 행동할 Object

    // 직전
    @Before("@annotation(shop.mtcoding.blog._core.error.anno.MyBefore)")
    public void beforeAdvice(JoinPoint jp) {  // 리플렉션된 메서드의 정보가 jp에 담김
        String name = jp.getSignature().getName();
        System.out.println("Before Advice : " + name);
    }

    // 직후
    @After("@annotation(shop.mtcoding.blog._core.error.anno.MyAfter)")
    public void afterAdvice(JoinPoint jp) {
        String name = jp.getSignature().getName();
        System.out.println("After Advice : " + name);
    }

    // 앞뒤 (프록시)
    @Around("@annotation(shop.mtcoding.blog._core.error.anno.MyAround)")
    public Object aroundAdvice(ProceedingJoinPoint jp) {
        //before
        String name = jp.getSignature().getName();
        System.out.println("Around Advice 직전 : " + name);

        try {
            //after
            Object result = jp.proceed();   // 컨트롤러 함수가 호출됨. 타입을 알수없기 때문에 Object 타입
            System.out.println("Around Advice 직후 : " + name);
            System.out.println("result 값 : " + result);
            return result;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }

}
