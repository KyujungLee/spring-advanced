package org.example.expert.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class PasswordEncoderTest {

    @InjectMocks
    private PasswordEncoder passwordEncoder;

    @Test
    void matches_메서드가_정상적으로_동작한다() {
        // given
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // when
        /**
         * Lv.3-1 PasswordEncoder 의 matches 매서드 매개변수 순서가 틀림.(raw가 첫번째, 인코딩된 것이 두번째)
         */
        boolean matches = passwordEncoder.matches(encodedPassword, rawPassword);

        // then
        /**
         * Lv.3-1 테스트 결과 false 확인
         */
        assertFalse(matches);
    }
}
