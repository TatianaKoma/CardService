package com.greedobank.cards.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class JsonErrorResponse {
    @NonNull
    private String reason;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> details;
}
