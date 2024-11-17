package com.profit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.profit.dto.ResponseObject;
import com.profit.dto.SecUserDTO;
import com.profit.enumeration.ResponseCode;
import com.profit.request.CompanyRegister;
import com.profit.service.CustomerMasterService;
import com.profit.service.SecUserService;

@RestController
@RequestMapping("/initial")
public class PreSecurityController {

	@Autowired
	SecUserService secUserService;

	@Autowired
	CustomerMasterService customerMasterService;

	@PostMapping("/register")
	public ResponseObject<String> companyRegister(@RequestBody CompanyRegister companyRegister) {
		return secUserService.registerCompany(companyRegister);
	}

	@PostMapping("/user/register")
	public ResponseObject<SecUserDTO> saveSecUser(@RequestBody SecUserDTO secUserDTO) throws Exception {
		return secUserService.saveSecUser(secUserDTO);
	}

	@GetMapping("/forgotPassword")
	public ResponseObject<ResponseCode> forgotPassword(@RequestParam String email) {
		return secUserService.forgotPassword(email);
	}

	@GetMapping("/resetPassword")
	public ResponseObject<ResponseCode> resetPassword(@RequestParam String email, @RequestParam String pwd,
			String otp) {
		return secUserService.resetPassword(email, pwd, otp);
	}
}
