package com.sopterm.makeawish.service;

import com.sopterm.makeawish.domain.Cake;
import com.sopterm.makeawish.domain.user.SocialType;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.dto.auth.AuthSignInRequestDTO;
import com.sopterm.makeawish.dto.cake.CakeReadyRequestDTO;
import com.sopterm.makeawish.dto.wish.WishRequestDTO;
import com.sopterm.makeawish.repository.CakeRepository;
import com.sopterm.makeawish.repository.UserRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;



@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CakeServiceTest {
    @Autowired
    WishService wishService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CakeService cakeService;

    @Autowired
    CakeRepository cakeRepository;

    @BeforeAll
    void 케이크_세팅() {
        cakeRepository.save(new Cake(1L, "케이크1", 0, "image1"));
        cakeRepository.save(new Cake(2L, "케이크2", 4900, "image2"));
        cakeRepository.save(new Cake(3L, "케이크3", 14900, "image3"));
        cakeRepository.save(new Cake(4L, "케이크4", 19900, "image4"));
        cakeRepository.save(new Cake(5L, "케이크5", 25900, "image5"));
        cakeRepository.save(new Cake(6L, "케이크6", 359000, "image6"));
        cakeRepository.save(new Cake(7L, "케이크7", 47900, "image7"));
    }

    @Test
    public void 카카오페이_준비_연결_실패() {
        // given
        CakeReadyRequestDTO request = new CakeReadyRequestDTO("partnerOrderId", "partnerUserId", 1L, "0", "200", "http://localhost:8080", "http://localhost:8080", "http://localhost:8080");

        // when - then
        assertThatThrownBy(() -> cakeService.getKakaoPayReady(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 카카오페이_준비_연결_성공() {
        // given
        CakeReadyRequestDTO request = new CakeReadyRequestDTO("partnerOrderId", "partnerUserId", 2L, "0", "200", "http://localhost:8080", "http://localhost:8080", "http://localhost:8080");

        // when
        ThrowableAssert.ThrowingCallable doNothing = () -> {
            cakeService.getKakaoPayReady(request);
        };

        // then
        assertThatCode(doNothing).doesNotThrowAnyException();
    }

    @Test
    public void 똥케이크_선물한_경우() {
        // given
        AuthSignInRequestDTO userRequest = new AuthSignInRequestDTO("kim@email.com", SocialType.KAKAO, "12345", "김아무", LocalDateTime.now());
        User user = userRepository.save(new User(userRequest));

        WishRequestDTO wishRequest = new WishRequestDTO("imageUrl", 50000, "소원 제목", "소원 힌트", "ㅅㅇ ㅈㅁ", getLocalDateTime(0), getLocalDateTime(7), "01012345678");
        Long wishId = wishService.createWish(user.getId(), wishRequest);
        Wish wish = wishService.getWish(wishId);
        Cake cake = cakeService.getCake(1L);
        int prevTotalPrice = wish.getTotalPrice();

        // when
        cakeService.createPresent("최아무", cake, wish, "선물 메세지");

        //then
        assertThat(prevTotalPrice).isEqualTo(wishService.getWish(wish.getId()).getTotalPrice());
    }

    @Test
    public void 그외_케이크_선물한_경우() {
        // given
        AuthSignInRequestDTO userRequest = new AuthSignInRequestDTO("kim@email.com", SocialType.KAKAO, "12345", "김아무", LocalDateTime.now());
        User user = userRepository.save(new User(userRequest));

        WishRequestDTO wishRequest = new WishRequestDTO("imageUrl", 50000, "소원 제목", "소원 힌트", "ㅅㅇ ㅈㅁ", getLocalDateTime(0), getLocalDateTime(7), "01012345678");
        Long wishId = wishService.createWish(user.getId(), wishRequest);
        Wish wish = wishService.getWish(wishId);
        Cake cake = cakeService.getCake(5L);
        int prevTotalPrice = wish.getTotalPrice();

        // when
        cakeService.createPresent("최아무", cake, wish, "선물 메세지");

        //then
        assertThat(prevTotalPrice).isEqualTo(wishService.getWish(wish.getId()).getTotalPrice() - cake.getPrice());
    }

    private String getLocalDateTime(int plusDays){
        LocalDateTime date = LocalDateTime.now().plusDays(plusDays);
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss+00:00"));
    }
}
