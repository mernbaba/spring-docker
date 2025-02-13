package com.profit.controller;

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
import com.profit.dto.PTPricingPlanDTO;
import com.profit.dto.ResponseObject;
import com.profit.service.PTPricingPlanService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/ptplan")
public class PTPricingPlanController {
	
	@Autowired
	HttpServletRequest request;

	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	PTPricingPlanService ptPricingPlanService;
	
	@GetMapping("/getAll")
	public ResponseObject<List<PTPricingPlanDTO>> getAllPricingPlans(){
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		
		return ptPricingPlanService.getAllPlans(company, branch);
		
	}
	
	@GetMapping("/getByStaff")
	public ResponseObject<?> getPricingPlansForStaff(@RequestParam String staffCode){
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		
		return ptPricingPlanService.getStaffPlans(staffCode, branch, company);
	}
	
	@PostMapping("/save")
	public ResponseObject<PTPricingPlanDTO> savePricingPlan(@RequestBody PTPricingPlanDTO dto){
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		String username = claims.getSubject();
		
		return ptPricingPlanService.savePlan(dto, company, branch, username);
		
	}
	
	@PutMapping("/update")
	public ResponseObject<PTPricingPlanDTO> updatePricingPlan(@RequestBody PTPricingPlanDTO dto){
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		String username = (String) claims.getSubject();
		
		return ptPricingPlanService.updatePricingPlans(dto, company, branch, username);
		
	}

}
