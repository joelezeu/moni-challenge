package com.moni.challenge.services;

import com.moni.challenge.domain.FundWalletRequest;
import com.moni.challenge.domain.TransferFundsRequest;
import com.moni.challenge.domain.dto.Response;
import com.moni.challenge.exceptions.ChallengeException;
import com.moni.challenge.models.User;
import com.moni.challenge.models.Wallet;
import com.moni.challenge.repositories.UserRepository;
import com.moni.challenge.repositories.WalletRepository;
import com.moni.challenge.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final ResponseUtils responseUtils;

    public ResponseEntity<Response> fundWallet(FundWalletRequest fundWalletRequest) {
        String phoneNumber = fundWalletRequest.getPhoneNumber();
        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);
        if (userOptional.isEmpty()) {
            throw new ChallengeException("User not found", HttpStatus.NOT_FOUND);
        }

        //Make a call to PayStack to Initialize the Payment and on Successfully
        //Debiting the user and redirecting to the endpoint user's should be funded.

        boolean isPayStackFundingSuccessful = true;

        if (isPayStackFundingSuccessful) {
            User user = userOptional.get();
            Optional<Wallet> walletOptional = walletRepository.findByUser(user);
            if (walletOptional.isEmpty()) {
                throw new ChallengeException("Wallet not found", HttpStatus.NOT_FOUND);
            }
            Wallet wallet = walletOptional.get();
            wallet.setBalance(fundWalletRequest.getAmount());
            wallet.setUser(user);
            wallet.setPreviousBalance(wallet.getBalance());
            wallet.setReference(fundWalletRequest.getReference());

            walletRepository.save(wallet);

            //Log into Transctions

            return responseUtils.getResponse(true, "Wallet Funded Successful");
        }
        throw new ChallengeException("Payment failed. Please try again", HttpStatus.EXPECTATION_FAILED);
    }

    public ResponseEntity<Response> transferFunds(TransferFundsRequest transferFundsRequest) {
        BigDecimal amount = transferFundsRequest.getAmount();
        Optional<User> creditOptional = userRepository.findByPhoneNumber(transferFundsRequest.getCreditPhoneNumber());
        if (creditOptional.isEmpty()) {
            throw new ChallengeException("Credit account not found", HttpStatus.NOT_FOUND);
        }
        Optional<User> debitOptional = userRepository.findByPhoneNumber(transferFundsRequest.getDebitPhoneNumber());
        if (debitOptional.isEmpty()) {
            throw new ChallengeException("Debit account not found", HttpStatus.NOT_FOUND);
        }
        User creditUser = creditOptional.get();
        User deditUser = debitOptional.get();

        Optional<Wallet> creditWalletOptional = walletRepository.findByUser(creditUser);
        if (creditWalletOptional.isEmpty()) {
            throw new ChallengeException("Credit Wallet not found", HttpStatus.NOT_FOUND);
        }

        Wallet walletCr = creditWalletOptional.get();
        if (walletCr.getBalance().compareTo(amount) < 0) {
            throw new ChallengeException("Insufficient Balance", HttpStatus.EXPECTATION_FAILED);
        }

        Optional<Wallet> deditWalletOptional = walletRepository.findByUser(deditUser);
        if (deditWalletOptional.isEmpty()) {
            throw new ChallengeException("Debit Wallet not found", HttpStatus.NOT_FOUND);
        }

        Wallet walletDr = deditWalletOptional.get();

        BigDecimal creditBalance = walletCr.getBalance().subtract(amount);
        walletCr.setBalance(creditBalance);
        walletCr.setPreviousBalance(walletCr.getBalance());

        walletRepository.save(walletCr);

        BigDecimal debitBalance = walletDr.getBalance().add(amount);
        walletDr.setBalance(debitBalance);
        walletDr.setPreviousBalance(walletDr.getBalance());

        walletRepository.save(walletDr);

        return responseUtils.getResponse(true, "Funds Transfer Successful");

    }
}
