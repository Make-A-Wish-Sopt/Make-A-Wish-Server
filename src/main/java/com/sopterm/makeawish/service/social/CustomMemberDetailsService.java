package com.sopterm.makeawish.service.social;

import com.sopterm.makeawish.domain.user.InternalMemberDetails;
import com.sopterm.makeawish.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import static com.sopterm.makeawish.common.message.ErrorMessage.INVALID_USER;

@RequiredArgsConstructor
@Service
public class CustomMemberDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userIdStr) {
        val userId = Long.parseLong(userIdStr);
        val member = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(INVALID_USER.getMessage()));
        return new InternalMemberDetails(member);
    }
}
