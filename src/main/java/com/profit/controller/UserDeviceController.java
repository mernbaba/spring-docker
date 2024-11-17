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
import com.profit.dto.UserDeviceDTO;
import com.profit.request.ChangeDeviceRequest;
import com.profit.service.UserDeviceService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/userDevice")
public class UserDeviceController {
	
	@Autowired
	HttpServletRequest request;

	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	UserDeviceService userDeviceService;
	
	@GetMapping("/getuserDevices")
	public ResponseObject<List<UserDeviceDTO>> getAllUserDevices(){
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch"); 
		
		return userDeviceService.getAll(company, branch);
	}
	
	@PostMapping("/save")
	public ResponseObject<UserDeviceDTO> saveNewDevice(@RequestBody ChangeDeviceRequest req){
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch"); 
		String username = claims.getSubject();
		
		return userDeviceService.save(req, username);
		
	}
	
	@PutMapping("/approve")
	public ResponseObject<UserDeviceDTO> approveDeviceId(@RequestBody UserDeviceDTO dto){
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		String username = (String) claims.getSubject();
		
		return userDeviceService.update(dto, company, branch, username);
	}

}
