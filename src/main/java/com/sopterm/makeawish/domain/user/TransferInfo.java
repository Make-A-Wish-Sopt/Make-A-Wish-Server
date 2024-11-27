package com.sopterm.makeawish.domain.user;

import com.sopterm.makeawish.dto.user.UserAccountRequestDTO;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class TransferInfo {
    private AccountInfo accountInfo;
    private String kakaoPayCode;
    private boolean forPayCode;

    public void updateTransferInfo(AccountInfo accountInfo, String kakaoPayCode, boolean forPayCode){
        this.accountInfo = accountInfo;
        this.kakaoPayCode = kakaoPayCode;
        this.forPayCode = forPayCode;
    }

    @Builder
    public TransferInfo(UserAccountRequestDTO userAccountRequestDTO){
        this.accountInfo = userAccountRequestDTO.accountInfo();
        this.forPayCode = userAccountRequestDTO.forPayCode();
        this.kakaoPayCode = userAccountRequestDTO.kakaoPayCode();
    }
}
