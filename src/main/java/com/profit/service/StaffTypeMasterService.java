package com.profit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.profit.datamodel.StaffTypeMaster;
import com.profit.dto.ResponseObject;
import com.profit.dto.StaffTypeMasterDTO;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;
import com.profit.repository.StaffTypeMasterRepository;

import jakarta.transaction.Transactional;

@Service
public class StaffTypeMasterService {

	@Autowired
	private StaffTypeMasterRepository staffTypeMasterRepository;

	@Transactional
	public ResponseObject<StaffTypeMasterDTO> save(StaffTypeMasterDTO staffTypeMasterDTO, String company, String branch,
			String userName) {
		try {

			StaffTypeMaster staffTypeMaster = new StaffTypeMaster();
			BeanUtils.copyProperties(staffTypeMasterDTO, staffTypeMaster);
			staffTypeMaster.setStaffTypeCode(staffTypeMasterDTO.getStaffTypeCode().toUpperCase());
			staffTypeMaster.setCompanyCode(company);
			staffTypeMaster.setBranchCode(branch);
			staffTypeMaster.setCreatedBy(userName);

			staffTypeMaster = staffTypeMasterRepository.save(staffTypeMaster);
			StaffTypeMasterDTO response = new StaffTypeMasterDTO();
			BeanUtils.copyProperties(staffTypeMaster, response);

			return ResponseObject.success(response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}

	public ResponseObject<List<StaffTypeMasterDTO>> getAll(String company, String branch) {
		try {

			List<StaffTypeMaster> staffMasterList = staffTypeMasterRepository.findByCompanyCodeAndBranchCode(company,branch);
			List<StaffTypeMasterDTO> responseLsit = new ArrayList<>();
			staffMasterList.forEach(staff -> {
				StaffTypeMasterDTO dto = new StaffTypeMasterDTO();
				BeanUtils.copyProperties(staff, dto);
				responseLsit.add(dto);
			});
			return new ResponseObject<>(responseLsit, ResponseCode.SUCCESS);
		} catch (Exception e) {
			throw new CloudBaseException(ResponseCode.INVALID_QUERY);
		}
	}

}
