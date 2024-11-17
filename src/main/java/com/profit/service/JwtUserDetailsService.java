package com.profit.service;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.profit.configuration.JwtTokenUtil;
//import com.profit.datamodel.EntryForm;
import com.profit.datamodel.SecUser;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;
//import com.profit.repository.EntryFormRepository;
import com.profit.repository.SecUserRepository;
import com.profit.request.JwtRequest;
import com.profit.response.JwtResponse;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

//    @Autowired
//    private EntryFormRepository entryFormRepository;

    @Autowired
    private SecUserRepository secUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        SecUser user = secUserRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid User");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    public ResponseEntity<JwtResponse> getToken(JwtRequest ar) {
        JwtResponse response = generateJwtToken(ar.getUsername());
        return ResponseEntity.ok(response);
    }

    public JwtResponse generateJwtToken(String username) {
        SecUser user = secUserRepository.findByUserName(username);
        validateUser(user);
        
        UserDetails userDetails = loadUserByUsername(username);
        String token = jwtTokenUtil.generateToken(userDetails, user);

        JwtResponse jwtResponse = new JwtResponse(token, user.getUserType(), user.getCompanyCode(), user.getBranchCode());
        // Use a logger instead of System.out.println
        // logger.info("Generated JWT token for user: {}", username);

        return jwtResponse;
    }

//    @Transactional
//    public Boolean updatePassword(String user, String newPwd) {
//        String password = bcryptEncoder.encode(newPwd);
//        EntryForm entryForm = entryFormRepository.findByName(user);
//        
//        if (entryForm != null) {
//            entryFormRepository.updatePassword(user, password);
//            return true;
//        }
//        return false; // Return false if user is not found
//    }

    private void validateUser(SecUser user) {
        if (user == null) {
            throw new CloudBaseException(ResponseCode.USER_NOT_FOUND);
        }

//        if (user.isAccountLocked()) {
//            throw new UserValidationException("User account is locked.");
//        }
//
//        if (user.isPasswordExpired()) {
//            throw new UserValidationException("User password has expired.");
//        }
//
        if (!user.getIsActive()) {
            throw new CloudBaseException(ResponseCode.INACTIVE_USER);
        }

        if (user.getLastWorkingDate() != null && user.getLastWorkingDate().before(new Date())) {
            throw new CloudBaseException(ResponseCode.DEACTIVATED_USER);
        }
    }
}