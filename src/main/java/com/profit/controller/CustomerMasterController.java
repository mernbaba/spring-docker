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
import com.profit.dto.CustomerMasterDTO;
import com.profit.dto.ResponseObject;
import com.profit.service.CustomerMasterService;
import com.profit.service.SecUserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class CustomerMasterController {
	
	@Autowired
	CustomerMasterService customerMasterService;
	
	@Autowired
    HttpServletRequest request;

    @Autowired
    JwtTokenUtil jwtTokenUtil;
    
    @GetMapping("/getcustomer")
    public ResponseObject<List<CustomerMasterDTO>> getAllCusotmers() {
    	String token = request.getHeader("Authorization"); 
		token = StringUtils.replace(token, "Bearer " , "");
		
		Claims claims =jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String)claims.get("company");
		String branch = (String)claims.get("branch");
		return customerMasterService.getAll(company, branch);
    }
	
	@PostMapping("/customerRegister")
	public ResponseObject<CustomerMasterDTO> saveCustomer(@RequestBody CustomerMasterDTO customerMaster) {
		String token = request.getHeader("Authorization"); 
		token = StringUtils.replace(token, "Bearer " , "");
		
		Claims claims =jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String)claims.get("company");
		String branch = (String)claims.get("branch");
		String username = (String)claims.getSubject();
		return customerMasterService.saveCustomer(customerMaster, company, branch, username);
	}
	
	@PutMapping("/updateCustomer")
	public ResponseObject<CustomerMasterDTO> updateCustomer(@RequestBody CustomerMasterDTO dto) {
		String token = request.getHeader("Authorization"); 
		token = StringUtils.replace(token, "Bearer " , "");
		
		Claims claims =jwtTokenUtil.getAllClaimsFromToken(token);
		String company = (String)claims.get("company");
		String branch = (String)claims.get("branch");
		String username = (String)claims.getSubject();
		return customerMasterService.updateCustomer(dto, company, branch, username);
	}

}
