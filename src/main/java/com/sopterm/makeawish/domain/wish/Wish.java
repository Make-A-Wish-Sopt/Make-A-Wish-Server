package com.sopterm.makeawish.domain.wish;

import com.sopterm.makeawish.domain.BaseEntity;
import com.sopterm.makeawish.domain.Present;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.dto.wish.UserWishUpdateRequestDTO;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.sopterm.makeawish.domain.wish.WishStatus.*;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.Objects.nonNull;

@Entity
@Getter
@NoArgsConstructor
public class Wish extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "wish_id")
    private Long id;

    private String title;

    private String presentImageUrl;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private int totalPrice;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User wisher;

    @OneToMany(mappedBy = "wish")
    private final List<Present> presents = new ArrayList<>();

    private boolean wantsGift;

    @Builder
    public Wish(String title, String presentImageUrl, LocalDateTime startAt,
                LocalDateTime endAt, User wisher, boolean wantsGift) {
        this.title = title;
        this.presentImageUrl = presentImageUrl;
        this.startAt = startAt;
        this.endAt = endAt;
        this.totalPrice = 0;
        this.wantsGift = wantsGift;
        setWisher(wisher);
    }

    private void setWisher(User user) {
        if (nonNull(this.wisher)) {
            this.wisher.getWishes().remove(this);
        }
        this.wisher = user;
        user.getWishes().add(this);
    }

    public void updateTotalPrice(int price) {
        this.totalPrice += price;
    }

    public WishStatus getStatus(int expiryDay) {
        val now = LocalDateTime.now().toLocalDate().atStartOfDay();
        if (this.startAt.isAfter(now)) {
            return BEFORE;
        } else if (this.endAt.plusDays(expiryDay).isBefore(now)) {
            return END;
        } else {
            return WHILE;
        }
    }

    public void updateContent(String imageUrl, String title, boolean wantsGift) {
        if (nonNull(imageUrl)) {
            this.presentImageUrl = imageUrl;
        }
        if (nonNull(title)) {
            this.title = title;
        }
        if(nonNull(wantsGift)) {
            this.wantsGift = wantsGift;
        }
    }

    public void updateTerm(LocalDateTime startAt, LocalDateTime endAt) {
        if (nonNull(startAt)) {
            this.startAt = startAt;
        }
        if (nonNull(endAt)) {
            this.endAt = endAt;
        }
    }
}
