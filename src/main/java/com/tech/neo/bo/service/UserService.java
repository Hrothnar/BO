package com.tech.neo.bo.service;

import com.tech.neo.bo.domain.entity.Account;
import com.tech.neo.bo.domain.entity.User;
import com.tech.neo.bo.exception.NotEnoughMoney;
import com.tech.neo.bo.repository.AccountRepository;
import com.tech.neo.bo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service()
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Autowired()
    public UserService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional()
    public void updateUserPhoneNumber(long id, String oldPhoneNumber, String newPhoneNumber) {
        userRepository.updatePhoneNumber(id, oldPhoneNumber, newPhoneNumber);
    }

    @Transactional()
    public void addUserPhoneNumber(long id, String phoneNumber) {
        Optional<User> optional = userRepository.findById(getCurrentUserId());
        optional.ifPresent((user) -> {
            user.getPhoneNumbers().add(phoneNumber);
            userRepository.save(user);
        });
    }

    @Transactional()
    public void removePhoneNumber(long id, String phoneNumber) {
        User user = userRepository.findById(id).orElseThrow();
        Set<String> emails = user.getEmails();
        Set<String> phoneNumbers = user.getPhoneNumbers();
        if (emails.size() + phoneNumbers.size() == 1) {
            throw new RuntimeException("Should be at least one contact left");
        }
        user.getPhoneNumbers().remove(phoneNumber);
        userRepository.save(user);
    }

    @Transactional()
    public void updateUserEmail(long id, String oldEmail, String newEmail) {
        userRepository.updateEmail(id, oldEmail, newEmail);
    }

    @Transactional()
    public void addEmail(long id, String email) {
        Optional<User> optional = userRepository.findById(getCurrentUserId());
        optional.ifPresent((user) -> {
            user.getEmails().add(email);
            userRepository.save(user);
        });
    }

    @Transactional()
    public void removeEmail(long id, String email) {
        User user = userRepository.findById(id).orElseThrow();
        Set<String> emails = user.getEmails();
        Set<String> phoneNumbers = user.getPhoneNumbers();
        if (emails.size() + phoneNumbers.size() == 1) {
            throw new RuntimeException("Should be at least one contact left");
        }
        user.getEmails().remove(email);
        userRepository.save(user);
    }

    @Transactional()
    public List<User> findPeopleYoungerThan(LocalDate birthDate) {
        return userRepository.findPeopleYoungerThan(birthDate);
    }

    @Transactional()
    public List<User> findAllByFullName(String fullName) {
        return userRepository.findByFullNameContaining(fullName);
    }

    @Transactional()
    public User findOneByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow();
    }

    @Transactional()
    public User findOneByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Transactional()
    public void transferMoney(long currentUserId, long accountId, double amount) {
        User user = userRepository.findById(currentUserId).orElseThrow();
        Account accountFrom = user.getAccount();
        Account accountTo = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("This account not found"));
        if (accountFrom.getBalance().doubleValue() < amount) {
            throw new NotEnoughMoney("Not enough money for transfer");
        }
        accountFrom.setBalance(BigDecimal.valueOf(accountFrom.getBalance().doubleValue() - amount));
        accountTo.setBalance(BigDecimal.valueOf(accountTo.getBalance().doubleValue() + amount));
        accountRepository.saveAll(List.of(accountFrom, accountTo));
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
