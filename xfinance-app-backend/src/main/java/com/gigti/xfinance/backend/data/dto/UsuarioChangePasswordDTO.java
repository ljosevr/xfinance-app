package com.gigti.xfinance.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioChangePasswordDTO {

    @Size(min = 4)
    private String oldPassword;
    @Size(min = 4)
    private String newPassword1;
    @Size(min = 4)
    private String newPassword2;

}
