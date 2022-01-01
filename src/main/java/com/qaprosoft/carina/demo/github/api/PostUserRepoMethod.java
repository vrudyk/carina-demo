package com.qaprosoft.carina.demo.github.api;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;

public class PostUserRepoMethod extends AbstractApiMethodV2 {

    public PostUserRepoMethod() {
        super("github/api/repo/_post/rq.json", "github/api/repo/_post/rs.json", "github/api/repo/repo.properties");
        replaceUrlPlaceholder("base_url", Configuration.getEnvArg("api_url"));
        request.header("Authorization", R.CONFIG.get("github_access_token"));
    }
}
