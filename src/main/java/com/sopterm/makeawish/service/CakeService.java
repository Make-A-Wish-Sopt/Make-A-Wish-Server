package com.sopterm.makeawish.service;

import com.sopterm.makeawish.common.KakaoPayProperties;
import com.sopterm.makeawish.domain.Cake;
import com.sopterm.makeawish.domain.Present;
import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.dto.cake.*;
import com.sopterm.makeawish.dto.present.PresentDTO;
import com.sopterm.makeawish.dto.present.PresentResponseDTO;
import com.sopterm.makeawish.repository.CakeRepository;
import com.sopterm.makeawish.repository.PresentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class CakeService {

    private final WishService wishService;
    private final CakeService cakeService;
    private final CakeRepository cakeRepository;
    private final PresentRepository presentRepository;

    public List<CakeResponseDTO> getAllCakes() {
        return cakeRepository.findAll()
                .stream().map(CakeResponseDTO::from)
                .toList();
    }

    public CakeReadyResponseDTO getKakaoPayReady(CakeReadyRequestDTO request) {
        val requestEntity = new HttpEntity<>(this.getReadyParameters(request), this.getHeaders());
        try {
            val restTemplate = new RestTemplate();
            return restTemplate.postForObject(
                    KakaoPayProperties.readyUrl,
                    requestEntity,
                    CakeReadyResponseDTO.class
            );
        } catch (HttpClientErrorException e) {
            throw new HttpClientErrorException(e.getStatusCode(), e.getMessage());
        }
    }

    private HttpHeaders getHeaders() {
        val httpHeaders = new HttpHeaders();
        val auth = "KakaoAK " + KakaoPayProperties.adminKey;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }

    private MultiValueMap<String, String> getReadyParameters(CakeReadyRequestDTO request) {
        val cake = getPayCake(request.cake());

        val parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("cid", KakaoPayProperties.cid);
        parameters.add("partner_order_id", request.partnerOrderId());
        parameters.add("partner_user_id", request.partnerUserId());
        parameters.add("item_name", cake.getName());
        parameters.add("quantity", "1");
        parameters.add("total_amount", String.valueOf(cake.getPrice()));
        parameters.add("vat_amount", request.vatAmount());
        parameters.add("tax_free_amount", request.taxFreeAmount());
        parameters.add("approval_url", request.approvalUrl());
        parameters.add("cancel_url", request.cancelUrl());
        parameters.add("fail_url", request.failUrl());

        return parameters;
    }

    private MultiValueMap<String, String> getApproveParameters(CakeApproveRequestDTO request) {
        val parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("cid", KakaoPayProperties.cid);
        parameters.add("partner_order_id", request.partnerOrderId());
        parameters.add("partner_user_id", request.partnerUserId());
        parameters.add("tid", request.tid());
        parameters.add("pg_token", request.pgToken());

        return parameters;
    }

    public CakeApproveResponseDTO getKakaoPayApprove(CakeApproveRequestDTO request) {
        val requestEntity = new HttpEntity<>(this.getApproveParameters(request), this.getHeaders());

        val restTemplate = new RestTemplate();
        try {
            return restTemplate.postForObject(
                    KakaoPayProperties.approveUrl,
                    requestEntity,
                    CakeApproveResponseDTO.class
            );
        } catch (HttpClientErrorException e) {
            throw new HttpClientErrorException(e.getStatusCode(), e.getMessage());
        }
    }

    @Transactional
    public CakeCreateResponseDTO createPresent(String name, Cake cake, Wish wish, String message) {
        val present = new Present(name, message, wish, cake);
        presentRepository.save(present);
        wish.updateTotalPrice(cake.getPrice());
        val contribute = calculateContribute(cake.getPrice(), wish.getPresentPrice());
        return new CakeCreateResponseDTO(cake.getId(), wish.getPresentImageUrl(), wish.getHint(), wish.getInitial(), contribute, wish.getWisher().getNickname());
    }

    private String calculateContribute(int price, int targetPrice) {
        return String.format("%.0f", (double) price / (double) targetPrice * 100);
    }

    public Cake getCake(Long id) {
        return cakeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(INVALID_CAKE.getMessage()));
    }

    @Cacheable(cacheNames = "allPresentsList")
    public List<PresentDTO> getPresents(Long userId, Long wishId) {
        val wish = wishService.getWish(wishId);
        if (!isRightWisher(userId, wish))
            throw new IllegalArgumentException(INCORRECT_WISH.getMessage());

        val allCake = getAllCakes().stream().collect(
                Collectors.toMap(
                        CakeResponseDTO::toEntity,
                        count -> 0L
                ));

        val cakes = getAllPresent(wish);
        allCake.putAll(cakes);

        return allCake.entrySet().stream()
                .map(cake -> PresentDTO.from(cake.getKey(), cake.getValue()))
                .sorted(Comparator.comparing(PresentDTO::cakeId))
                .toList();
    }

    private Map<Cake, Long> getAllPresent(Wish wish) {
        return wish.getPresents().stream()
                .collect(Collectors.groupingBy(Present::getCake, Collectors.counting()));
    }

    private boolean isRightWisher(Long userId, Wish wish) {
        return userId.equals(wish.getWisher().getId());
    }

    public Cake getPayCake(Long cakeId) {
        if (cakeId.equals(1L)) {
            throw new IllegalArgumentException(NOT_PAID_CAKE.getMessage());
        }
        return getCake(cakeId);
    }

    public List<PresentResponseDTO> getEachPresent(Long userId, Long wishId, Long cakeId) {
        val wish = wishService.getWish(wishId);
        if (!isRightWisher(userId, wish)) {
            throw new IllegalArgumentException(INCORRECT_WISH.getMessage());
        }
        val presents = presentRepository.findPresentsByWishIdAndCakeId(wishId, cakeId);
        return presents.stream().map(PresentResponseDTO::from).collect(Collectors.toList());
    }

    @Transactional
    public CakeCreateResponseDTO createPresentNew(CakeRequest request) {
        val cake = cakeService.getCake(request.cakeId());
        val wish = wishService.getWish(request.wishId());
        val present = new Present(request.name(), request.message(), wish, cake);
        presentRepository.save(present);
        wish.updateTotalPrice(cake.getPrice());
        val contribute = calculateContribute(cake.getPrice(), wish.getPresentPrice());
        return new CakeCreateResponseDTO(cake.getId(), wish.getPresentImageUrl(), wish.getHint(), wish.getInitial(), contribute, wish.getWisher().getNickname());
    }
}
