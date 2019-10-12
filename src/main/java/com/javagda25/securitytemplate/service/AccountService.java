package com.javagda25.securitytemplate.service;

import com.javagda25.securitytemplate.model.Account;
import com.javagda25.securitytemplate.model.AccountRole;
import com.javagda25.securitytemplate.model.dto.AccountPasswordChangeRequest;
import com.javagda25.securitytemplate.repository.AccountRepository;
import com.javagda25.securitytemplate.repository.AccountRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;
    private AccountRoleService accountRoleService;
    private AccountRoleRepository accountRoleRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, AccountRoleService accountRoleService, AccountRoleRepository accountRoleRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRoleService = accountRoleService;
        this.accountRoleRepository = accountRoleRepository;
    }


    public boolean register(Account account) {
        if (accountRepository.existsByUsername(account.getUsername())) {
            return false;
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAccountRoles(accountRoleService.getDefaultRoles());
        accountRepository.save(account);
        return true;
    }

    public List<Account> getAll () {
        return accountRepository.findAll();
    }

    public Optional<Account> getById(Long id) {
        return accountRepository.findById(id);
    }

    public void deleteAccount (Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (!account.isAdmin()) {
                accountRepository.delete(account);
            }
        }
    }

//    public void lockAccount(Account account) {
//        account.setLocked(true);
//        accountRepository.save(account);
//    }

    public void toggleLock(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setLocked(!account.isLocked());
            accountRepository.save(account);
        }
    }

    public void changePassword(AccountPasswordChangeRequest accountPasswordChangeRequest) {
        Long id = accountPasswordChangeRequest.getAccountId();
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setPassword(passwordEncoder.encode(accountPasswordChangeRequest.getResetpassword()));
            accountRepository.save(account);
        }
    }

    public void editRoles(Long accountId, HttpServletRequest req) {
        if (accountRepository.existsById(accountId)) {
            Account account = accountRepository.getOne(accountId);

            Map<String, String[]> formParameters = req.getParameterMap();
            Set<AccountRole> collectionOfRoles = new HashSet<>();

            for (String roleName : formParameters.keySet()) {
                String[] values = formParameters.get(roleName);

                if (values[0].equals("on")) {
                    Optional<AccountRole> accountRoleOptional = accountRoleRepository.findByName(roleName);

                    if (accountRoleOptional.isPresent()) {
                        collectionOfRoles.add(accountRoleOptional.get());
                    }
                }
            }

            account.setAccountRoles(collectionOfRoles);

            accountRepository.save(account);

        }
    }
}
