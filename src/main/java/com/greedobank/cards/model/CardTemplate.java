package com.greedobank.cards.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.greedobank.cards.utils.CardType;
import com.greedobank.cards.utils.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_template_id", updatable = false, nullable = false)
    private int id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CardType type;

    @Column(name = "issue_cost")
    private Double issueCost;

    @Column(name = "service_cost")
    private Double serviceCost;

    @Column(name = "reissue_cost")
    private Double reissueCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_type")
    private CurrencyType currency;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "created_by_id")
    private int createdById;

    @OneToMany(mappedBy = "cardTemplate", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Card> cardList;
}
