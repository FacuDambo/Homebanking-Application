package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    List<LoanDTO> getLoans();
    Loan findById(Long id);
    void saveClientLoan(ClientLoan clientLoan);
    void saveLoan(Loan loan);
}
