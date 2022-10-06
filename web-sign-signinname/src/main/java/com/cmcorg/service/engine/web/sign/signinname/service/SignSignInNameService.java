package com.cmcorg.service.engine.web.sign.signinname.service;

import com.cmcorg.service.engine.web.sign.signinname.mode.dto.*;

public interface SignSignInNameService {

    String signUp(SignSignInNameSignUpDTO dto);

    String signInPassword(SignSignInNameSignInPasswordDTO dto);

    String updatePassword(SignSignInNameUpdatePasswordDTO dto);

    String updateAccount(SignSignInNameUpdateAccountDTO dto);

    String signDelete(SignSignInNameSignDeleteDTO dto);
}
