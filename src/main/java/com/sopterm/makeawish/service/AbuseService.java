package com.sopterm.makeawish.service;

import com.sopterm.makeawish.common.AbuseException;
import com.sopterm.makeawish.domain.abuse.AbuseLog;
import com.sopterm.makeawish.domain.abuse.AbuseUser;
import com.sopterm.makeawish.repository.abuse.AbuseLogRepository;
import com.sopterm.makeawish.repository.abuse.AbuseUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sopterm.makeawish.common.message.ErrorMessage.IS_ABUSE_USER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AbuseService {
    private final AbuseLogRepository abuseLogRepository;
    private final AbuseUserRepository abuseUserRepository;
    private final UserService userService;

    private static final int ABUSE_CAUTION_COUNT = 4;

    @Transactional
    public void createAbuseUser(Long userId) {
        abuseUserRepository.save(new AbuseUser(userService.getUser(userId)));
    }

    public void checkAbuseUser(Long userId) {
        abuseUserRepository.findAbuseUserByUserId(userId)
                .ifPresent(abuseUser -> {
                    throw new AbuseException(IS_ABUSE_USER.getMessage());
                });
    }

    @Transactional
    public Integer countAbuseLogByUser(Long userId) {
        val abuseLogCount = abuseLogRepository.countAbuseLogByUserIdDuringWeekend(userId);
        if (abuseLogCount >= ABUSE_CAUTION_COUNT) {
            createAbuseUser(userId);
        }
        return abuseLogCount;
    }

    public void createAbuseLog(AbuseLog abuseLog){
        abuseLogRepository.save(abuseLog);
    }
}
