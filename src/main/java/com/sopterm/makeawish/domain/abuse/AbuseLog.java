package com.sopterm.makeawish.domain.abuse;

import com.sopterm.makeawish.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class AbuseLog {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "abuse_log_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    protected LocalDateTime createdAt;

    @Builder
    public AbuseLog(User user){
        this.user = user;
    }
}
