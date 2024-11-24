package com.profit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.profit.datamodel.PrefixGenerator;
import com.profit.datamodel.RoleMapping;
import com.profit.datamodel.SecUser;
import com.profit.datamodel.StaffMaster;
import com.profit.dto.ResponseObject;
import com.profit.dto.StaffMasterDTO;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;
import com.profit.repository.BranchMasterRepository;
import com.profit.repository.PrefixGeneratorRepository;
import com.profit.repository.RoleMappingRepository;
import com.profit.repository.SecUserRepository;
import com.profit.repository.StaffMasterRepository;

import jakarta.transaction.Transactional;

@Service
public class StaffMasterService {

	@Autowired
	StaffMasterRepository staffMasterRepository;

	@Autowired
	BranchMasterRepository branchMasterRepository;

	@Autowired
	PrefixGeneratorRepository prefixGeneratorRepository;

	@Autowired
	SecUserRepository secUserRepository;

	@Autowired
	RoleMappingRepository roleMappingRepository;

	public ResponseObject<List<StaffMasterDTO>> getAll(String company, String branch) {
		List<StaffMaster> entityList = staffMasterRepository.findAllByCompanyCodeAndBranchCode(company, branch);
		List<StaffMasterDTO> dtoList = new ArrayList<StaffMasterDTO>();

		for (StaffMaster entity : entityList) {
			StaffMasterDTO dto = new StaffMasterDTO();
			BeanUtils.copyProperties(entity, dto);
			dtoList.add(dto);
		}
		return ResponseObject.success(dtoList);
	}

	public ResponseObject<List<StaffMasterDTO>> getAllTrainers(String company, String branch) {
		List<StaffMaster> entityList = staffMasterRepository.findAllByCompanyCodeAndBranchCode(company, branch);
		List<StaffMasterDTO> dtoList = new ArrayList<StaffMasterDTO>();

		for (StaffMaster entity : entityList) {
			if (entity.getIsPT()) {
				StaffMasterDTO dto = new StaffMasterDTO();
				BeanUtils.copyProperties(entity, dto);
				dtoList.add(dto);
			}
		}
		return ResponseObject.success(dtoList);
	}

	@Transactional
	public ResponseObject<StaffMasterDTO> save(StaffMasterDTO dto, String company, String branch, String username) {
		ResponseCode error = null;
		try {
			StaffMaster master = staffMasterRepository.findByPhone(dto.getPhone());
			if (master != null) {
				error = ResponseCode.MOBILE_NUMBER_ALREADY_EXISTS;
				throw new CloudBaseException(error);
			} else {
				StaffMaster entity = new StaffMaster();
				PrefixGenerator prefix = prefixGeneratorRepository.findByBranchCodeAndCompanyCodeAndPrefixCode(branch,
						company, "STAFF");
				if (prefix != null) {
					entity.setStaffCode(
							prefix.getPrefix() + "-" + String.format("%03d", prefix.getLastGenerated() + 1));
					prefix.setLastGenerated(prefix.getLastGenerated() + 1);
					prefixGeneratorRepository.save(prefix);
				}
				entity.setAddress(dto.getAddress());
				entity.setStaffName(dto.getStaffName());
				entity.setGender(dto.getGender());
				if (dto.getIsPT()) {
					entity.setIsPT(dto.getIsPT());
				}
				entity.setGmail(dto.getGmail());
				entity.setPhone(dto.getPhone());
				entity.setStaffType("STAFF");
				entity.setUserType(dto.getUserType());
				entity.setProfilePic(dto.getProfilePic());
				entity.setShift(dto.getShift());
				entity.setIsActive(true);
				entity.setBranchCode(branch);
				entity.setCompanyCode(company);
				entity.setCreatedBy(username);

				staffMasterRepository.save(entity);

				RoleMapping role = new RoleMapping();
				role.setRoleCode("STAFF_USER");
				role.setUserCode(entity.getStaffCode());
				role.setBranchCode(branch);
				role.setCompanyCode(company);
				role.setCreatedBy(username);

				roleMappingRepository.save(role);

				StaffMasterDTO staffMasterDTO = new StaffMasterDTO();
				BeanUtils.copyProperties(entity, staffMasterDTO);
				return ResponseObject.success(staffMasterDTO);
			}
		} catch (CloudBaseException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}

	@Transactional
	public ResponseObject<StaffMasterDTO> update(StaffMasterDTO dto, String company, String branch, String username) {
		try {
			Optional<StaffMaster> staff = staffMasterRepository.findById(dto.getId());
			StaffMaster entity = new StaffMaster();

			if (staff.isPresent()) {
				entity = staff.get();
				BeanUtils.copyProperties(dto, entity);
//				if (dto.getIsPT()) {
//					entity.setStaffName(null);
//				}
				entity.setCompanyCode(company);
				entity.setBranchCode(branch);
				entity.setLastModifiedBy(username);
				entity = staffMasterRepository.save(entity);
			}

			SecUser secUser = secUserRepository.findByUserCode(entity.getStaffCode());
			if (secUser != null && ObjectUtils.isNotEmpty(secUser)) {

				if (!secUser.getEmail().equalsIgnoreCase(entity.getGmail())) {
					secUser.setEmail(entity.getGmail());
				}
				if (!secUser.getPhoneNumber().equals(entity.getPhone())) {
					secUser.setPhoneNumber(entity.getPhone());
//					secUser.setUserName(entity.getPhone());
				}
				secUserRepository.save(secUser);

			}

			StaffMasterDTO staffDto = new StaffMasterDTO();
			BeanUtils.copyProperties(entity, staffDto);

			return ResponseObject.success(staffDto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}

}
