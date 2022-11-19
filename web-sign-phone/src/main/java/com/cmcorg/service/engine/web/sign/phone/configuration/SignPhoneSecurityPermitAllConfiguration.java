package com.cmcorg.service.engine.web.sign.phone.configuration;

import com.cmcorg.service.engine.web.sign.helper.configuration.AbstractSignHelperSecurityPermitAllConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SignPhoneSecurityPermitAllConfiguration extends AbstractSignHelperSecurityPermitAllConfiguration {

    public final static int SIGN_LEVEL = 200;

    @Override
    protected String getSignPreUri() {
        return "phone";
    }

    @Override
    public int getSignLevel() {
        return SIGN_LEVEL;
    }

}
