package com.profit.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;


@Data
public class BranchMasterDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long branchId;
    private String branchCode;
    private String branchName;
    private String shortName;
    private String companyCode;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String phoneNo;
    private Boolean headoffice;
    private Boolean isactive;
    private String createdby;
    private Date createddate;
    private String lastmodifiedby;
    private Date lastmodifieddate;
}
