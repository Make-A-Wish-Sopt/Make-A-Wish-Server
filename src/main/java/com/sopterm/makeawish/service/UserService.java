package com.sopterm.makeawish.service;

import com.popbill.api.AccountCheckInfo;
import com.popbill.api.AccountCheckService;
import com.popbill.api.PopbillException;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.dto.user.UserAccountRequestDTO;
import com.sopterm.makeawish.dto.user.UserAccountResponseDTO;
import com.sopterm.makeawish.repository.PresentRepository;
import com.sopterm.makeawish.repository.UserRepository;
import com.sopterm.makeawish.repository.wish.WishRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final WishRepository wishRepository;
	private final PresentRepository presentRepository;
	private final AccountCheckService accountCheckService;

	@Value("${popbill.businessNumber}")
	private String corpNum;

	public UserAccountResponseDTO getUserAccount(Long userId) {
		val wisher = getUser(userId);
		return nonNull(wisher.getAccount()) ? UserAccountResponseDTO.of(wisher) : null;
	}

	@Transactional
	public UserAccountResponseDTO updateUserAccount(Long userId, UserAccountRequestDTO requestDTO) {
		val wisher = getUser(userId);
		if (isNull(requestDTO.accountInfo())) {
			throw new IllegalArgumentException(NO_EXIST_USER_ACCOUNT.getMessage());
		}
		wisher.updateAccount(
			requestDTO.accountInfo().getName(),
			requestDTO.accountInfo().getBank(),
			requestDTO.accountInfo().getAccount());
		wisher.updatePhoneNumber(requestDTO.phone());
		return UserAccountResponseDTO.of(wisher);
	}

	@Transactional
	public void deleteUser(Long userId) {
		val user = getUser(userId);
		user.getWishes().forEach(wish -> {
			wish.getPresents().forEach(presentRepository::delete);
			wishRepository.delete(wish);
		});
		userRepository.delete(user);
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_USER.getMessage()));
	}

	public void verifyUserAccount(String name, String BankCode, String AccountNumber) throws PopbillException {
		try {
            val accountInfo = accountCheckService.CheckAccountInfo(corpNum, BankCode, AccountNumber);

			if(!name.equals(accountInfo.getAccountName()))
				throw new IllegalArgumentException(NOT_VALID_USER_ACCOUNT.getMessage());
		} catch (PopbillException e) {
			throw new PopbillException(e.getCode(), e.getMessage());
		}
	}
}
