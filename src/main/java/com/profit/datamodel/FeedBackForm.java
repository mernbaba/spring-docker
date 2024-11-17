package com.profit.datamodel;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "tb_feedback_form", uniqueConstraints = @UniqueConstraint(name = "uk_feedback_form", columnNames = {"id, branch_code, company_code"}))
public class FeedBackForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_code", nullable = false, length = 56)
    private String userCode;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "comments", nullable = false, length = 500)
    private String comments;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "company_code", nullable = false)
    private String companyCode;

    @Column(name = "branch_code", nullable = false)
    private String branchCode;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date")
    @CreationTimestamp
    private Timestamp createdDate;
    
    @ManyToOne
	@JoinColumn(name = "user_code", referencedColumnName = "user_code", insertable = false, updatable = false)
	private SecUser secUser;

    @ManyToOne
    @JoinColumns({@JoinColumn(name = "branch_code", referencedColumnName = "branch_code", insertable = false, updatable = false),
            @JoinColumn(name = "company_code", referencedColumnName = "company_code", insertable = false, updatable = false)})
    private BranchMaster branchMaster;
}
