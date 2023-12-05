package com.sopterm.makeawish.dto.user.account;

public record AccountCheckInfo(
        String result
        , String resultMessage
        , String accountName
        , String bankCode
        , String accountNumber
        , String checkDate
) {
}
