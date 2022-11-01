package com.greedobank.cards.model;

import com.greedobank.cards.utils.RoleTitle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Role {

    private int id;
    private RoleTitle title;
}
