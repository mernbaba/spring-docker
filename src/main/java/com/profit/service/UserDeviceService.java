package com.profit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.profit.datamodel.SecUser;
import com.profit.datamodel.UserDevice;
import com.profit.dto.CustomerMasterDTO;
import com.profit.dto.ResponseObject;
import com.profit.dto.RoleMasterDTO;
import com.profit.dto.StaffMasterDTO;
import com.profit.dto.UserDeviceDTO;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;
import com.profit.repository.SecUserRepository;
import com.profit.repository.UserDeviceRepository;
import com.profit.request.ChangeDeviceRequest;

import jakarta.transaction.Transactional;

@Service
public class UserDeviceService {

	@Autowired
	UserDeviceRepository userDeviceRepository;

	@Autowired
	SecUserRepository secUserRepository;
	
	public ResponseObject<List<UserDeviceDTO>> getAll(String company, String branch) {
		
		List<UserDeviceDTO> dtoList=new ArrayList<UserDeviceDTO>();
		
		userDeviceRepository.findAllByCompanyCodeAndBranchCode(company, branch)
				.forEach(entity -> {
					UserDeviceDTO dto = new UserDeviceDTO();
					BeanUtils.copyProperties(entity, dto);
					dtoList.add(dto);
				});
		
		return ResponseObject.success(dtoList);
	}

//	@Transactional
//	public ResponseObject<UserDeviceDTO> save(Object object, Object object2, String company, String branch, String username) {
//		ResponseCode error = null;
//		try {
//			String userCode = null;
//
//			UserDevice entity = new UserDevice();
//
////			StaffMasterDTO staffMasterDTO;
//			CustomerMasterDTO customerMasterDTO;
//			
////			System.out.println("Received DTO class: " + dto.getClass().getName());
//
////			if (dto instanceof StaffMasterDTO) {
////				staffMasterDTO = (StaffMasterDTO) dto;
////				userCode = staffMasterDTO.getStaffCode();
////				entity.setUserCode(staffMasterDTO.getStaffCode());
////				entity.setUserType(staffMasterDTO.getStaffType());
////				entity.setNewDeviceId(staffMasterDTO.getDeviceId());
////
////			} else if (dto instanceof CustomerMasterDTO) {
////				customerMasterDTO = (CustomerMasterDTO) dto;
////				userCode = customerMasterDTO.getCustomerCode();
////				entity.setUserCode(customerMasterDTO.getCustomerCode());
////				entity.setUserType("CUSTOMER");
////				entity.setNewDeviceId(customerMasterDTO.getDeviceId());
////			}
//			
////			if(ObjectUtils.isNotEmpty(object) && ObjectUtils.isEmpty(object2)) {
////				userCode = object.getStaffCode();
////				entity.setUserCode(object.getStaffCode());
////				entity.setUserType(object.getStaffType());
////				entity.setNewDeviceId(object.getDeviceId());
////			}else if(ObjectUtils.isNotEmpty(object2) && ObjectUtils.isEmpty(object)){
////				userCode = object2.getCustomerCode();
////				entity.setUserCode(object2.getCustomerCode());
////				entity.setUserType("CUSTOMER");
////				entity.setNewDeviceId(object2.getDeviceId());
////			}else {
////				error = ResponseCode.BAD_REQUEST;
////				throw new CloudBaseException(error);
////			}
//			
//			String oldDeviceId = secUserRepository.findByUserCode(userCode).getDeviceId();
//			entity.setOldDeviceId(oldDeviceId);
//			entity.setBranchCode(branch);
//			entity.setCompanyCode(company);
//			entity.setCreatedBy(username);
//			
//			userDeviceRepository.save(entity);
//
//			UserDeviceDTO userDeviceDTO = new UserDeviceDTO();
//			BeanUtils.copyProperties(entity, userDeviceDTO);
//
//			return ResponseObject.success(userDeviceDTO);
//			
//		} catch (Exception e) {
//			if(error != null) {
//				throw new CloudBaseException(error);
//			}
//			e.printStackTrace();
//			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
//		}
//	}

	@Transactional
	public ResponseObject<UserDeviceDTO> update(UserDeviceDTO dto, String company, String branch, String username) {
		// TODO Auto-generated method stub
		try {
			UserDevice device = userDeviceRepository.findById(dto.getId())
					.orElseThrow(() -> new CloudBaseException(ResponseCode.USER_NOT_FOUND));

			BeanUtils.copyProperties(dto, device);
			device.setLastModifiedBy(username);

			if (device.getStatus().equalsIgnoreCase("APPROVED")) {
				SecUser secUserEntity = secUserRepository.findByUserCode(device.getUserCode());
				secUserEntity.setExcused(device.getNewDeviceId());

				userDeviceRepository.save(device);
				secUserRepository.save(secUserEntity);
			}


			UserDeviceDTO deviceDto = new UserDeviceDTO();
			BeanUtils.copyProperties(device, deviceDto);

			return ResponseObject.success(deviceDto);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}

	@Transactional
	public ResponseObject<UserDeviceDTO> save(ChangeDeviceRequest dto, String username) {
		
		try {
			UserDevice entity = new UserDevice();

			
			entity.setUserCode(dto.getUserCode());
			entity.setUserType(dto.getStaffType());
			
			String oldDeviceId = secUserRepository.findByUserCode(dto.getUserCode()).getDeviceId();
			entity.setOldDeviceId(oldDeviceId);
			entity.setNewDeviceId(dto.getNewDeviceId());
			entity.setStatus("PENDING");
			entity.setName(dto.getName());
			entity.setPhoneNumber(dto.getPhoneNumber());
			entity.setDeviceName(dto.getDeviceName());
			entity.setBranchCode(dto.getBranchCode());
			entity.setCompanyCode(dto.getCompanyCode());
			entity.setCreatedBy(username);
			
			userDeviceRepository.save(entity);
			
			UserDeviceDTO userDeviceDTO = new UserDeviceDTO();
			BeanUtils.copyProperties(entity, userDeviceDTO);
			
			return ResponseObject.success(userDeviceDTO);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}
}
