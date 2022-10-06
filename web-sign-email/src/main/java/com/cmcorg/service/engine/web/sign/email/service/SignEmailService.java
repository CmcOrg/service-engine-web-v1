package com.cmcorg.service.engine.web.sign.email.service;

import com.cmcorg.engine.web.model.model.dto.NotBlankCodeDTO;
import com.cmcorg.service.engine.web.sign.email.model.dto.*;

public interface SignEmailService {

    String signUpSendCode(EmailNotBlankDTO dto);

    String signUp(SignEmailSignUpDTO dto);

    String signInPassword(SignEmailSignInPasswordDTO dto);

    String updatePasswordSendCode();

    String updatePassword(SignEmailUpdatePasswordDTO dto);

    String updateAccountSendCode();

    String updateAccount(SignEmailUpdateAccountDTO dto);

    String forgotPasswordSendCode(EmailNotBlankDTO dto);

    String forgotPassword(SignEmailForgotPasswordDTO dto);

    String signDeleteSendCode();

    String signDelete(NotBlankCodeDTO dto);

    String bindAccountSendCode(EmailNotBlankDTO dto);

    String bindAccount(SignEmailBindAccountDTO dto);
}
