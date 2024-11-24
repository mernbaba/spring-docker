package com.profit.controller;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.profit.configuration.JwtTokenUtil;
import com.profit.dto.CustomerPaymentSummaryDTO;
import com.profit.dto.ResponseObject;
import com.profit.service.CustomerPaymentSummaryService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/payment/summery")
public class CustomerPaymentSummeryController {

	@Autowired
	HttpServletRequest request;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	CustomerPaymentSummaryService customerPaymentSummeryService;

	@GetMapping("/getAllUsers")
	public ResponseObject<List<CustomerPaymentSummaryDTO>> getAllUsers(@RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String customerCode) {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");

		return customerPaymentSummeryService.getUsers(fromDate, toDate, customerCode, branch, company);

	}

	@GetMapping("/getUserPayments")
	public ResponseObject<List<CustomerPaymentSummaryDTO>> getUserPayments(@RequestParam String customerCode) {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		return customerPaymentSummeryService.getUserPayments(customerCode, branch, company);
	}

	@PostMapping("/save")
	public ResponseObject<CustomerPaymentSummaryDTO> saveCustomerPaymentSummery(
			@RequestBody CustomerPaymentSummaryDTO dto) {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		String username = claims.getSubject();
		return customerPaymentSummeryService.save(dto, branch, company, username);
	}

	@PutMapping("/update")
	public ResponseObject<CustomerPaymentSummaryDTO> updateCustomerPayments(
			@RequestBody CustomerPaymentSummaryDTO dto) {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		String username = claims.getSubject();
		return customerPaymentSummeryService.update(dto, branch, company, username);
	}

}
