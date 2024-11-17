package com.profit.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.profit.configuration.JwtTokenUtil;
import com.profit.dto.ResponseObject;
import com.profit.dto.StaffTypeMasterDTO;
import com.profit.service.StaffTypeMasterService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/staff/type/master")
public class StaffTypeMasterController {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private StaffTypeMasterService staffTypeMasterService;

	@PostMapping("/save")
	private ResponseObject<StaffTypeMasterDTO> save(@RequestBody StaffTypeMasterDTO staffTypeMasterDTO) {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		String userName = (String) claims.getSubject();
		return staffTypeMasterService.save(staffTypeMasterDTO, company, branch, userName);
	}

	@GetMapping("/")
	public ResponseObject<List<StaffTypeMasterDTO>> getAll() {

		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		return staffTypeMasterService.getAll(company, branch);
	}
}
