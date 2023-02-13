package de.rode.onlineshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

    private Double absolute;
    private Double percentage;

    private Product product;
    private ProductGroup productGroup;
    private CustomerGroup customerGroup;

}
