package com.profit.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.profit.configuration.JwtTokenUtil;
import com.profit.datamodel.RoleMaster;
import com.profit.dto.ResponseObject;
import com.profit.dto.RoleMasterDTO;
import com.profit.service.RoleMasterService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/roleMaster")
public class RoleMasterController {
	
	@Autowired
	HttpServletRequest request;

	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	RoleMasterService roleMasterService;
	
	@GetMapping("/getRoles")
	public ResponseObject<List<RoleMasterDTO>> getAllRoles(){
		String token = request.getHeader("Authorization");
		token = StringUtils.replace(token, "Bearer ", "");
		
		return roleMasterService.getAllRoles();		
	}

}
