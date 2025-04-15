package shop.mtcoding.blog.temp;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

// https://regex101.com
public class RegexTest {

    @Test
    public void 한글만된다_test() {
        String value = "ㅏㅏㅑㅇㅎㄹ";
        boolean result = Pattern.matches("^[가-힣ㄱ-ㅎㅏ-ㅣ]+$", value);
        System.out.println("테스트 : " + result);
    }


    @Test
    public void 한글은안된다_test() throws Exception {
        String value = "$86..ssa";
        // String value = "$86..ssa";
        boolean result = Pattern.matches("^[^가-힣ㄱ-ㅎㅏ-ㅣ]+$", value);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void 영어만된다_test() throws Exception {
        String value = "ssar#";
        // String value = "ssar2";
        boolean result = Pattern.matches("^[a-zA-Z]+$", value);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void 영어는안된다_test() throws Exception {
        String value = "SAss";
        // String value = "ssar";
        boolean result = Pattern.matches("^[^a-zA-Z]+$", value);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void 영어와숫자만된다_test() throws Exception {
        String value = "ssar2@";
        // String value = "ssar2&";
        // String value = "ssar한글";
        boolean result = Pattern.matches("^[a-zA-Z0-9]+$", value);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void 영어만되고_길이는최소2최대4이다_test() throws Exception {
        String value = "ssar";
        // String value = "ssarm";
        boolean result = Pattern.matches("^[a-zA-Z]{2,4}$", value);
        System.out.println("테스트 : " + result);
    }

    // 소문자, 대문자, 숫자, 특수문자가 포함되어야 하고, 최소 2자부터 20자 사이여야 한다
    @Test
    public void password_test() {
        String password = "2#Ed   ";
        boolean result = Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>?/`~\\\\])[a-zA-Z0-9!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>?/`~\\\\]{2,20}$", password);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void user_username_test() throws Exception {
        String username = "";
        // String username = "ssa^";
        boolean result = Pattern.matches("^[a-zA-Z0-9]{2,20}$", username);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void user_email_test() throws Exception {
        String email = "s...sd@co.kr";
        // String username = "@fGf.ccm"; // +를 *로 변경해보기
        boolean result = Pattern.matches("^[a-zA-Z0-9.]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$", email);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void user_fullname_test() throws Exception {
        String fullname = "코스";
        // String fullname = "코스ss1";
        boolean result = Pattern.matches("^[a-zA-Z가-힣]{1,20}$", fullname);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void account_gubun_test() throws Exception {
        String gubun = "TRANSFER"; // WITHDRAW(8), DEPOSIT(7), TRANSFER(8)

        boolean result = Pattern.matches("^(WITHDRAW|DEPOSIT|TRANSFER)$", gubun);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void account_gubun_test2() throws Exception {
        String gubun = "TRANSFER"; // WITHDRAW(8), DEPOSIT(7), TRANSFER(8)

        boolean result = Pattern.matches("^(TRANSFER)$", gubun);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void account_tel_test() throws Exception {
        String tel = "0102227777";

        boolean result = Pattern.matches("^[0-9]{3}[0-9]{4}[0-9]{4}$", tel);
        System.out.println("테스트 : " + result);
    }
}