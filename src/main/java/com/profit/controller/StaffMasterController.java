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
import com.profit.dto.ResponseObject;
import com.profit.dto.StaffMasterDTO;
import com.profit.service.StaffMasterService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class StaffMasterController {

	@Autowired
	StaffMasterService staffMasterService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@GetMapping("getStaff")
	public ResponseObject<List<StaffMasterDTO>> getAllStaff() {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		return staffMasterService.getAll(company, branch);
	}
	
	@GetMapping("getTrainers")
	public ResponseObject<List<StaffMasterDTO>> getAllTrainers() {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		return staffMasterService.getAllTrainers(company, branch);
	}

	@PostMapping("saveStaff")
	public ResponseObject<StaffMasterDTO> saveStaff(@RequestBody StaffMasterDTO dto) {
		String token = request.getHeader("Authorization"); 
		token = StringUtils.replace(token, "Bearer " , "");
		
		Claims claims =jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String)claims.get("company");
		String branch = (String)claims.get("branch");
		String username = (String)claims.getSubject();
		return staffMasterService.save(dto, company, branch, username);
	}

	@PutMapping("updateStaff")
	public ResponseObject<StaffMasterDTO> updateStaff(@RequestBody StaffMasterDTO dto) {
		String token = request.getHeader("Authorization"); 
		token = StringUtils.replace(token, "Bearer " , "");
		
		Claims claims =jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String)claims.get("company");
		String branch = (String)claims.get("branch");
		String username = (String)claims.getSubject();
		return staffMasterService.update(dto, company, branch, username);
	}

}
