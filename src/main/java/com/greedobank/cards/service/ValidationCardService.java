package com.greedobank.cards.service;

import com.greedobank.cards.model.Card;
import com.greedobank.cards.utils.ResponseMessages;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidationCardService {
    private static final int VALID_STRING_LENGTH = 16;
    private static final int CVV_LENGTH = 3;

    private boolean isValidCardNumber(String number) {
        if (number == null
                || number.trim().length() != VALID_STRING_LENGTH
                || !number.matches("\\d+")) {
            return false;
        }
        int sum = Character.getNumericValue(number.charAt(number.length() - 1));
        int parity = number.length() % 2;
        for (int i = number.length() - 2; i >= 0; i--) {
            int summand = Character.getNumericValue(number.charAt(i));
            if (i % 2 == parity) {
                int product = summand * 2;
                summand = (product > 9) ? (product - 9) : product;
            }
            sum += summand;
        }
        return (sum % 10) == 0;
    }

    private boolean isValidPinCode(String pin) {
        if (pin == null) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("[0-9]{4}");
            Matcher matcher = pattern.matcher(pin);
            return matcher.matches();
        }
    }

    private boolean isValidCvvCode(String cvv) {
        return cvv.length() == CVV_LENGTH;
    }

    private boolean isValidEndDate(OffsetDateTime date) {
        return date.isAfter(OffsetDateTime.now());
    }

    public Builder check(Card card) {
        return new ValidationCardService().new Builder(card);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class Builder {
        private final Card card;
        private boolean isValid = true;

        public Builder number() {
            boolean isValidNumber = isValidCardNumber(card.getNumber());
            System.out.println(ResponseMessages.VALID_NUMBER + isValidNumber);
            isValid = isValid && isValidNumber;
            return this;
        }

        public Builder pin() {
            boolean isValidPin = isValidPinCode(card.getPin());
            System.out.println(ResponseMessages.VALID_PIN + isValidPin);
            isValid = isValid && isValidPin;
            return this;
        }

        public Builder cvv() {
            boolean isValidCvv = isValidCvvCode(card.getCvv());
            System.out.println(ResponseMessages.VALID_CVV + isValidCvv);
            isValid = isValid && isValidCvv;
            return this;
        }

        public Builder endDate() {
            boolean isValidDate = isValidEndDate(card.getEndDate());
            System.out.println(ResponseMessages.VALID_END_DATE + isValidDate);
            isValid = isValid && isValidDate;
            return this;
        }

        public boolean validate() {
            return this.isValid;
        }
    }
}
