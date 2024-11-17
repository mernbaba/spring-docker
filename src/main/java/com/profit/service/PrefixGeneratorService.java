package com.profit.service;

import com.profit.datamodel.PrefixGenerator;
import com.profit.repository.CompanyMasterRepository;
import com.profit.repository.PrefixGeneratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PrefixGeneratorService {

    @Autowired
    private PrefixGeneratorRepository prefixGeneratorRepository;
    
    @Autowired
    CompanyMasterRepository companyMasterRepository;

    public List<PrefixGenerator> getAllPrefixGenerators() {
        return prefixGeneratorRepository.findAll();
    }

    public PrefixGenerator getPrefixGeneratorById(Long id) {
        return prefixGeneratorRepository.findById(id).orElse(null);
    }

    public PrefixGenerator createPrefixGenerator(PrefixGenerator prefixGenerator) {
        // Set created date if not already set
        if (prefixGenerator.getPrefixDate() == null) {
            prefixGenerator.setPrefixDate(new Date());
        }
        return prefixGeneratorRepository.save(prefixGenerator);
    }

    public PrefixGenerator updatePrefixGenerator(Long id, PrefixGenerator prefixGenerator) {
        if (prefixGeneratorRepository.existsById(id)) {
            prefixGenerator.setId(id);
            return prefixGeneratorRepository.save(prefixGenerator);
        }
        return null;
    }

    public void deletePrefixGenerator(Long id) {
        prefixGeneratorRepository.deleteById(id);
    }
    
    public String generateNewCode(String name) {
    	
    	
    	return null;
    }

	/*public String generateNewCode(Object object) {
	//        Optional<PrefixGenerator> optionalPrefixGenerator = 
	//            prefixGeneratorRepository.findByCompanyCode(companyCode);
	    
	    String newCode;
	
	    if (optionalPrefixGenerator.isPresent()) {
	        PrefixGenerator prefixGenerator = optionalPrefixGenerator.get();
	        long lastGenerated = companyMasterRepository.findAll().size();
	//            long lastGenerated = prefixGenerator.getLastGenerated() != null ? prefixGenerator.getLastGenerated() : 0;
	        newCode = "COMP-" + (lastGenerated + 1);
	
	        prefixGenerator.setLastGenerated(lastGenerated + 1);
	        prefixGeneratorRepository.save(prefixGenerator);
	    } else {
	        newCode = companyCode + "1"; // Starting point if no previous code exists
	
	        PrefixGenerator newPrefixGenerator = new PrefixGenerator();
	        newPrefixGenerator.setCompanyCode(companyCode);
	        newPrefixGenerator.setBranchCode("");
	        newPrefixGenerator.setPrefixCode("");
	        newPrefixGenerator.setPrefixDate(new Date());
	        newPrefixGenerator.setPrefix("");
	        newPrefixGenerator.setLastGenerated(1L);
	        newPrefixGenerator.setPadLength("3");
	
	        prefixGeneratorRepository.save(newPrefixGenerator);
	    }
	
	    return newCode;
	}*/
}