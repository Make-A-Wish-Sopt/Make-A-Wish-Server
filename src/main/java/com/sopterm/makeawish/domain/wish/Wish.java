package com.sopterm.makeawish.domain.wish;

import com.sopterm.makeawish.domain.Present;
import com.sopterm.makeawish.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.Objects.nonNull;

@Entity
@Getter
@NoArgsConstructor
public class Wish {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "wish_id")
    private Long id;

    private String title;

    private String presentImageUrl;

    @Column(columnDefinition = "TEXT")
    private String hint1;

    private String hint2;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @Embedded
    private AccountInfo account;

    private String phoneNumber;

    private int presentPrice;

    private int totalPrice;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User wisher;

    @OneToMany(mappedBy = "wish")
    private final List<Present> presents = new ArrayList<>();

    @Builder
    public Wish(String title, String presentImageUrl, String hint1, String hint2, LocalDateTime startAt,
                LocalDateTime endAt, AccountInfo account, String phoneNumber, int presentPrice, User wisher) {
        this.title = title;
        this.presentImageUrl = presentImageUrl;
        this.hint1 = hint1;
        this.hint2 = hint2;
        this.startAt = startAt;
        this.endAt = endAt;
        this.account = account;
        this.phoneNumber = phoneNumber;
        this.presentPrice = presentPrice;
        this.totalPrice = 0;
        setUser(wisher);
    }

    private void setUser(User user) {
        if (nonNull(this.wisher)) {
            this.wisher.getWishes().remove(this);
        }
        this.wisher = user;
        user.getWishes().add(this);
    }

    public void updateTotalPrice(int price) {
        this.totalPrice += price;
    }

    @Builder
    public void updateWish(LocalDateTime startAt, LocalDateTime endAt, String name, String bank, String account, String phoneNumber) {
        this.startAt = startAt == null ? this.startAt : startAt;
        this.endAt = endAt == null ? this.endAt : endAt;
        this.account = updateAccount(name,bank,account);
        this.phoneNumber = phoneNumber == null ? this.phoneNumber : phoneNumber;
    }

    private AccountInfo updateAccount(String name, String bank, String account) {
        if(name == null) name = this.account.getName();
        if(bank == null) bank = this.account.getBank();
        if(account == null) account = this.account.getAccount();
        return new AccountInfo(name,bank,account);
    }
}
