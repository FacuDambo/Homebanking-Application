package com.mindhub.homebanking.services;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CardService {

    List<CardDTO> getCards();
    CardDTO getCard(@PathVariable Long id);
    Card findById(Long id);
    void saveCard(Card card);
}
