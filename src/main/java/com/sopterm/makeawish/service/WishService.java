package com.sopterm.makeawish.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopterm.makeawish.common.message.slack.SlackErrorMessage;
import com.sopterm.makeawish.common.message.slack.SlackSuccessMessage;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.dto.wish.*;
import com.sopterm.makeawish.external.SlackWishClient;
import com.sopterm.makeawish.repository.PresentRepository;
import com.sopterm.makeawish.repository.UserRepository;
import com.sopterm.makeawish.repository.wish.WishRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

import static com.sopterm.makeawish.common.Util.convertToDate;
import static com.sopterm.makeawish.common.message.ErrorMessage.*;
import static com.sopterm.makeawish.common.message.slack.SlackField.*;
import static com.sopterm.makeawish.domain.wish.WishStatus.*;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishService {

	@Value("${spring.profiles.active}")
	private String activeProfile;
	private final WishRepository wishRepository;
	private final UserRepository userRepository;
	private final PresentRepository presentRepository;
	private final SlackWishClient slackWishClient;
	private final ObjectMapper jsonMapper = new ObjectMapper();

	private final int EXPIRY_DAY = 7;

	@Transactional
	public Long createWish(Long userId, WishRequestDTO requestDTO) {
		val wisher = getUser(userId);
		if (wishRepository.findMainWish(wisher, EXPIRY_DAY).isPresent()) {
			throw new IllegalStateException(EXIST_MAIN_WISH.getMessage());
		}
		val from = convertToDate(requestDTO.startDate());
		val to = convertToDate(requestDTO.endDate());
		validateWishDate(wisher, from, to);
		val wish = requestDTO.toEntity(wisher);
		try {
			val wishSlackRequest = createSlackWishRequest(wish);
			slackWishClient.postWishMessage(wishSlackRequest.toString());
		} catch (RuntimeException e) {
			log.error(SlackErrorMessage.POST_REQUEST_ERROR.getMessage() + e.getMessage());
		}
		return wishRepository.save(wish).getId();
	}

	public WishResponseDTO findWish(Long wishId) throws AccessDeniedException {
		val wish = getWish(wishId);
		if (!wish.getStatus(0).equals(WHILE)) {
			throw new AccessDeniedException(INVALID_WISH.getMessage());
		}
		return WishResponseDTO.from(wish);
	}

	public MainWishResponseDTO findMainWish(Long userId) {
		val wish = wishRepository
			.findMainWish(getUser(userId), EXPIRY_DAY)
			.orElse(null);
		return nonNull(wish) ? MainWishResponseDTO.from(wish) : null;
	}

	public Wish getWish(Long id) {
		return wishRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_WISH.getMessage()));
	}

	public String getPresentInfo(String url, String tag) throws IOException {
		return Jsoup.connect(url)
			.timeout(5000).get()
			.select("." + tag)
			.toString();
	}

	public UserWishResponseDTO findWish(Long userId, Long wishId) throws AccessDeniedException {
		val wish = getWish(wishId);
		if (!wish.getWisher().getId().equals(userId)) {
			throw new AccessDeniedException(FORBIDDEN.getMessage());
		}
		return UserWishResponseDTO.of(wish);
	}

	@Transactional
	public void deleteWishes(Long userId, WishIdRequestDTO requestDTO) {
		val user = getUser(userId);
		requestDTO.wishes().forEach(wishId -> deleteUserWish(user, getWish(wishId)));
	}

	public WishesResponseDTO findWishes(Long userId) {
		val wishes = wishRepository.findEndWishes(getUser(userId), EXPIRY_DAY);
		return WishesResponseDTO.of(wishes);
	}

	@Transactional
	public UserWishUpdateResponseDTO updateUserMainWish(Long userId, UserWishUpdateRequestDTO request) {
		val wisher = getUser(userId);
		val wish = getUserMainWish(wisher);
		val status = wish.getStatus(0);

		if (status.equals(END)) {
			throw new IllegalArgumentException(NOT_CURRENT_WISH.getMessage());
		}
		if (status.equals(BEFORE)) {
			val startDate = nonNull(request.startDate()) ? convertToDate(request.startDate()) : null;
			val endDate = nonNull(request.endDate()) ? convertToDate(request.endDate()) : null;
			wish.updateTerm(startDate, endDate);
		}
		if (status.equals(BEFORE) || status.equals(WHILE)) {
			wish.updateContent(request.imageUrl(), request.price(), request.title(), request.hint(), request.initial());
			wisher.updateProfile(request.name(), request.bankName(), request.account(), request.phone());
		}

		return UserWishUpdateResponseDTO.of(wisher, wish);
	}

	public UserWishUpdateResponseDTO findUserMainWish(Long userId) {
		val wisher = getUser(userId);
		val wish = getUserMainWish(wisher);
		return UserWishUpdateResponseDTO.of(wisher, wish);
	}

	@Transactional
	public void stopWish(Long userId) throws AccessDeniedException {
		val user = getUser(userId);
		val wish = getUserMainWish(user);

		val status = wish.getStatus(0);
		switch(status) {
			case BEFORE -> wishRepository.delete(wish);
			case WHILE -> wish.updateTerm(null, LocalDateTime.now().toLocalDate().atStartOfDay().minusDays(1));
			default -> throw new AccessDeniedException(EXPIRE_WISH.getMessage());
		}
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_USER.getMessage()));
	}

	private void validateWishDate(User wisher, LocalDateTime from, LocalDateTime to) {
		val now = LocalDateTime.now().toLocalDate().atStartOfDay();
		if (from.isBefore(now) || to.isBefore(now)) {
			throw new IllegalArgumentException(PAST_WISH.getMessage());
		}
		if (from.isAfter(to)) {
			throw new IllegalArgumentException(INVALID_TERM.getMessage());
		}
		if (wishRepository.existsConflictWish(wisher, from, to, EXPIRY_DAY)) {
			throw new IllegalArgumentException(EXIST_MAIN_WISH.getMessage());
		}
	}

	private void deleteUserWish(User wisher, Wish wish) {
		if (wish.getWisher().equals(wisher)) {
			presentRepository.deleteAll(wish.getPresents());
			wishRepository.delete(wish);
		}
	}

	private Wish getUserMainWish(User user) {
		return wishRepository
			.findMainWish(user, 0)
			.orElseThrow(() -> new EntityNotFoundException(NO_WISH.getMessage()));
	}

	private JsonNode createSlackWishRequest(Wish wish) {
		val rootNode = jsonMapper.createObjectNode();
		rootNode.put(TEXT, SlackSuccessMessage.SUCCESS_CREATE_WISH.getMessage());
		val blocks = jsonMapper.createArrayNode();

		val textField = jsonMapper.createObjectNode();
		textField.put(TYPE, SECTION);
		textField.set(TEXT, createTextFieldNode(SlackSuccessMessage.SUCCESS_CREATE_WISH.getMessage()));

		val contentNode = jsonMapper.createObjectNode();
		contentNode.put(TYPE, SECTION);
		val fields = jsonMapper.createArrayNode();
		fields.add(createTextFieldNode(USER_NAME + StringUtils.LF + wish.getWisher().getNickname()));
		fields.add(createTextFieldNode(USER_WISH_PERIOD + StringUtils.LF + wish.getStartAt() + " ~ " + wish.getEndAt()));
		contentNode.set(FIELDS, fields);

		blocks.add(textField);
		blocks.add(contentNode);
		rootNode.set(BLOCKS, blocks);
		return rootNode;
	}

	private JsonNode createTextFieldNode (String text) {
		val textField = jsonMapper.createObjectNode();
		textField.put(TYPE, MARKDOWN);
		textField.put(TEXT, text);
		return textField;
	}

}