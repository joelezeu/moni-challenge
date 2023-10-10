package com.moni.challenge.controllers;

import com.moni.challenge.domain.FundWalletRequest;
import com.moni.challenge.domain.TransferFundsRequest;
import com.moni.challenge.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/fund-wallet")
    public ResponseEntity<?> fundWallet(@RequestBody FundWalletRequest fundWalletRequest) {
        return walletService.fundWallet(fundWalletRequest);
    }
    @PostMapping("/transfer-funds")
    public ResponseEntity<?> transferFunds(@RequestBody TransferFundsRequest transferFundsRequest){
        return walletService.transferFunds(transferFundsRequest);
    }
    @PostMapping("/request-funds")
    public ResponseEntity<?> requestFunds(FundWalletRequest fundWalletRequest){
        return walletService.requestFunds(fundWalletRequest);
    }
}
