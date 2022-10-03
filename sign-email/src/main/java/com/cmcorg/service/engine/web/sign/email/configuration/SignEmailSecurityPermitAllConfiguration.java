package com.cmcorg.service.engine.web.sign.email.configuration;

import com.cmcorg.service.engine.web.sign.helper.configuration.AbstractSignHelperSecurityPermitAllConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SignEmailSecurityPermitAllConfiguration extends AbstractSignHelperSecurityPermitAllConfiguration {

    public final static int SIGN_LEVEL = 100;

    @Override
    protected String getSignPreUri() {
        return "email";
    }

    @Override
    public int getSignLevel() {
        return SIGN_LEVEL;
    }

}
