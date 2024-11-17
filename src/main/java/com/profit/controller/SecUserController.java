package com.profit.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.profit.configuration.JwtTokenUtil;
import com.profit.dto.ResponseObject;
import com.profit.dto.SecUserDTO;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;
import com.profit.service.SecUserService; // Assume this service exists

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class SecUserController {

	@Autowired
	private SecUserService secUserService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@GetMapping("/getAllUsers")
	public ResponseObject<List<SecUserDTO>> getAllUsers() {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		return secUserService.getAllUsers(company, branch);
	}

	@GetMapping("/getUser")
	public ResponseObject<?> getProfile() {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");

		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		String username = (String) claims.getSubject();
		return secUserService.getProfile(company, branch, username);
	}

	@PostMapping("/changePwd")
	private ResponseObject<?> changePassword(@RequestParam String oldPwd, @RequestParam String newPwd) {
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		String userName = (String) claims.getSubject();

		if (oldPwd.equalsIgnoreCase(newPwd)) {
			throw new CloudBaseException(ResponseCode.OLD_NEW_PWD_SAME);
		} else {
			return secUserService.changePassword(newPwd, company, branch, userName);
		}
	}
}
