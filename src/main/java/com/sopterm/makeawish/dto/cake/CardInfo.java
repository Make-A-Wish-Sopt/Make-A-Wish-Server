package com.sopterm.makeawish.dto.cake;

public record CardInfo(
        String purchase_corp,
        String purchase_corp_code,
        String issuer_corp,
        String issuer_corp_code,
        String kakaopay_purchase_corp,
        String kakaopay_purchase_corp_code,
        String kakaopay_issuer_corp,
        String kakaopay_issuer_corp_code,
        String bin,
        String card_type,
        String install_month,
        String approved_id,
        String card_mid,
        String interest_free_install,
        String card_item_code
) {
}
