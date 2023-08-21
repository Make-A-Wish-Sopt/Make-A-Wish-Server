package com.sopterm.makeawish.service;

import com.sopterm.makeawish.common.Util;
import com.sopterm.makeawish.domain.Cake;
import com.sopterm.makeawish.domain.user.AccountInfo;
import com.sopterm.makeawish.domain.user.SocialType;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.dto.cake.CakeReadyRequestDTO;
import com.sopterm.makeawish.repository.CakeRepository;
import com.sopterm.makeawish.repository.UserRepository;
import com.sopterm.makeawish.repository.wish.WishRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Autowired
    WishRepository wishRepository;

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
    @DisplayName("똥케이크로 카카오페이 연결 준비 시 예외 발생")
    public void 카카오페이_준비_연결_실패() {
        // given
        CakeReadyRequestDTO request = new CakeReadyRequestDTO("partnerOrderId", "partnerUserId", 1L, "0", "200", "https://www.sunmulzu.shop/", "https://www.sunmulzu.shop/", "https://www.sunmulzu.shop/");

        // when - then
        assertThatThrownBy(() -> cakeService.getKakaoPayReady(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격 있는 케이크로 카카오페이 연결 준비 시 연결 성공")
    public void 카카오페이_준비_연결_성공() {
        // given
        CakeReadyRequestDTO request = new CakeReadyRequestDTO("partnerOrderId", "partnerUserId", 2L, "0", "200", "https://www.sunmulzu.shop/", "https://www.sunmulzu.shop/", "https://www.sunmulzu.shop/");

        // when
        ThrowableAssert.ThrowingCallable doNothing = () -> {
            cakeService.getKakaoPayReady(request);
        };

        // then
        assertThatCode(doNothing).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("똥케이크 선물 시 가격 변동 X")
    public void 똥케이크_선물한_경우() {
        // given
        User user = userRepository.save(createUser());
        Wish wish = wishRepository.save(createWish(user));
        Cake cake = cakeService.getCake(1L);
        int prevTotalPrice = wish.getTotalPrice();

        // when
        cakeService.createPresent("최아무", cake, wish, "선물 메세지");

        //then
        assertThat(prevTotalPrice).isEqualTo(wishService.getWish(wish.getId()).getTotalPrice());
    }

    @Test
    @DisplayName("가격있는 케이크를 선물 시 가격 변동 O")
    public void 그외_케이크_선물한_경우() {
        // given
        User user = userRepository.save(createUser());
        Wish wish = wishRepository.save(createWish(user));
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


    private User createUser(){
        AccountInfo accountInfo = new AccountInfo("김아무", "bank", "account");
        return User.builder()
                .email("kim@email.com")
                .socialType(SocialType.KAKAO)
                .socialId("12345")
                .nickname("김아무")
                .createdAt(LocalDateTime.now())
                .account(accountInfo)
                .build();
    }

    private Wish createWish(User user){
        return Wish.builder()
                .wisher(user)
                .presentImageUrl("image-url")
                .title("소원 제목")
                .hint("소원 힌트")
                .initial("ㅅㅇ ㅈㅁ")
                .presentPrice(50000)
                .startAt(Util.convertToDate(getLocalDateTime(0)))
                .endAt(Util.convertToDate(getLocalDateTime(7)))
                .build();
    }
}
