package com.mfpe.memberService.service;


import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mfpe.memberService.exceptions.InvalidClaimIdException;
import com.mfpe.memberService.exceptions.InvalidMemberIdException;
import com.mfpe.memberService.model.Bills;
import com.mfpe.memberService.model.ClaimDetails;
import com.mfpe.memberService.model.ClaimStatusDTO;
import com.mfpe.memberService.proxy.ClaimsClient;
import com.mfpe.memberService.repository.BillsRepo;


import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
public class ClaimStatusAndDetails{

    @Autowired
    private BillsRepo billsRepo;
    
    @Autowired
    private ClaimsClient claimsClient;
    
    // This method fetch claim Status from Claims MicroService
    public  ClaimStatusDTO fetchClaimStatus(String claimId, String token)
    {
        log.info("Inside the fetch Claim Status method : Begin");
        ClaimStatusDTO claimStatusDTO  = new ClaimStatusDTO();
        
        log.info("Claims Client Called to get Claim Status");
        try {
        	claimStatusDTO = claimsClient.statusDetails(claimId,token);
        }
        catch(Exception e)
        {
            throw new InvalidClaimIdException("The Claim Id is Invalid");
        }
        
        return claimStatusDTO;
    }
   
    
    // This method fetch Bills from Bills table
    public  Bills fetchBills(String memberId)
    {
        log.info("Inside the fetch Bills method : Begin");
        
        Optional<Bills> bill = billsRepo.findById(memberId);
        if(!bill.isPresent() )
		{
			throw new InvalidMemberIdException("The Member Id is Invalid");
		}
        
        return bill.get();
    }

    
    public  ClaimStatusDTO fetchClaimDetails( ClaimDetails claimDetails,String token)
    {
        log.info("Inside the fetch Claim Status method : Begin");
        ClaimStatusDTO claimStatusDTO  = new ClaimStatusDTO();
        claimDetails.setClaimId(claimDetails.getMemberId()+claimDetails.getPolicyId()+claimDetails.getBenefitId()+generateClaimId().substring(0, 4).toUpperCase());
        log.info("claim id generated :"+claimDetails.getClaimId());
        try {
        	log.info("attempting to connect to claim service to submit claim");
        	claimStatusDTO = claimsClient.onSbmitStatusDetails(claimDetails,token);
        	log.info("response came from claim service");
        }
        catch(Exception e)
        {
        	log.info("some exception occured while contacting policy service");
        	throw e;
        }
        return claimStatusDTO;
    }
    
    
    public String generateClaimId()
    {
    	return UUID.randomUUID().toString();
    }
        
}
