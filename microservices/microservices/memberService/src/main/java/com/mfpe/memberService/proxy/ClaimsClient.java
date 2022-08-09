package com.mfpe.memberService.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mfpe.memberService.model.ClaimDetails;
import com.mfpe.memberService.model.ClaimStatusDTO;


@FeignClient(name = "claim-service", url = "${Claims.URL}")
public interface ClaimsClient {

		@GetMapping("/getClaimStatus/{claimId}")
		ClaimStatusDTO statusDetails(@PathVariable String claimId,@RequestHeader(name = "Authorization", required = true)String token);
		
		@PostMapping("/submitClaim")
		ClaimStatusDTO onSbmitStatusDetails(@RequestBody ClaimDetails claimDetails,@RequestHeader(name = "Authorization", required = true)String token);
}
