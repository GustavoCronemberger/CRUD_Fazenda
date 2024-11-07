package br.com.gustavokt.domain;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Farm {
    Integer id;
    String name;
    int values;
    Producer producer;
}
