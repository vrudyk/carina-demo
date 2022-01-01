package com.qaprosoft.carina.demo.github.api;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;

public class DeleteUserRepoMethod extends AbstractApiMethodV2 {

    public DeleteUserRepoMethod(String username, String repo) {
        super(null, "github/api/repo/_delete/rs.json");
        replaceUrlPlaceholder("base_url", Configuration.getEnvArg("api_url"));
        replaceUrlPlaceholder("username", username);
        replaceUrlPlaceholder("repo", repo);
        request.header("Authorization", R.CONFIG.get("github_access_token"));
    }
}
