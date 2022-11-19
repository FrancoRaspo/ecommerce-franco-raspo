package com.ecommerce.francoraspo.models.requests;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString


public class ProductoItemRequest {
    @NotNull @Min(1) @Max(999)
    private Long cantidad;
}
