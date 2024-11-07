package br.com.gustavokt.domain;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Fazenda {
    Integer id;
    String name;
    int values;
    Producer producer;
}
