package com.profit.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.profit.datamodel.BranchMaster;
import com.profit.datamodel.CompanyMaster;
import com.profit.datamodel.CustomerMaster;
import com.profit.datamodel.Messages;
import com.profit.datamodel.Otp;
import com.profit.datamodel.PrefixGenerator;
import com.profit.datamodel.RoleMapping;
import com.profit.datamodel.SecUser;
import com.profit.datamodel.StaffMaster;
import com.profit.datamodel.UserDevice;
import com.profit.dto.CustomerMasterDTO;
import com.profit.dto.ResponseObject;
import com.profit.dto.SecUserDTO;
import com.profit.dto.StaffMasterDTO;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;
import com.profit.repository.BranchMasterRepository;
import com.profit.repository.CompanyMasterRepository;
import com.profit.repository.CustomerMasterRepository;
import com.profit.repository.MessagesRepository;
import com.profit.repository.OtpRepository;
import com.profit.repository.PrefixGeneratorRepository;
import com.profit.repository.RoleMappingRepository;
import com.profit.repository.SecUserRepository;
import com.profit.repository.StaffMasterRepository;
import com.profit.repository.UserDeviceRepository;
import com.profit.request.CompanyRegister;
import com.profit.request.JwtRequest;
import com.profit.response.JwtResponse;

import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

@Service
public class SecUserService {

	@Autowired
	CompanyMasterRepository companyMasterRepository;

	@Autowired
	BranchMasterRepository branchMasterRepository;

	@Autowired
	SecUserRepository secUserRepository;

	@Autowired
	StaffMasterRepository staffMasterRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	PrefixGeneratorService prefixGeneratorService;

	@Autowired
	private PrefixGeneratorRepository prefixGeneratorRepository;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
	private CustomerMasterRepository customerMasterRepository;

	@Autowired
	RoleMappingRepository roleMappingRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MessagesRepository messagesRepository;

	@Autowired
	private OtpRepository otpRepository;
	@Autowired
	private OtpService otpService;
	
	@Autowired
	LoginAuditService loginAuditService;
	
	@Autowired
	private UserDeviceRepository userDeviceRepository;
	
	@Value("${app.version}")
	private String appVersion;

	public ResponseObject<List<SecUserDTO>> getAllUsers(String company, String branch) {
		List<SecUser> entityList = secUserRepository.findAllByCompanyCodeAndBranchCode(company, branch);
		List<SecUserDTO> dtoList = new ArrayList<>();

		for (SecUser entity : entityList) {
			SecUserDTO dto = new SecUserDTO();
			BeanUtils.copyProperties(entity, dto);
			
			dto.setPassword(null);
			
			dtoList.add(dto);
		}

		return ResponseObject.success(dtoList);
	}

