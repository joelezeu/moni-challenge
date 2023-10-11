package com.moni.challenge.services;

import com.moni.challenge.domain.UserRequest;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final WalletRepository walletRepository;

    private final ResponseUtils responseUtils;

    public ResponseEntity<Response> createUser(UserRequest userRequest) {
        //Create User
        String phoneNumber = userRequest.getPhoneNumber();
        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);
        if (userOptional.isPresent()) {
            throw new ChallengeException("User already exist", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setAddress(userRequest.getAddress());
        user.setDob(userRequest.getDob());
        user.setLastName(userRequest.getLastName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setFirstName(userRequest.getFirstName());
        user.setEmail(userRequest.getEmail());

        user = userRepository.save(user);

        //Create Wallet

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setPreviousBalance(BigDecimal.ZERO);
        wallet.setReference(UUID.randomUUID().toString());

        wallet = walletRepository.save(wallet);

        return responseUtils.getResponse(true, "User Created Successfully, Balance is " + wallet.getBalance());

    }
}
