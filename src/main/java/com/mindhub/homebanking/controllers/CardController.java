package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
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
}