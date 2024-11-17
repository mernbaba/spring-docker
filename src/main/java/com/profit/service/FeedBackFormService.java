package com.profit.service;

import com.profit.datamodel.FeedBackForm;
import com.profit.dto.FeedBackFormDTO;
import com.profit.dto.ResponseObject;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;
import com.profit.repository.FeedBackFormRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedBackFormService {

    @Autowired
    FeedBackFormRepository feedBackFormRepository;


    public ResponseObject<List<FeedBackFormDTO>> getAllFeedBacks(String company, String branch) {

        List<FeedBackFormDTO> dtoList = new ArrayList<FeedBackFormDTO>();

        feedBackFormRepository.findAllByCompanyCodeAndBranchCode(company, branch)
                .forEach(entity -> {
                    FeedBackFormDTO dto = new FeedBackFormDTO();
                    BeanUtils.copyProperties(entity, dto);
                    dtoList.add(dto);
                });

        return ResponseObject.success(dtoList);
    }

    @Transactional
    public ResponseObject<FeedBackFormDTO> saveFeedBack(FeedBackFormDTO dto, String company, String branch, String username) {

        try {

            FeedBackForm entity = new FeedBackForm();

            entity.setUserCode(dto.getUserCode());
            entity.setUserName(dto.getUserName());
            entity.setComments(dto.getComments());
            entity.setCategory(dto.getCategory());
            entity.setCompanyCode(company);
            entity.setBranchCode(branch);
            entity.setCreatedBy(username);

            feedBackFormRepository.save(entity);

            FeedBackFormDTO savedDto = new FeedBackFormDTO();
            BeanUtils.copyProperties(entity, savedDto);

            return ResponseObject.success(savedDto);

//        } catch (CloudBaseException e) {
//            e.printStackTrace();
//            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
        }
    }

}
