package com.Hacktualidad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private String photo;
    private Integer stock;
    private Boolean active;
    private CategoryDTO category;


}