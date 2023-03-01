package com.greedobank.cards.facade;

import com.greedobank.cards.dto.CardCreationDTO;
import com.greedobank.cards.dto.CardDTO;
import com.greedobank.cards.dto.CardResetPinDTO;
import com.greedobank.cards.mapper.CardMapper;
import com.greedobank.cards.model.Card;
import com.greedobank.cards.service.CardService;
import com.greedobank.cards.service.ValidationCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Random;

import static com.greedobank.cards.utils.ResponseMessages.BANK_BIN;

@Component
@RequiredArgsConstructor
public class CardFacade {
    private final CardMapper cardMapper;
    private final CardService cardService;
    private final ValidationCardService validationCardService;

    private static final int CARD_NUMBER_LENGTH = 16;
    private static final int PIN_CODE_LENGTH = 4;
    private static final int CVV_LENGTH = 3;
    private static final int DIGIT_UPPER_LIMIT = 10;
    private static final Random RNG = new Random(System.currentTimeMillis());

    public CardDTO create(CardCreationDTO cardCreationDTO) {
        Card card = cardMapper.toCard(cardCreationDTO);
        card.setNumber(generate(BANK_BIN.getDescription(), CARD_NUMBER_LENGTH));
        card.setPin(generate("", PIN_CODE_LENGTH));
        card.setCvv(generate("", CVV_LENGTH));
        card.setEndDate(OffsetDateTime.now().plusYears(3));
        card.setActive(false);
        Card cardCreated = cardService.create(card);
        return cardMapper.toCardDTO(cardCreated);
    }

    public CardDTO getById(int id) {
        Card card = cardService.getById(id);
        return cardMapper.toCardDTO(card);

    }

    public void updatePin(int id, CardResetPinDTO cardResetPinDTO) {
        Card card = cardMapper.toCard(cardResetPinDTO);
        if (!validationCardService.check(card).pin().validate()) {
            card.setPin(generate("", PIN_CODE_LENGTH));
        }
        if (!card.isActive()) {
            card.setActive(true);
        }
       cardService.updatePin(id,card);
    }

    public String generate(String bin, int length) {
        int randomNumberLength = length - (bin.length() + 1);

        StringBuilder builder = new StringBuilder(bin);
        for (int i = 0; i < randomNumberLength; i++) {
            int digit = RNG.nextInt(DIGIT_UPPER_LIMIT);
            builder.append(digit);
        }
        int checkDigit = this.getCheckDigit(builder.toString());
        builder.append(checkDigit);

        return builder.toString();
    }

    private int getCheckDigit(String number) {
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            int digit = Integer.parseInt(number.substring(i, (i + 1)));

            if ((i % 2) == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / DIGIT_UPPER_LIMIT) + (digit % DIGIT_UPPER_LIMIT);
                }
            }
            sum += digit;
        }
        int mod = sum % DIGIT_UPPER_LIMIT;
        return ((mod == 0) ? 0 : DIGIT_UPPER_LIMIT - mod);
    }
}
