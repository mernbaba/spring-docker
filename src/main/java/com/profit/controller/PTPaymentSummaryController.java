package com.profit.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.profit.configuration.JwtTokenUtil;
import com.profit.dto.PTPaymentSummaryDTO;
import com.profit.dto.ResponseObject;
import com.profit.service.PTPaymentSummaryService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/ptSummary")
public class PTPaymentSummaryController {

	@Autowired
	HttpServletRequest request;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	PTPaymentSummaryService paymentSummaryService;

	@GetMapping("/getAllByDates")
	public ResponseObject<List<PTPaymentSummaryDTO>> getAllDetails(@RequestParam(required = false) String fromDate,
			@RequestParam(required = false) String toDate, @RequestParam String customerCode) {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");

		return paymentSummaryService.getAllByDates(fromDate, toDate, customerCode, branch, company);

	}

	@PostMapping("/save")
	public ResponseObject<PTPaymentSummaryDTO> saveDetails(@RequestBody PTPaymentSummaryDTO dto) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		String username = claims.getSubject();

		return paymentSummaryService.save(dto, branch, company, username);
	}

	@PutMapping("/update")
	public ResponseObject<PTPaymentSummaryDTO> updateDetails(@RequestBody PTPaymentSummaryDTO dto) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		String username = claims.getSubject();

		return paymentSummaryService.update(dto, branch, company, username);

	}

}
