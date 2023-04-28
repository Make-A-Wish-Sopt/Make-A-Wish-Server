package com.sopterm.makeawish.service;

import java.util.List;

import com.sopterm.makeawish.common.KakaoPayProperties;
import com.sopterm.makeawish.dto.cake.CakeReadyRequestDto;
import com.sopterm.makeawish.dto.cake.CakeReadyResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.sopterm.makeawish.dto.cake.CakeResponseDTO;
import com.sopterm.makeawish.repository.CakeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CakeService {

	private final CakeRepository cakeRepository;


	public List<CakeResponseDTO> getAllCakes() {
		return cakeRepository.findAll()
			.stream().map(CakeResponseDTO::from)
			.toList();
	}


	public CakeReadyResponseDto getKakaoPayReady(CakeReadyRequestDto request){ // 결제 요청
		MultiValueMap<String, String> parameters=new LinkedMultiValueMap<>();
		parameters.add("cid", KakaoPayProperties.cid);
		parameters.add("partner_order_id", request.partner_order_id());
		parameters.add("partner_user_id", request.partner_user_id());
		parameters.add("item_name",request.item_name());
		parameters.add("quantity", "1");
		parameters.add("total_amount", request.total_amount());
		parameters.add("vat_amount", request.vat_amount());
		parameters.add("tax_free_amount", request.tax_free_amount());
		parameters.add("approval_url", request.approval_url());
		parameters.add("cancel_url", request.cancel_url());
		parameters.add("fail_url", request.fail_url());

		HttpEntity<MultiValueMap<String, String>> requestEntity=new HttpEntity<>(parameters, this.getHeaders());

		RestTemplate restTemplate=new RestTemplate();

		CakeReadyResponseDto response=restTemplate.postForObject(
				"https://kapi.kakao.com/v1/payment/ready",
				requestEntity,
				CakeReadyResponseDto.class
		);
		return response;
	}

	private HttpHeaders getHeaders(){
		HttpHeaders httpHeaders=new HttpHeaders();
		String auth="KakaoAK "+KakaoPayProperties.adminKey;

		httpHeaders.set("Authorization", auth);
		httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		return httpHeaders;
	}
}
