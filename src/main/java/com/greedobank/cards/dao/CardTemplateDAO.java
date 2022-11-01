package com.greedobank.cards.dao;

import com.greedobank.cards.model.CardTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTemplateDAO extends JpaRepository<CardTemplate, Integer> {
}
