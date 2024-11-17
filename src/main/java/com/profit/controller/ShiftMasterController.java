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
import com.profit.dto.ShiftMasterDTO;
import com.profit.service.ShiftMasterService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class ShiftMasterController {
	
	@Autowired
	HttpServletRequest request;

	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	ShiftMasterService shiftMasterService;
	
	
	@GetMapping("/getShifts")
	public ResponseObject<List<ShiftMasterDTO>> getAllShifts() {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		return shiftMasterService.getShits(company, branch);
	}
	
	@PostMapping("/saveShift")
	public ResponseObject<ShiftMasterDTO> saveShift(@RequestBody ShiftMasterDTO dto){
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		String username = (String) claims.getSubject();
		return shiftMasterService.save(dto, company, branch, username);
	}
	
	@PutMapping("updateShift")
		public ResponseObject<ShiftMasterDTO> updateShift(@RequestBody ShiftMasterDTO dto){
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		String username = (String) claims.getSubject();
		return shiftMasterService.updateShift(dto, company, branch, username);
	}

}