	@Transactional
	public ResponseObject<String> registerCompany(CompanyRegister companyRegister) {

		try {
			if (companyRegister != null && ObjectUtils.isNotEmpty(companyRegister)) {

				// CompanyMaster_save
				CompanyMaster companyMaster = new CompanyMaster();
				companyMaster.setCompanyName(companyRegister.getCompanyName());
				companyMaster.setCompanyCode("COMP-" + (companyMasterRepository.findAll().size() + 1));
				companyMaster.setIsMultiBranch(companyRegister.getHasMultiBranch());
				companyMaster.setCreatedBy("system");
				companyMaster.setCreatedDate(new Date());

				companyMaster = companyMasterRepository.save(companyMaster);

				// SecUser_save
				SecUser secUser = new SecUser();
				secUser.setUserName(companyRegister.getPhoneNumber());
				secUser.setUserType("STAFF");
				secUser.setEmail(companyRegister.getEmail());
				String encodedPassword = passwordEncoder.encode(companyRegister.getPassword());
				secUser.setPassword(encodedPassword);
				secUser.setUserCode("");
				secUser.setIsActive(true);
				secUser.setIsAdmin(true);
				secUser.setPhoneNumber(companyRegister.getPhoneNumber());
				secUser.setCreatedBy("system");
				secUser.setCreatedDate(new Date());

				secUser.setCompanyCode(companyMaster.getCompanyCode());

				secUserRepository.save(secUser);

				// BranchMaster_save
				BranchMaster branchMaster = new BranchMaster();

				branchMaster.setBranchName(companyRegister.getBranchName());
//				branchMaster.setBranchCode(companyRegister.getBranchName().replaceAll(" ", ""));
				branchMaster.setBranchCode("BRNC-" + (branchMasterRepository.findAll().size() + 1));
				branchMaster.setShortName(companyRegister.getBranchSrtCode());
				branchMaster.setCompanyCode(companyMaster.getCompanyCode());
				branchMaster.setBranchLock(false);
				branchMaster.setIsActive(true);
				branchMaster.setIsHeadOffice(true);
				branchMaster.setCreatedDate(new Date());
				branchMaster.setCreatedBy(secUser.getUserName());

				branchMasterRepository.save(branchMaster);

//				StaffMaster_save
				StaffMaster staffMaster = new StaffMaster();
				staffMaster.setBranchCode(branchMaster.getBranchCode());
				staffMaster.setCompanyCode(branchMaster.getCompanyCode());
				staffMaster.setGmail(companyRegister.getEmail());
				staffMaster.setPhone(companyRegister.getPhoneNumber());
				staffMaster.setStaffName(companyRegister.getStaffName());
				staffMaster.setStaffType("STAFF");
				staffMaster.setUserType("OWNER");
				staffMaster.setStaffCode("");
				staffMaster.setIsActive(true);
				staffMaster.setCreatedBy("system");

				staffMasterRepository.save(staffMaster);

//				PrefixGen_save
				PrefixGenerator newPrefixGenerator = new PrefixGenerator();
				newPrefixGenerator.setCompanyCode(companyMaster.getCompanyCode());
				newPrefixGenerator.setBranchCode(branchMaster.getBranchCode());
				newPrefixGenerator.setPrefixCode(staffMaster.getStaffType());
				newPrefixGenerator.setPrefixDate(new Date());
				newPrefixGenerator.setPrefix((branchMaster.getShortName() + "s").toUpperCase());
				newPrefixGenerator.setLastGenerated(1L);
				newPrefixGenerator.setPadLength("3");

				prefixGeneratorRepository.save(newPrefixGenerator);

				staffMaster.setStaffCode(newPrefixGenerator.getPrefix() + "-"
						+ String.format("%03d", newPrefixGenerator.getLastGenerated()));
				staffMasterRepository.save(staffMaster);

//				SavingBranchCodeInSecUser
				secUser.setBranchCode(branchMaster.getBranchCode());
				secUser.setUserCode(staffMaster.getStaffCode());
				secUserRepository.save(secUser);

				RoleMapping roleEntity = new RoleMapping();
				roleEntity.setRoleCode("ADMIN");
				roleEntity.setUserCode(staffMaster.getStaffCode());
				roleEntity.setCompanyCode(companyMaster.getCompanyCode());
				roleEntity.setBranchCode(branchMaster.getBranchCode());
				roleEntity.setCreatedBy("system");

				roleMappingRepository.save(roleEntity);

				return ResponseObject.success("Company Registerd Successfully");
			} else {
				throw new CloudBaseException(ResponseCode.OBJECT_CAN_NOT_BE_NULL);
			}
		} catch (Exception e) {
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}

	public ResponseObject<JwtResponse> authenticateUser(JwtRequest authenticationRequest) throws Exception {

		SecUser secUser = secUserRepository.findByUserName(authenticationRequest.getUsername());
//		if(authenticationRequest.getVersion().equals(appVersion)) {
//			throw new CloudBaseException(ResponseCode.)
//		}
		if (secUser == null) {
			System.out.println(ResponseCode.USER_NOT_FOUND);
			throw new CloudBaseException(ResponseCode.USER_NOT_FOUND);
		}
		boolean check = passwordEncoder.matches(authenticationRequest.getPassword(), secUser.getPassword());

		if (check) {
			if(authenticationRequest.getApp() != null && authenticationRequest.getApp().equalsIgnoreCase("Portal")) {
				if(secUser.getUserType() == null || !secUser.getUserType().equalsIgnoreCase("STAFF")) {
					throw new CloudBaseException(ResponseCode.INVALID_PERMISSIONS);
				}
			}
			JwtResponse jwtresponse = userDetailsService.getToken(authenticationRequest).getBody();
			return ResponseObject.success(jwtresponse);
			
		} else {
			loginAuditService.save(authenticationRequest, null, null, "FAILURE");
			throw new CloudBaseException(ResponseCode.INVALID_CREDENTIALS);
		}

	}

	@Transactional
	public ResponseObject<SecUserDTO> saveSecUser(SecUserDTO secUserDTO) throws Exception {

		ResponseCode error = null;
		try {
			SecUser entity = new SecUser();

			BeanUtils.copyProperties(secUserDTO, entity);

			if (secUserDTO != null && ObjectUtils.isNotEmpty(secUserDTO)) {

				CustomerMaster custEntity = customerMasterRepository.findByPhoneNumber(secUserDTO.getPhoneNumber());

				StaffMaster staffEntity = staffMasterRepository.findByPhone(secUserDTO.getPhoneNumber());

				if ((custEntity == null && staffEntity != null) || (staffEntity == null && custEntity != null)) {
					if (custEntity != null) {
						entity.setCompanyCode(custEntity.getCompanyCode());
						entity.setBranchCode(custEntity.getBranchCode());
						entity.setUserCode(custEntity.getCustomerCode());
						entity.setUserType("CUSTOMER");
						entity.setCreatedBy("customer");
//						entity.setEmail(custEntity.getEmail());
						custEntity.setEmail(secUserDTO.getEmail());
						customerMasterRepository.save(custEntity);
					} else {
						entity.setCompanyCode(staffEntity.getCompanyCode());
						entity.setBranchCode(staffEntity.getBranchCode());
						entity.setUserCode(staffEntity.getStaffCode());
						entity.setUserType("STAFF");
						entity.setCreatedBy("staff");
//						entity.setEmail(staffEntity.getGmail());
						staffEntity.setGmail(secUserDTO.getEmail());
						staffMasterRepository.save(staffEntity);
					}
				} else {
					error = ResponseCode.MOBILE_NUM_NOT_FOUND;
					throw new CloudBaseException(error);
//					throw new Exception("Mobile number not registered at admin portal");
				}

				/*if (StringUtils.isNotBlank(dto.getEmail())) {
					if (!custEntity.getEmail().equals(dto.getEmail())) {
						throw new RuntimeException("email different form admin");
					}
				}*/

				entity.setMultiBranch(false);
				entity.setAccountLocked(false);
				entity.setPasswordExpired(false);
				entity.setIsAdmin(false);
				entity.setIsActive(true);
				entity.setFirstLogin(false);
				entity.setUserName(secUserDTO.getPhoneNumber());
				entity.setPassword(passwordEncoder.encode(secUserDTO.getPassword()));
				secUserRepository.save(entity);

				SecUserDTO dto = new SecUserDTO();
				BeanUtils.copyProperties(entity, dto);
				return ResponseObject.success(dto);
			} else {
				error = ResponseCode.OBJECT_CAN_NOT_BE_NULL;
				throw new CloudBaseException(error);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}

	public ResponseObject<?> getProfile(String company, String branch, String username) {

		try {
			SecUser secUser = secUserRepository.findByUserName(username);
			UserDevice device = userDeviceRepository.getLatestUserCode(secUser.getUserCode());
			CompanyMaster companyMaster = companyMasterRepository.findByCompanyCode(company);

			if (secUser != null) {
				if (secUser.getUserType().equalsIgnoreCase("CUSTOMER")) {
					CustomerMaster customer = customerMasterRepository.findByPhoneNumber(username);
					CustomerMasterDTO dto = new CustomerMasterDTO();
					BeanUtils.copyProperties(customer, dto);
					dto.setCompanyName(companyMaster.getCompanyName());
					dto.setDeviceId(secUser.getDeviceId());
					if(device != null) {dto.setStatus(device.getStatus());}
					dto.setExcused(secUser.getExcused());
					return ResponseObject.success(dto);
				} else if (secUser.getUserType().equalsIgnoreCase("STAFF")) {
					StaffMaster staff = staffMasterRepository.findByPhone(username);
					StaffMasterDTO dto = new StaffMasterDTO();
					BeanUtils.copyProperties(staff, dto);
					dto.setCompanyName(companyMaster.getCompanyName());
					dto.setDeviceId(secUser.getDeviceId());
					dto.setIsAdmin(secUser.getIsAdmin());
					if(device != null) {dto.setStatus(device.getStatus());}
					dto.setExcused(secUser.getExcused());
					return ResponseObject.success(dto);
				} else {
					throw new CloudBaseException(ResponseCode.USER_NOT_FOUND);
				}
			} else {
				throw new CloudBaseException(ResponseCode.USER_NOT_FOUND);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.USER_NOT_FOUND);
		}

	}

	public ResponseObject<String> changePassword(String newPwd, String company, String branch, String userName) {
//	SecUser secUser=	secUserRepository.findByUserName(userName);
//	if(secUser !=null && ObjectUtils.isNotEmpty(secUser)) {
		String pwd = passwordEncoder.encode(newPwd);
		try {
			secUserRepository.updatePassword(userName, company, branch, pwd);
			return new ResponseObject<String>("Password Updated Successful", ResponseCode.SUCCESS);
		} catch (Exception e) {
			throw new CloudBaseException(ResponseCode.INVALID_QUERY);
		}

	}

	@Value("${spring.mail.username}")
	private String sender;

	@Transactional
	public ResponseObject<ResponseCode> forgotPassword(String email) {
		try {

			Otp otpEntity = new Otp();
			Messages msg = new Messages();

			SecUser secUser = secUserRepository.findByEmail(email);
			String userName = null;

			// if user already exist going forward for generating otp
			if (secUser != null && ObjectUtils.isNotEmpty(secUser)) {

				if (secUser.getUserType().equalsIgnoreCase("CUSTOMER")) {
					userName = customerMasterRepository.findByPhoneNumber(secUser.getPhoneNumber()).getCustomerName();
				} else if (secUser.getUserType().equalsIgnoreCase("STAFF")) {
					userName = staffMasterRepository.findByPhone(secUser.getPhoneNumber()).getStaffName();
				}

				// taking random 6 char
				String OTP = otpService.generateOTP();
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message);
				helper.setFrom(sender);

				String subject = "Here's your One Time Password (OTP) - Expire in 10 minute!";

				String content = "Hello " + userName + "." + "<br>"
						+ "For security reason, you're required to use the following " + "One Time Password to login:"
						+ "<br><b>" + OTP + "</br>" + "<br>" + "Note: this OTP is set to expire in 10 minute.";

				String mesgss = "Hello " + userName + "." + "\n"
						+ "For security reason, you're required to use the following " + "One Time Password to login:"
						+ "\n\n" + OTP + "\n" + "\n" + "Note: this OTP is set to expire in 10 minute.";

				// 10 min added
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MINUTE, +10);

				// setting the values
				otpEntity.setPartyCode(secUser.getUserName());
				otpEntity.setPartyType(secUser.getUserType());
				otpEntity.setActivity("PASSWORD_AUTH");
				otpEntity.setActivityId(secUser.getId());
				otpEntity.setOtp(OTP);
				otpEntity.setOtpGentime(new Date());
				otpEntity.setOtpExptime(cal.getTime());
				otpEntity.setOtpStatus("GENERATED");
				otpEntity.setAppName("Teja");
				otpEntity.setCompanyCode(secUser.getCompanyCode());
				otpEntity.setCreatedby(secUser.getUserName());
				otpEntity.setCreateddate(new Date());

				// setting the values
				msg.setMessageDate(new Date());
				msg.setPartyCode(secUser.getUserName());
				msg.setPartyType(secUser.getUserType());
				msg.setMessageType("PASSWORD_AUTH");
				msg.setOtp(OTP);
				msg.setMessage(mesgss);
				msg.setCompanyCode(secUser.getCompanyCode());

				helper.setSubject(subject);

				helper.setText(content, true);
				helper.setTo(email);

				otpRepository.save(otpEntity);
				messagesRepository.save(msg);
				// send mail
				mailSender.send(message);

				return ResponseObject.success(ResponseCode.OTP_SENT_TO_YOUR_EMAIL);
			} else {
				throw new CloudBaseException(ResponseCode.USER_NOT_FOUND);
			}

		} catch (Exception e) {
			throw new CloudBaseException(ResponseCode.USER_NOT_FOUND);
		}
	}

	public ResponseObject<ResponseCode> resetPassword(String email, String pwd, String otp) {

		try {
			SecUser secUser = secUserRepository.findByEmail(email);
			if (secUser != null && ObjectUtils.isNotEmpty(secUser)) {

				Otp otpEntity = otpRepository.findByPartyCodeAndPartyTypeAndCompanyCode(secUser.getUserName(),
						secUser.getUserType(), secUser.getCompanyCode());

				Date currentDate = new Date();
				Date exp = otpEntity.getOtpExptime();

				long currentTime = currentDate.getTime();
				long expTime = exp.getTime();

				if (currentTime <= expTime) {

					if (otpEntity.getOtp().equalsIgnoreCase(otp)) {
						pwd = passwordEncoder.encode(pwd);

						secUserRepository.updatePassword(secUser.getUserName(), secUser.getCompanyCode(),
								secUser.getBranchCode(), pwd);
						otpRepository.updateOtpStatusByIdAndPartyCode("SUCCESS", otpEntity.getId(),
								otpEntity.getPartyCode());

					} else {
						throw new CloudBaseException(ResponseCode.INVALID_OTP);
					}

				} else {
					otpRepository.updateOtpStatusByIdAndPartyCode("EXPIRED", otpEntity.getId(), otpEntity.getPartyCode());
					throw new CloudBaseException(ResponseCode.OTP_EXPIRED);
				}
				return ResponseObject.success(ResponseCode.PASSWORD_UPDATED_SUCESSFULLY);
			} else {
				throw new CloudBaseException(ResponseCode.USER_NOT_FOUND);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.USER_NOT_FOUND);
		}
		
		
	}

	public ResponseObject<ResponseCode> delete(String userCode, String username) {
		try {
			SecUser secUser = secUserRepository.findByUserCode(userCode);
			if (secUser != null) {
				if (secUser.getUserType().equalsIgnoreCase("CUSTOMER")) {
					customerMasterRepository.updateCustomer(secUser.getUserCode(), username);
				}
				secUserRepository.deleteById(secUser.getId());
				return ResponseObject.success(ResponseCode.SUCCESS);
			} else {
				throw new CloudBaseException(ResponseCode.USER_NOT_FOUND);
			}
		}catch (CloudBaseException e) {
			throw e;
		}catch (Exception e) {
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
		
	}

}
