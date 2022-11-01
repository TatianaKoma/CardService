package com.greedobank.cards;

import com.greedobank.cards.dto.BankAccountCreationDTO;
import com.greedobank.cards.dto.BankAccountDTO;
import com.greedobank.cards.dto.BankAccountGetBalanceDTO;
import com.greedobank.cards.dto.BankAccountReplenishDTO;
import com.greedobank.cards.dto.BankAccountWithdrawDTO;
import com.greedobank.cards.dto.BankAccountWithdrawOnlineDTO;
import com.greedobank.cards.dto.CardCreationDTO;
import com.greedobank.cards.dto.CardDTO;
import com.greedobank.cards.dto.CardResetPinDTO;
import com.greedobank.cards.dto.CardTemplateCreationDTO;
import com.greedobank.cards.dto.CardTemplateCreationUpdateDTO;
import com.greedobank.cards.dto.CardTemplateDTO;
import com.greedobank.cards.dto.TariffDTO;
import com.greedobank.cards.dto.TariffUpdateDTO;
import com.greedobank.cards.model.BankAccount;
import com.greedobank.cards.model.Card;
import com.greedobank.cards.model.CardTemplate;
import com.greedobank.cards.model.Customer;
import com.greedobank.cards.utils.CardType;
import com.greedobank.cards.utils.CurrencyType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class EntityInitializer {

    public static Customer getCustomer(int id) {
        Map<Integer, Customer> customerMap = Map.of(
                1, new Customer(1, "Tom", "Ford", "dzhmur@griddynamics.com",
                        OffsetDateTime.parse("2022-06-30T10:50:30+01:00"), new ArrayList<>(), new ArrayList<>()),
                2, new Customer(2, "Emma", "Grand", "egrand@griddynamics.com",
                        OffsetDateTime.parse("2022-06-30T10:50:30+01:00"), new ArrayList<>(), new ArrayList<>()),
                3, new Customer(3, "Peter", "Smith", "psmith@griddynamics.com",
                        OffsetDateTime.parse("2022-06-30T10:50:30+01:00"), new ArrayList<>(), new ArrayList<>())
        );
        return customerMap.get(id);
    }

    public static CardTemplate getCardTemplate(int id) {
        Map<Integer, CardTemplate> cardTemplateMap = Map.of(
                1, new CardTemplate(1, CardType.GOLD,
                        15.0, 16.0, 17.0, CurrencyType.UAH,
                        OffsetDateTime.parse("2022-06-30T10:50:30+01:00"),
                        OffsetDateTime.parse("2022-06-30T10:50:30+01:00"), 1, new HashSet<>()),
                2, new CardTemplate(2, CardType.PREMIUM,
                        16.0, 17.0, 18.0, CurrencyType.UAH,
                        OffsetDateTime.parse("2022-08-30T10:50:30+01:00"),
                        OffsetDateTime.parse("2022-08-30T10:50:30+01:00"), 1, new HashSet<>()),
                3, new CardTemplate(3, CardType.GOLD,
                        17.0, 18.0, 19.0, CurrencyType.UAH,
                        OffsetDateTime.parse("2022-06-30T10:50:30+01:00"),
                        OffsetDateTime.parse("2022-06-30T10:50:30+01:00"), 1, new HashSet<>())

        );
        return cardTemplateMap.get(id);
    }

    public static CardTemplateDTO getCardTemplateDTO(int id) {
        Map<Integer, CardTemplateDTO> cardTemplateMap = Map.of(
                1, new CardTemplateDTO(1, "GOLD",
                        new TariffDTO(15.0, 16.0, 17.0, "UAH"),
                        OffsetDateTime.parse("2022-06-30T10:50:30+01:00"),
                        OffsetDateTime.parse("2022-06-30T10:50:30+01:00"), 1),
                2, new CardTemplateDTO(2, "PREMIUM",
                        new TariffDTO(16.0, 17.0, 18.0, "UAH"),
                        OffsetDateTime.parse("2022-08-30T10:50:30+01:00"),
                        OffsetDateTime.parse("2022-08-30T10:50:30+01:00"), 1),
                3, new CardTemplateDTO(3, "GOLD",
                        new TariffDTO(17.0, 18.0, 19.0, "UAH"),
                        OffsetDateTime.parse("2022-06-30T10:50:30+01:00"),
                        OffsetDateTime.parse("2022-06-30T10:50:30+01:00"), 1)

        );
        return cardTemplateMap.get(id);
    }

    public static CardCreationDTO getCardCreationDTO(int id) {
        Map<Integer, CardCreationDTO> cardCreationDTOMap = Map.of(
                1, new CardCreationDTO("Tom", "Ford",
                        "UAH", 1, 1),
                2, new CardCreationDTO("Emma", "Grand",
                        "UAH", 2, 2),
                3, new CardCreationDTO("Peter", "Smith",
                        "UAH", 3, 3)
        );
        return cardCreationDTOMap.get(id);
    }

    public static CardDTO getCardDTO(int id) {
        Map<Integer, CardDTO> cardDTOMap = Map.of(
                1, new CardDTO(1, "Tom", "Ford",
                        "4731179262995095", "6536", "368", "09/25", true, "UAH"),
                2, new CardDTO(2, "Emma", "Grand",
                        "4731179262995095", "7773", "545", "09/25", true, "UAH"),
                3, new CardDTO(3, "Peter", "Smith",
                        "4731174547276064", "1234", "754", "09/25", true, "UAH")
        );
        return cardDTOMap.get(id);
    }

    public static Card getCard(int id) {
        Map<Integer, Card> cardMap = Map.of(
                1, new Card(1, "Tom", "Ford", "4731179262995095", "6536", "368",
                        OffsetDateTime.parse("2025-09-30T10:50:30+01:00"), true, CurrencyType.UAH, getCardTemplate(1),
                        getCustomer(1), new BankAccount()),
                2, new Card(2, "Emma", "Grand", "4731179262995095", "6536", "368",
                        OffsetDateTime.parse("2025-09-30T10:50:30+01:00"), true, CurrencyType.UAH, getCardTemplate(2),
                        getCustomer(2), new BankAccount()),
                3, new Card(3, "Peter", "Smith", "4731174547276064", "1234", "754",
                        OffsetDateTime.parse("2025-09-30T10:50:30+01:00"), true, CurrencyType.UAH, getCardTemplate(3),
                        getCustomer(3), new BankAccount())
        );
        return cardMap.get(id);
    }

    public static CardResetPinDTO getCardResetPinDTO(int id) {
        Map<Integer, CardResetPinDTO> cardResetPinDtoMap = Map.of(
                1, new CardResetPinDTO("7777"),
                2, new CardResetPinDTO("0101"),
                3, new CardResetPinDTO("77"));
        return cardResetPinDtoMap.get(id);
    }

    public static CardTemplateCreationUpdateDTO getCardTemplateCreationUpdateDTO() {
        return new CardTemplateCreationUpdateDTO("PREMIUM",
                new TariffUpdateDTO(null, 16.0, 17.0, "UAH"));
    }

    public static CardTemplateCreationDTO getCardTemplateCreationDTO(int id) {
        Map<Integer, CardTemplateCreationDTO> cardTemplateCreationDTOMap = Map.of(
                1, new CardTemplateCreationDTO("GOLD",
                        new TariffDTO(15.0, 16.0, 17.0, "UAH")),
                2, new CardTemplateCreationDTO("PREMIUM",
                        new TariffDTO(16.0, 17.0, 18.0, "UAH")),
                3, new CardTemplateCreationDTO("GOLD",
                        new TariffDTO(17.0, 18.0, 19.0, "UAH"))
        );
        return cardTemplateCreationDTOMap.get(id);
    }

    public static BankAccountDTO getBankAccountDTO(int id) {
        Map<Integer, BankAccountDTO> bankAccountDTOMap = Map.of(
                1, new BankAccountDTO(1, "UA914731171976360791119690404", new BigDecimal(0),
                        "UAH", OffsetDateTime.parse("2022-10-14T09:26:51.3090147+03:00"), true),
                2, new BankAccountDTO(2, "UA604731178341063433740218173", new BigDecimal(0),
                        "UAH", OffsetDateTime.parse("2022-10-14T09:26:51.3090147+03:00"), true),
                3, new BankAccountDTO(3, "UA374731170949466142867399317", new BigDecimal(2500),
                        "UAH", OffsetDateTime.parse("2022-10-13T22:36:44.021197+03:00"), true)
        );
        return bankAccountDTOMap.get(id);
    }

    public static BankAccount getBankAccount(int id) {
        Map<Integer, BankAccount> bankAccountMap = Map.of(
                1, new BankAccount(1, "UA914731171976360791119690404", new BigDecimal(0), CurrencyType.UAH,
                        OffsetDateTime.parse("2022-10-14T09:26:51.3090147+03:00"),
                        true, getCard(1), getCustomer(1)),
                2, new BankAccount(2, "UA914731171976360791119690404", new BigDecimal(0), CurrencyType.UAH,
                        OffsetDateTime.parse("2022-10-14T09:26:51.3090147+03:00"),
                        true, getCard(2), getCustomer(2)),
                3, new BankAccount(3, "UA914731171976360791119690404", new BigDecimal(2500), CurrencyType.UAH,
                        OffsetDateTime.parse("2022-10-14T09:26:51.3090147+03:00"),
                        true, getCard(3), getCustomer(3))
        );
        return bankAccountMap.get(id);
    }

    public static BankAccountCreationDTO getBankAccountCreationDTO(int id) {
        Map<Integer, BankAccountCreationDTO> bankAccountCreationDTOMap = Map.of(
                1, new BankAccountCreationDTO("UAH", 1, 1),
                2, new BankAccountCreationDTO("UAH", 2, 2),
                3, new BankAccountCreationDTO("UAH", 3, 3)
        );
        return bankAccountCreationDTOMap.get(id);
    }

    public static BankAccountReplenishDTO getBankAccountReplenishDTO() {
        return new BankAccountReplenishDTO(new BigDecimal(1000), "4731179262995095");
    }

    public static BankAccountGetBalanceDTO getBankAccountGetBalanceDTO() {
        return new BankAccountGetBalanceDTO(new BigDecimal(1000));
    }

    public static BankAccountWithdrawDTO getBankAccountWithdrawDTO() {
        return new BankAccountWithdrawDTO(new BigDecimal(1000), "4731174547276064", "1234");
    }

    public static BankAccountWithdrawOnlineDTO getBankAccountWithdrawOnlineDTO() {
        return new BankAccountWithdrawOnlineDTO(new BigDecimal(1000), "4731174547276064", "1234",
                "754", "09/25");
    }
}
