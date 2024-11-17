package com.profit.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PrefixGeneratorDTO {
    private Long id;
    private String companyCode;
    private String branchCode;
    private String prefixCode;
    private Date prefixDate;
    private Date prefix;
    private Long lastGenerated;
    private String padLength;
}
