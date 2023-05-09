package com.sopterm.makeawish.dto.cake;

public record Amount(
        int total,
        int tax_free,
        int vat,
        int point,
        int discount,
        int green_deposit
) {
}
