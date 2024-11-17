package com.profit.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class FeedBackFormDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String userCode;
    private String userName;
    private String comments;
    private String category;
    private String companyCode;
    private String branchCode;
    private String createdBy;
    private Timestamp createdDate;
}
