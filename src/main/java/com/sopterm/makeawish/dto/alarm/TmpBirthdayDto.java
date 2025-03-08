package com.sopterm.makeawish.dto.alarm;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;

@Getter
public record TmpBirthdayDto(
        @Size(min = 4, max = 4)
        @NonNull
        String birthDate,
        @NonNull
        String phoneNumber
) {
}
