package com.sopterm.makeawish.domain.wish;

import com.sopterm.makeawish.domain.BaseEntity;
import com.sopterm.makeawish.domain.Present;
import com.sopterm.makeawish.domain.user.User;
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

    @Column(columnDefinition = "TEXT")
    private String hint;

    private String initial;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private int presentPrice;

    private int totalPrice;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User wisher;

    @OneToMany(mappedBy = "wish")
    private final List<Present> presents = new ArrayList<>();

    @Builder
    public Wish(String title, String presentImageUrl, String hint, String initial, LocalDateTime startAt,
                LocalDateTime endAt, int presentPrice, User wisher) {
        this.title = title;
        this.presentImageUrl = presentImageUrl;
        this.hint = hint;
        this.initial = initial;
        this.startAt = startAt;
        this.endAt = endAt;
        this.presentPrice = presentPrice;
        this.totalPrice = 0;
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
        val now = LocalDateTime.now();
        if (this.startAt.isAfter(now)) {
            return BEFORE;
        } else if (this.startAt.isBefore(now) && this.endAt.plusDays(expiryDay).isAfter(now)) {
            return WHILE;
        } else {
            return END;
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
