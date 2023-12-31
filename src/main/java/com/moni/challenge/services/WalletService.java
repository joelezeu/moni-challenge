package com.moni.challenge.services;

import com.moni.challenge.domain.FundWalletRequest;
import com.moni.challenge.domain.TransferFundsRequest;
import com.moni.challenge.domain.dto.Response;
import com.moni.challenge.exceptions.ChallengeException;
import com.moni.challenge.models.RequestFund;
import com.moni.challenge.models.RequestFundStatus;
import com.moni.challenge.models.User;
import com.moni.challenge.models.Wallet;
import com.moni.challenge.repositories.RequestFundRepository;
import com.moni.challenge.repositories.UserRepository;
import com.moni.challenge.repositories.WalletRepository;
import com.moni.challenge.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final ResponseUtils responseUtils;
    private final RequestFundRepository requestFundRepository;

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
            wallet.setPreviousBalance(wallet.getBalance());
            wallet.setBalance(fundWalletRequest.getAmount());
            wallet.setUser(user);
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

        Optional<Wallet> deditWalletOptional = walletRepository.findByUser(deditUser);
        if (deditWalletOptional.isEmpty()) {
            throw new ChallengeException("Debit Wallet not found", HttpStatus.NOT_FOUND);
        }

        Wallet walletDr = deditWalletOptional.get();
        if (walletDr.getBalance().compareTo(amount) < 0) {
            throw new ChallengeException("Insufficient Balance", HttpStatus.EXPECTATION_FAILED);
        }

        BigDecimal creditBalance = walletDr.getBalance().subtract(amount);
        walletDr.setPreviousBalance(walletDr.getBalance());
        walletDr.setBalance(creditBalance);

        walletRepository.save(walletDr);

        Wallet walletCr = creditWalletOptional.get();

        BigDecimal debitBalance = walletCr.getBalance().add(amount);
        walletCr.setPreviousBalance(walletCr.getBalance());
        walletCr.setBalance(debitBalance);


        walletRepository.save(walletCr);

        //Log Transactions.

        return responseUtils.getResponse(true, "Funds Transfer Successful");

    }

    public ResponseEntity<Response> requestFunds(FundWalletRequest fundWalletRequest) {
        Optional<User> creditOptional = userRepository.findByPhoneNumber(fundWalletRequest.getPhoneNumber());
        if (creditOptional.isEmpty()) {
            throw new ChallengeException("Credit account not found", HttpStatus.NOT_FOUND);
        }
        User user = creditOptional.get();
        Optional<Wallet> creditWalletOptional = walletRepository.findByUser(creditOptional.get());
        if (creditWalletOptional.isEmpty()) {
            throw new ChallengeException("Credit Wallet not found", HttpStatus.NOT_FOUND);
        }

        RequestFund requestFund = new RequestFund();
        requestFund.setAmount(fundWalletRequest.getAmount());
        requestFund.setRecipient(user.getPhoneNumber());
        requestFund.setStatus(RequestFundStatus.PENDING);

        requestFundRepository.save(requestFund);


        String paymentLink = "http://link_to_share.com";

        return responseUtils.getResponse(true, "Kindly use this link to credit your account " + paymentLink);
    }
}
