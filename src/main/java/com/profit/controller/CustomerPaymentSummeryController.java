package com.profit.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.profit.service.PTPaymentSummaryService;

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
	CustomerPaymentSummaryService customerPaymentSummaryService;
	
	@Autowired
	PTPaymentSummaryService ptPaymentSummaryService;

	@GetMapping("/getAllUsers")
	public ResponseObject<List<CustomerPaymentSummaryDTO>> getAllUsers(@RequestParam(required = false) String fromDate,
			@RequestParam(required = false) String toDate, @RequestParam String customerCode) {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");

		return customerPaymentSummaryService.getUsers(fromDate, toDate, customerCode, branch, company);

	}

	@GetMapping("/getUserPayments")
	public ResponseObject<List<CustomerPaymentSummaryDTO>> getUserPayments(@RequestParam String customerCode) {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		return customerPaymentSummaryService.getUserPayments(customerCode, branch, company);
	}
	
	@GetMapping("/dashboardData")
	public ResponseObject<?> getDashboardData(){
		Map<String, List<?>> responseMap = new HashMap<>();
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		
		LocalDate startDate = LocalDate.now().minusMonths(6);
		LocalDate endDate = LocalDate.now();
		
		responseMap.put("customerPaymentData", customerPaymentSummaryService.customerPendingPayments(startDate, endDate, company, branch).getResponse());
		responseMap.put("ptPaymentData", ptPaymentSummaryService.customerPtPendingPayments(startDate, endDate, company, branch).getResponse());
		
		return ResponseObject.success(responseMap);
		
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
		return customerPaymentSummaryService.save(dto, branch, company, username);
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
		return customerPaymentSummaryService.update(dto, branch, company, username);
	}
	
	@PutMapping("/inactive/{id}")
	public ResponseObject<?> updateById(@PathVariable long id){
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		String username = claims.getSubject();
		
		return customerPaymentSummaryService.deactivateCustomerSummaryRecordById(id, company, branch, username);
	}

}
