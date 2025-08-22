package com.nthuy.demo_erm.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResultPaginationDTO<T> {
    private List<T> content;
    private Meta meta;
}
