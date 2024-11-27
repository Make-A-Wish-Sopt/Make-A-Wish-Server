package com.sopterm.makeawish.domain.user;

import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.dto.auth.AuthSignInRequestDTO;
import com.sopterm.makeawish.dto.user.UserAccountRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
public class User {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 20, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @Column
    private String email;

    @Column
    private String nickname;

    @Column(unique = true)
    private String socialId;

    private String refreshToken;

    private String image;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Embedded
    private TransferInfo transferInfo;

    @OneToMany(mappedBy = "wisher")
    private final List<Wish> wishes = new ArrayList<>();

    @Builder
    public User(AuthSignInRequestDTO authSignInRequestDto) {
        this.email = authSignInRequestDto.email();
        this.socialType = authSignInRequestDto.socialType();
        this.socialId = authSignInRequestDto.socialId();
        this.nickname = authSignInRequestDto.nickname();
        this.createdAt = authSignInRequestDto.createdAt();
        val account = new AccountInfo(null, null, null);
        this.transferInfo = new TransferInfo(account, null, false);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateProfile(TransferInfo userTransferInfo) {
        updateTransferInfo(userTransferInfo);
    }

    public void updateTransferInfo(TransferInfo transferInfo){
        this.transferInfo.updateTransferInfo(transferInfo.getAccountInfo(), transferInfo.getKakaoPayCode(), transferInfo.isForPayCode());
    }
}