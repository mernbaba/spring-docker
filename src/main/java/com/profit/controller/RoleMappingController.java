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
import com.profit.dto.ResponseObject;
import com.profit.dto.RoleMappingDTO;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;
import com.profit.service.RoleMappingService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/rolemapping")
public class RoleMappingController {
	
	@Autowired
	HttpServletRequest request;

	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	RoleMappingService roleMappingService;
	
	@GetMapping("/getRoles")
	public ResponseObject<List<RoleMappingDTO>> getAllRoles(){
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		
		return roleMappingService.getRoles(company, branch);
	}
	
	@GetMapping("/getUserRoles")
	public ResponseObject<List<RoleMappingDTO>> getRolesByUserCode(@RequestParam String userCode){
		if(userCode == null) {
			throw new CloudBaseException(ResponseCode.BAD_REQUEST);
		}
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		
		return roleMappingService.getRolesByUserCode(userCode, company, branch);
	}
	
	@PutMapping("/updateRoles")
	public ResponseObject<List<RoleMappingDTO>> saveRole(@RequestBody List<RoleMappingDTO> dtoList){
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String) claims.get("company");
		String branch = (String) claims.get("branch");
		String username = (String) claims.getSubject();
		
		return roleMappingService.updateRoles(dtoList, company, branch, username);
	}

}
