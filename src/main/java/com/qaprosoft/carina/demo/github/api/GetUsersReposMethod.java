package com.qaprosoft.carina.demo.github.api;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;
import com.qaprosoft.carina.core.foundation.utils.Configuration;

public class GetUsersReposMethod extends AbstractApiMethodV2 {

    public GetUsersReposMethod(String username) {
        super();
        replaceUrlPlaceholder("base_url", Configuration.getEnvArg("api_url"));
        replaceUrlPlaceholder("username", username);
    }
}
