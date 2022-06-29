package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.PaymentApplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import static com.mindhub.homebanking.utils.Utils.getRandomLong;
import static com.mindhub.homebanking.utils.Utils.getRandomRepeat;


@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/cards")
    public List<CardDTO> getCards() {
        return cardService.getCards();
    };

    @GetMapping("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id){
        return cardService.getCard(id);
    };

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(
            @RequestParam CardType type, @RequestParam CardColor color, @RequestParam String cardName, Authentication authentication) {


        Client client = clientService.findByEmail(authentication.getName());

        List<Card> creditCards = client.getCards().stream().filter(card -> card.getCardType() == CardType.CREDIT && !card.isDisabled()).collect(Collectors.toList());
        List<Card> debitCards = client.getCards().stream().filter(card -> card.getCardType() == CardType.DEBIT && !card.isDisabled()).collect(Collectors.toList());

        if (creditCards.size() >= 3 && type == CardType.CREDIT) {
            return new ResponseEntity<>("Reached max credit cards", HttpStatus.FORBIDDEN);
        }else if (debitCards.size() >= 3 && type == CardType.DEBIT) {
            return new ResponseEntity<>("Reached max debit cards", HttpStatus.FORBIDDEN);
        }

        Card card = new Card(type, color, getRandomLong(3000000000000000L, 5999999999999999L), getRandomRepeat(1, 999), LocalDate.now(), LocalDate.now().plusYears(5), cardName, client, false);
        cardService.saveCard(card);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current/cards")
    public ResponseEntity<Object> disableCard(@RequestParam Long id, Authentication authentication) {

        Card obtainCard = cardService.findById(id);
        Client client = clientService.findByEmail(authentication.getName());

        if (!client.getCards().contains(obtainCard)) {
            return new ResponseEntity<>("This is not your card", HttpStatus.FORBIDDEN);
        }

        obtainCard.setDisabled(true);
        cardService.saveCard(obtainCard);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/authenticated")
    public ResponseEntity<?> isAuthenticated(Authentication authentication) {
        if (authentication != null) {
            return new ResponseEntity<>("authenticated", HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>("not authenticated", HttpStatus.ACCEPTED);
    }

    @CrossOrigin
    @Transactional
    @PostMapping ("/cards/payments")
    public ResponseEntity<Object> paymentsPostman(@RequestBody PaymentApplicationDTO payment, Authentication authentication){

        if(payment.getNumberCard() == 0 || payment.getDescription().isEmpty()){
            return new ResponseEntity<>("Missing data",HttpStatus.FORBIDDEN);
        }

        Card card = cardService.getCardByNumber(payment.getNumberCard());

        if(card == null){
            return new ResponseEntity<>("The card is invalid",HttpStatus.FORBIDDEN);
        }
        Client client = card.getClient();
        Account account = client.getAccounts().stream().filter(account1 -> account1.getBalance()> payment.getAmount()).findFirst().orElse(null);

        if(payment.getCvv() <= 0){
            return new ResponseEntity<>("The cvv is wrong",HttpStatus.FORBIDDEN);
        }

        if(payment.getAmount() <= 0){
            return new ResponseEntity<>("Invalid amount",HttpStatus.FORBIDDEN);
        }

        if (payment.getCvv() != card.getCvv()){
            return new ResponseEntity<>("The cvv does not match the cvv of the card",HttpStatus.FORBIDDEN);
        }

        if(card.getThruDate().isBefore(LocalDate.now())){
            return new ResponseEntity<>("The card is expired",HttpStatus.FORBIDDEN);
        }

        if (account == null){
            return new ResponseEntity<>("The account associated with the card is not available",HttpStatus.FORBIDDEN);
        }

        if (account.getBalance()<=0 ){
            return new ResponseEntity<>("Account cannot have 0 balance",HttpStatus.FORBIDDEN);

        }

        if (account.getBalance() < payment.getAmount()){
            return new ResponseEntity<>("Insufficient balance to carry out this operation",HttpStatus.FORBIDDEN);
        }


        account.setBalance(account.getBalance()- payment.getAmount());
        accountService.saveAccount(account);

        transactionService.saveTransaction(new Transaction(TransactionType.DEBIT, -payment.getAmount(), payment.getDescription() +" "+payment.getNumberCard(), "Other", LocalDateTime.now(), account.getBalance() ,account));


        return new ResponseEntity<>("Payment accepted",HttpStatus.ACCEPTED);
    }
}