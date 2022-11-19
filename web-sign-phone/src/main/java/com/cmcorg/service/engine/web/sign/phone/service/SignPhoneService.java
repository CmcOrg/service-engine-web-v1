package com.cmcorg.service.engine.web.sign.phone.service;

import com.cmcorg.engine.web.model.model.dto.NotBlankCodeDTO;
import com.cmcorg.service.engine.web.sign.phone.dto.*;

public interface SignPhoneService {

    String signUpSendCode(PhoneNotBlankDTO dto);

    String signUp(SignPhoneSignUpDTO dto);

    String signInPassword(SignPhoneSignInPasswordDTO dto);

    String updatePasswordSendCode();

    String updatePassword(SignPhoneUpdatePasswordDTO dto);

    String updateAccountSendCode();

    String updateAccount(SignPhoneUpdateAccountDTO dto);

    String forgotPasswordSendCode(PhoneNotBlankDTO dto);

    String forgotPassword(SignPhoneForgotPasswordDTO dto);

    String signDeleteSendCode();

    String signDelete(NotBlankCodeDTO dto);

    String bindAccountSendCode(PhoneNotBlankDTO dto);

    String bindAccount(SignPhoneBindAccountDTO dto);

    String signInSendCode(PhoneNotBlankDTO dto);

    String signInCode(SignPhoneSignInCodeDTO dto);

}
