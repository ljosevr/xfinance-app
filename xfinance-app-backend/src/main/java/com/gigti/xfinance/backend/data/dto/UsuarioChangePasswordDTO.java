package com.gigti.xfinance.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioChangePasswordDTO {

    private String oldPassword;
    private String newPassword1;
    private String newPassword2;

}
