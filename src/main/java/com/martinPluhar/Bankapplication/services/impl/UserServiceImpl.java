package com.martinPluhar.Bankapplication.services.impl;

import com.martinPluhar.Bankapplication.config.JwtTokenProvider;
import com.martinPluhar.Bankapplication.dto.*;
import com.martinPluhar.Bankapplication.entity.Role;
import com.martinPluhar.Bankapplication.entity.User;
import com.martinPluhar.Bankapplication.repository.UserRepository;
import com.martinPluhar.Bankapplication.services.intfc.EmailService;
import com.martinPluhar.Bankapplication.services.intfc.TransactionService;
import com.martinPluhar.Bankapplication.services.intfc.UserService;
import com.martinPluhar.Bankapplication.util.AccountUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Služba pro správu uživatelských účtů a bankovních transakcí.
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    /**
     * Vytvoří nový bankovní účet pro zadaného uživatele na základě požadavku.
     *
     * @param userRequest Požadavek na vytvoření účtu obsahující informace o uživateli.
     * @return Odpověď od banky, která obsahuje informace o vytvořeném účtu nebo chybovou zprávu.
     */
    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        Role userRole = Role.valueOf(String.valueOf(userRequest.getRole()));
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .gander(userRequest.getGander())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .role(String.valueOf(userRole))
                .status("Aktivní")
                .build();
        User savedUser = userRepository.save(newUser);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
                        .build())
                .build();
    }
    public BankResponse login(LoginDto loginDto){
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        EmailDetails loginAlert = EmailDetails.builder()
                .subject("Přihlášení")
                .recipient(loginDto.getEmail())
                .messageBody("Úspěšně jste se přihlásil ke svému účtu. Pokud jste se vědomně nepřihlásil, prosím kontaktujte banku!")
                .build();

        emailService.sendEmailAlert(loginAlert);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_LOGIN_SUCCES)
                .responseMessage(jwtTokenProvider.generateToken(authentication))
                .accountInfo(null)
                .build();
    }
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // Může provádět jen token s autorizací ADMIN
    public BankResponse deleteAccountByEmail(String email) {

        if (userRepository.existsByEmail(email)) {
            userRepository.deleteAccountByEmail(email);
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DELETED_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DELETED_MESSAGE)
                    .accountInfo(null)
                    .build();
        } else {
            throw new RuntimeException("Uživatel s e-mailem " + email + " nebyl nalezen.");
        }
    }


    /**
     * Získá aktuální zůstatek bankovního účtu na základě čísla účtu.
     *
     * @param request Požadavek na získání zůstatku obsahující číslo účtu.
     * @return Odpověď od banky, která obsahuje aktuální zůstatek nebo chybovou zprávu.
     */
    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(foundUser.getAccountNumber())
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
                        .build())
                .build();
    }

    /**
     * Získá jméno uživatele na základě čísla účtu.
     *
     * @param request Požadavek na získání jména obsahující číslo účtu.
     * @return Jméno uživatele nebo chybovou zprávu, pokud účet neexistuje.
     */
    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName();
    }

    /**
     * Připíše zadanou částku na bankovní účet na základě požadavku.
     *
     * @param request Požadavek na připsání částky na účet.
     * @return Odpověď od banky, která obsahuje aktualizovaný zůstatek nebo chybovou zprávu.
     */
    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);

        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();

        // Získání účtů odesílatele a příjemce z requestu nebo z jiných datových struktur
        String senderAccount = request.getSenderAccount();
        String receiverAccount = request.getReceiverAccount();

        transactionService.saveTransaction(transactionDto, senderAccount, receiverAccount);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .build())
                .build();
    }

    /**
     * Provádí bankovní převod mezi dvěma účty na základě požadavku.
     *
     * @param request Požadavek na bankovní převod mezi účty.
     * @return Odpověď od banky, která obsahuje informace o provedeném převodu nebo chybovou zprávu.
     */
    @Override
    public BankResponse transfer(TransferRequest request) {
        boolean isSourceAccountExist = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());

        if (!isSourceAccountExist || !isDestinationAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());

        BigDecimal amountToTransfer = request.getAmount();
        BigDecimal sourceAccountBalance = sourceAccountUser.getAccountBalance();

        if (amountToTransfer.compareTo(sourceAccountBalance) > 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        // Aktualizuje zůstatek zdrojového účtu
        sourceAccountUser.setAccountBalance(sourceAccountBalance.subtract(amountToTransfer));
        userRepository.save(sourceAccountUser);

        // Aktualizuje zůstatek cílového účtu
        BigDecimal destinationAccountBalance = destinationAccountUser.getAccountBalance();
        destinationAccountUser.setAccountBalance(destinationAccountBalance.add(amountToTransfer));
        userRepository.save(destinationAccountUser);

        // Uloží transakci
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(destinationAccountUser.getAccountNumber())
                .transactionType("CREDIT")
                .amount(amountToTransfer)
                .build();
        transactionService.saveTransaction(transactionDto, request.getSourceAccountNumber(), request.getDestinationAccountNumber());

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(null)
                .build();
    }


    /**
     * Debetuje zadanou částku z bankovního účtu na základě požadavku.
     *
     * @param request Požadavek na debetaci částky z účtu.
     * @return Odpověď od banky, která obsahuje aktualizovaný zůstatek nebo chybovou zprávu.
     */
    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        // Zkontroluje, zda účet existuje
        if (!userRepository.existsByAccountNumber(request.getAccountNumber())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User accountToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        BigDecimal debitAmount = request.getAmount();

        // Zkontroluje, zda má účet dostatečný zůstatek pro provedení debetní transakce
        if (accountToDebit.getAccountBalance().compareTo(debitAmount) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        } else {
            // Provede debetní transakci
            accountToDebit.setAccountBalance(accountToDebit.getAccountBalance().subtract(debitAmount));
            userRepository.save(accountToDebit);

            TransactionDto transactionDto = TransactionDto.builder()
                    .accountNumber(accountToDebit.getAccountNumber())
                    .transactionType("DEBIT")
                    .amount(debitAmount)
                    .build();

            transactionService.saveTransaction(transactionDto, request.getSenderAccount(), request.getReceiverAccount());

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(accountToDebit.getFirstName() + " " + accountToDebit.getLastName())
                            .accountBalance(accountToDebit.getAccountBalance())
                            .accountNumber(request.getAccountNumber())
                            .build())
                    .build();
        }
    }



}
