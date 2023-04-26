package com.sopterm.makeawish.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sopterm.makeawish.dto.cake.CakeResponseDTO;
import com.sopterm.makeawish.repository.CakeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CakeService {

	private final CakeRepository cakeRepository;

	public List<CakeResponseDTO> getAllCakes() {
		return cakeRepository.findAll()
			.stream().map(CakeResponseDTO::from)
			.toList();
	}
}
