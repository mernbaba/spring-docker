package com.profit.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.profit.configuration.JwtTokenUtil;
import com.profit.dto.CustomerPricingPlanDTO;
import com.profit.dto.ResponseObject;
import com.profit.service.CustomerPricingPlanService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class CustomerPricingPlanController {

	@Autowired
	HttpServletRequest request;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	CustomerPricingPlanService customerPricingPlanService;

	@GetMapping("/getPricingPlans")
	public ResponseObject<List<CustomerPricingPlanDTO>> getAllPricingPlans() {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		return customerPricingPlanService.getAllPricingPlans(company, branch);
	}

	@PostMapping("/savePricingPlans")
	public ResponseObject<CustomerPricingPlanDTO> savePricingPlan(@RequestBody CustomerPricingPlanDTO dto) {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		String username = (String) claims.getSubject();
		return customerPricingPlanService.save(dto, company, branch, username);
	}
	
	@PutMapping("/updatePricingPlans")
	public ResponseObject<CustomerPricingPlanDTO> updatePricingPlan(@RequestBody CustomerPricingPlanDTO dto) {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		String username = (String) claims.getSubject();
		return customerPricingPlanService.updatePricingPlans(dto, company, branch, username);
	}

}
