package br.com.gustavokt.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Farm {
    Integer id;
    String name;
    int values;
    Producer producer;
}
