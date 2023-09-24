package com.sopterm.makeawish.config;

import com.sopterm.makeawish.common.Util;
import com.sopterm.makeawish.domain.user.AccountInfo;
import com.sopterm.makeawish.domain.user.SocialType;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.repository.UserRepository;
import com.sopterm.makeawish.repository.wish.WishRepository;
import com.sopterm.makeawish.service.CakeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@SpringBootTest
public class CacheTest {
    @Autowired
    CacheManager cacheManager;

    @Autowired
    CakeService cakeService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WishRepository wishRepository;

    @Test
    @Transactional
    @DisplayName("로컬 캐시에 있으면 캐시에서 값을 가져온다.")
    void 로컬_캐시_가져오기_성공() {
        // given
        User user = userRepository.save(createUser());
        Wish wish = wishRepository.save(createWish(user));

        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            cakeService.getPresents(user.getId(), wish.getId());
            long end = System.currentTimeMillis();
            System.out.println("time = " + (end - start));
        }
    }

    private String getLocalDateTime(int plusDays) {
        LocalDateTime date = LocalDateTime.now().plusDays(plusDays);
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss+00:00"));
    }


    private User createUser() {
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

    private Wish createWish(User user) {
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
