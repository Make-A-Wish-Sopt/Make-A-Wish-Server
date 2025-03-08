package com.sopterm.makeawish.service;

import com.sopterm.makeawish.domain.TmpBirthday;
import com.sopterm.makeawish.dto.alarm.TmpBirthdayDto;
import com.sopterm.makeawish.repository.TmpBirthdayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final TmpBirthdayRepository tmpBirthdayRepository;

    public void createBirthDayAlarm(TmpBirthdayDto request){
        tmpBirthdayRepository.save(new TmpBirthday(request));
    }
}
