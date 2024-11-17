package com.profit.controller;

import com.profit.configuration.JwtTokenUtil;
import com.profit.dto.FeedBackFormDTO;
import com.profit.dto.ResponseObject;
import com.profit.service.FeedBackFormService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedBacks")
public class FeedBackFormController {

    @Autowired
    HttpServletRequest request;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    FeedBackFormService feedBackFormService;

    @GetMapping("/getAll")
    public ResponseObject<List<FeedBackFormDTO>> getAllFeedBacks() {
        String token = request.getHeader("Authorization");
        token = StringUtils.replace(token, "Bearer " , "");

        Claims claims =jwtTokenUtil.getAllClaimsFromToken(token);
        String company = (String)claims.get("company");
        String branch = (String)claims.get("branch");
        return feedBackFormService.getAllFeedBacks(company, branch);
    }

    @PostMapping("/save")
    public ResponseObject<FeedBackFormDTO> saveFeedBack(@RequestBody FeedBackFormDTO dto) {
        String token = request.getHeader("Authorization");
        token = StringUtils.replace(token, "Bearer " , "");

        Claims claims =jwtTokenUtil.getAllClaimsFromToken(token);
        String company = (String)claims.get("company");
        String branch = (String)claims.get("branch");
        String username = claims.getSubject();
        return feedBackFormService.saveFeedBack(dto, company, branch, username);
    }
}
