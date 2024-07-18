package com.li.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultPageListVO<T> {
    private List<T> data;
    private Long pages;
    private Long total;
}
