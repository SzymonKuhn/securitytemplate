package com.javagda25.securitytemplate.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountPasswordChangeRequest {
    private Long accountId;
    private String resetpassword;

}
