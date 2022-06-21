package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;


    @Override
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }
    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }
    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
    @Override
    public List<Account> findByClient(Client client) {
        return accountRepository.findByClient(client);
    }
    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }
}
