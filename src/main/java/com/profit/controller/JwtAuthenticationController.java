package com.profit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.profit.dto.ResponseObject;
import com.profit.request.JwtRequest;
import com.profit.response.JwtResponse;
import com.profit.service.SecUserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	SecUserService secUserService;

	@Autowired
	HttpServletRequest request;

	@PostMapping("/authenticate")
	public ResponseObject<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
			throws Exception {
		System.out.println(authenticationRequest.getUsername() + authenticationRequest.getPassword());

		return secUserService.authenticateUser(authenticationRequest);
	}

//	@RequestMapping(value = "chpw", method = RequestMethod.POST)
//	public Boolean changeapipassword(@RequestParam String username,@RequestParam String oldpwd, @RequestParam String newpwd) throws Exception {
//		
//		JwtRequest authenticationRequest = new JwtRequest();
////		String token = request.getHeader("Authorization"); 
////		Map<String,Object> permissions = rolepermissions.rolePermission(token, entity, "WRITE");
////		Claims claims = (Claims) permissions.get("claims");
////		String user =claims.getSubject();
////		String company =(String) claims.get("company");
////		String pwd = bcryptEncoder.encode(user.getOldpwd());
//		
//		authenticationRequest.setUsername(username);
// 		authenticationRequest.setPassword(oldpwd);
//		
//		try {
//				authenticate(authenticationRequest);
//			}
//		catch (Exception e){ 
//			throw e;
////			throw new CloudBaseException(ResponseCode.BAD_CREDENTIALS); 
//			}
//		return userDetailsService.updatePassword(username, newpwd);
//		
//	
	@GetMapping("/health")
	private ResponseEntity<String> healthCheck() {
		return ResponseEntity.ok("Healthy");
	}

	@GetMapping("/.well-known/pki-validation/A3D7A0A49B297C764C02F9AF056B19E7.txt")
	public ResponseEntity<String> getVerifyFile() {

		String fileContent = "37ABDF3767016FC14B871C573D453688EC7DFA0B7405AEDF84E634F027DA19EB\r\n" + "comodoca.com\r\n"
				+ "bfb61055064f7b2";

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");
		return new ResponseEntity<String>(fileContent, headers, HttpStatus.OK);
	}

}