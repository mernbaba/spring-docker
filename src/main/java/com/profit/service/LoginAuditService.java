package com.profit.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.profit.datamodel.LoginAudit;
import com.profit.dto.LoginAuditDTO;
import com.profit.dto.ResponseObject;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;
import com.profit.repository.LoginAuditRepository;
import com.profit.request.JwtRequest;

import jakarta.transaction.Transactional;

@Service
public class LoginAuditService {

	@Autowired
	LoginAuditRepository loginAuditsRepository;

	@Transactional
	public ResponseObject<LoginAuditDTO> save(JwtRequest authenticationRequest, String company, String branch,
			String status) {

		try {
			LoginAudit entity = new LoginAudit();

			entity.setUserName(authenticationRequest.getUsername());
			entity.setIp(authenticationRequest.getMac());
			entity.setFbToken(authenticationRequest.getFbToken());
			entity.setAppName(authenticationRequest.getApp());
			entity.setStatus(status);
			entity.setCompanyCode(company);
			entity.setBranchCode(branch);
			entity.setDeviceId(authenticationRequest.getUid());
			entity.setComments(authenticationRequest.getComments());

			
			loginAuditsRepository.save(entity);
			
			LoginAuditDTO dto = new LoginAuditDTO();
			BeanUtils.copyProperties(entity, dto);

//			LoginAuditDTO loginAudits = new LoginAuditDTO();
//			BeanUtils.copyProperties(entity, loginAudits);
			return ResponseObject.success(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}
}
