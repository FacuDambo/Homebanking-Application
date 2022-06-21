package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {SpringApplication.run(HomebankingApplication.class, args);}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientService clientService,
									  AccountService accountService,
									  TransactionService transactionService,
									  LoanService loanService,
									  CardService cardService) {return (args) -> {
			//======================================CLIENTS=========================================
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("asd123"));
			Client client2 = new Client("Sergio", "Perez", "checo@redbull.com", passwordEncoder.encode("asd1234"));
			Client admin = new Client("admin", "admin", "admin@gmail.com", passwordEncoder.encode("admin123"));

			clientService.saveClient(client1);
			clientService.saveClient(client2);
			clientService.saveClient(admin);

			//======================================ACCOUNTS=========================================
			Account account1 = new Account("VIN001", 5000, LocalDateTime.now(), AccountType.CHECKING, client1, false);
			Account account2 = new Account("VIN002", 7500, LocalDateTime.now().plusDays(1), AccountType.SAVING, client1, false);
			Account account3 = new Account("VIN003", 5000, LocalDateTime.now().plusDays(240), AccountType.SAVING, client1, false);
			Account account4 = new Account("VIN004", 19500, LocalDateTime.now().plusDays(56), AccountType.SAVING, client2, false);

			accountService.saveAccount(account1);
			accountService.saveAccount(account2);
			accountService.saveAccount(account3);
			accountService.saveAccount(account4);

			//======================================TRANSACTIONS=========================================
			Transaction transaction1 = new Transaction(TransactionType.DEBIT, -2742, "Clothing", "Shopping", LocalDateTime.now().minusDays(1), account1.getBalance() + -2742 , account1);
			account1.setBalance(account1.getBalance() - 2742);
			Transaction transaction2 = new Transaction(TransactionType.CREDIT, 5320, "Deposit", "Deposit", LocalDateTime.now(), account1.getBalance() + 5320, account1);
			account1.setBalance(account1.getBalance() + 5320);
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, -1984, "Gas", "Taxes", LocalDateTime.now().minusDays(1), account1.getBalance() + -1984, account1);
			account1.setBalance(account1.getBalance() - 1984);
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, -2034, "Riot", "Groceries", LocalDateTime.now().minusDays(20), account1.getBalance() + -2034, account1);
			account1.setBalance(account1.getBalance() - 2034);

			Transaction transaction5 = new Transaction(TransactionType.CREDIT, 3652, "Deposit", "Deposit", LocalDateTime.now().minusDays(5), account2.getBalance() + 3652, account2);
			account2.setBalance(account2.getBalance() + 3652);
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, -362, "Books", "Shopping", LocalDateTime.now().minusDays(2), account2.getBalance() + -362, account2);
			account2.setBalance(account2.getBalance() - 362);
			Transaction transaction7 = new Transaction(TransactionType.DEBIT, -352, "Riot", "Gaming", LocalDateTime.now().minusDays(16), account2.getBalance() + -352, account2);
			account2.setBalance(account2.getBalance() - 352);
			Transaction transaction8 = new Transaction(TransactionType.DEBIT, -452, "Transaction", "Other", LocalDateTime.now().minusDays(8), account2.getBalance() + -452, account2);
			account2.setBalance(account2.getBalance() - 452);

			Transaction transaction9 = new Transaction(TransactionType.DEBIT, -522, "Expenses", "Taxes", LocalDateTime.now().minusDays(4), account3.getBalance() + -522, account3);
			account3.setBalance(account3.getBalance() -522);
			Transaction transaction10 = new Transaction(TransactionType.DEBIT, -855, "Doctor", "Health", LocalDateTime.now().minusDays(3), account3.getBalance() + -855, account3);
			account3.setBalance(account3.getBalance() - 855);
			Transaction transaction11 = new Transaction(TransactionType.DEBIT, -992, "Steam ", "Gaming", LocalDateTime.now().minusDays(95), account3.getBalance() + -992, account3);
			account3.setBalance(account3.getBalance() - 992);
			Transaction transaction12 = new Transaction(TransactionType.CREDIT, 3680, "Deposit", "Deposit", LocalDateTime.now().minusDays(15), account3.getBalance() + 3680, account3);
			account3.setBalance(account3.getBalance() + 3680);

			transactionService.saveTransaction(transaction1);
			transactionService.saveTransaction(transaction2);
			transactionService.saveTransaction(transaction3);
			transactionService.saveTransaction(transaction4);
			transactionService.saveTransaction(transaction5);
			transactionService.saveTransaction(transaction6);
			transactionService.saveTransaction(transaction7);
			transactionService.saveTransaction(transaction8);
			transactionService.saveTransaction(transaction9);
			transactionService.saveTransaction(transaction10);
			transactionService.saveTransaction(transaction11);
			transactionService.saveTransaction(transaction12);

			accountService.saveAccount(account1);
			accountService.saveAccount(account2);
			accountService.saveAccount(account3);
			accountService.saveAccount(account4);
			//======================================LOANS=========================================
			Loan mortgage = new Loan("Mortgage", 500000, List.of(12, 24, 36, 48, 60), 40);
			Loan personal = new Loan("Personal", 100000, List.of(6, 12, 24), 15);
			Loan car = new Loan("Car", 300000, List.of(6, 12, 24, 36), 30);

			loanService.saveLoan(mortgage);
			loanService.saveLoan(personal);
			loanService.saveLoan(car);

			//======================================CLIENT LOANS=========================================
			ClientLoan clientloan1 = new ClientLoan(400000, 60, client1, mortgage);
			ClientLoan clientloan2 = new ClientLoan(50000, 12, client1, personal);
			ClientLoan clientloan3 = new ClientLoan(10000, 24, client1, car);
			ClientLoan clientloan4 = new ClientLoan(400000, 36, client2, car);

			loanService.saveClientLoan(clientloan1);
			loanService.saveClientLoan(clientloan2);
			loanService.saveClientLoan(clientloan3);
			loanService.saveClientLoan(clientloan4);

			//======================================CARDS=========================================
			Card card1 = new Card(CardType.DEBIT, CardColor.GOLD, 4548320069587845L, 582, LocalDate.now().plusMonths(5), LocalDate.now().plusYears(5), "Inventory", client2, false);
			Card card2 = new Card(CardType.DEBIT, CardColor.TITANIUM, 4684340087779421L, 966,LocalDate.now().minusYears(5), LocalDate.now().minusMonths(2), "Software Subscriptions", client1, false);
			Card card3 = new Card(CardType.CREDIT, CardColor.SILVER, 45129873588014403L, 156,LocalDate.now(), LocalDate.now().plusYears(5), "Travel", client2, false);
			Card card4 = new Card(CardType.DEBIT, CardColor.SILVER, 5684231598140235L, 345, LocalDate.now(), LocalDate.now().plusYears(5), "Streaming Subscriptions", client1, false);
			Card card5 = new Card(CardType.DEBIT, CardColor.GOLD, 3214360084519800L, 474, LocalDate.now(), LocalDate.now().plusYears(5), "Savings", client1, false);

			cardService.saveCard(card1);
			cardService.saveCard(card2);
			cardService.saveCard(card3);
			cardService.saveCard(card4);
			cardService.saveCard(card5);
		};
	}
}
