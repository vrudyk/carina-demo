package com.qaprosoft.carina.demo.github.api;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;

public class PatchUserDataMethod extends AbstractApiMethodV2 {

    public PatchUserDataMethod(String rqPath) {
        super(rqPath, null, "github/api/user/_patch/user-data.properties");
        replaceUrlPlaceholder("base_url", Configuration.getEnvArg("api_url"));
        request.header("Authorization", R.CONFIG.get("github_access_token"));
    }
}
