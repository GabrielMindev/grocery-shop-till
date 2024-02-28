package com.example.groceryshoptill.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class SpecialDeal {
    private String type;
    private List<String> items;


}