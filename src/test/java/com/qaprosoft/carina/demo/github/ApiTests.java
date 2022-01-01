package com.qaprosoft.carina.demo.github;

import com.qaprosoft.apitools.validation.JsonCompareKeywords;
import com.qaprosoft.carina.core.foundation.IAbstractTest;
import com.qaprosoft.carina.core.foundation.api.http.HttpResponseStatusType;
import com.qaprosoft.carina.demo.github.api.*;
import io.restassured.path.json.JsonPath;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ApiTests implements IAbstractTest {

    @Test
    public void testGetUserInfoByName() {
        String username = "vrudyk";
        GetUserByNameMethod getUserByNameMethod = new GetUserByNameMethod(username);
        getUserByNameMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        getUserByNameMethod.callAPI();
        getUserByNameMethod.validateResponse(JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
        getUserByNameMethod.validateResponseAgainstSchema("github/api/user/_get/rs.schema");
    }

    @Test
    public void testGetUsersRepo() {
        String username = "vrudyk";
        String repo = "carina-demo";
        GetUsersRepoMethod getUsersRepoMethod = new GetUsersRepoMethod(username, repo);
        getUsersRepoMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        getUsersRepoMethod.callAPI();
        getUsersRepoMethod.validateResponseAgainstSchema("github/api/repo/_get/rs.schema");
    }

    @Test
    public void testGetUsersRepos() {
        String username = "vrudyk";
        GetUsersReposMethod getUsersRepoMethod = new GetUsersReposMethod(username);
        getUsersRepoMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        getUsersRepoMethod.callAPI();
    }

    @Test
    public void testCreateRepo() {
        String username = "vrudyk";
        PostUserRepoMethod postRepositoryMethod = new PostUserRepoMethod();
        postRepositoryMethod.expectResponseStatus(HttpResponseStatusType.CREATED_201);
        String rsBody = postRepositoryMethod.callAPI().asString();
        postRepositoryMethod.validateResponse(JSONCompareMode.LENIENT);
        postRepositoryMethod.validateResponseAgainstSchema("github/api/repo/_post/rs.schema");
        JsonPath jsonPath = new JsonPath(rsBody);
        String repo = jsonPath.getString("name");
        GetUsersRepoMethod getRepositoryMethod = new GetUsersRepoMethod(username, repo);
        getRepositoryMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        getRepositoryMethod.callAPI();
    }

    @Test
    public void testDeleteRepo() {
        String username = "vrudyk";
        PostUserRepoMethod postRepositoryMethod = new PostUserRepoMethod();
        postRepositoryMethod.expectResponseStatus(HttpResponseStatusType.CREATED_201);
        String rsBody = postRepositoryMethod.callAPI().asString();
        postRepositoryMethod.validateResponse(JSONCompareMode.LENIENT);
        postRepositoryMethod.validateResponseAgainstSchema("github/api/repo/_post/rs.schema");
        JsonPath jsonPath = new JsonPath(rsBody);
        String repo = jsonPath.getString("name");
        DeleteUserRepoMethod deleteUserRepoMethod = new DeleteUserRepoMethod(username, repo);
        deleteUserRepoMethod.expectResponseStatus(HttpResponseStatusType.NO_CONTENT_204);
        String rsDeleteBody = deleteUserRepoMethod.callAPI().asString();
        Assert.assertEquals(rsDeleteBody, "");
    }

    @Test
    public void testUpdateUserBio() {
        String rqJson = "github/api/user/_patch/rq-bio.json";
        String username = "vrudyk";

        GetUserByNameMethod getUserByNameMethod = new GetUserByNameMethod(username);
        getUserByNameMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        String rsUserInfoBefore = getUserByNameMethod.callAPI().asString();

        PatchUserDataMethod patchUserDataMethod = new PatchUserDataMethod(rqJson);
        patchUserDataMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        String rsPatchBody = patchUserDataMethod.callAPI().asString();
        patchUserDataMethod.validateResponseAgainstSchema("github/api/user/_patch/rs.schema");

        JsonPath jsonGet = new JsonPath(rsUserInfoBefore);
        String oldBio = jsonGet.getString("bio");
        JsonPath jsonPatch = new JsonPath(rsPatchBody);
        String actualBio = jsonPatch.getString("bio");
        Properties userProperties = new Properties();
        try {
            FileReader userPropertiesFile = new FileReader("src/test/resources/github/api/user/_patch/user-data.properties");
            userProperties.load(userPropertiesFile);
        } catch (FileNotFoundException e) {
            Assert.fail("File with data isn't read! " + e);
        } catch (IOException e) {
            Assert.fail("File with data isn't loaded! " + e);
        }
        String expectedBio = userProperties.getProperty("bio");
        Assert.assertEquals(actualBio, expectedBio, "Bio isn't updated right!");
        Assert.assertNotEquals(oldBio, actualBio, "Bio isn't updated!");
    }

    @Test
    public void testUpdateUserLocationCompany() {
        String rqJson = "github/api/user/_patch/rq-location-company.json";
        String username = "vrudyk";

        GetUserByNameMethod getUserByNameMethod = new GetUserByNameMethod(username);
        getUserByNameMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        String rsUserInfoBefore = getUserByNameMethod.callAPI().asString();

        PatchUserDataMethod patchUserDataMethod = new PatchUserDataMethod(rqJson);
        patchUserDataMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        String rsBody = patchUserDataMethod.callAPI().asString();
        patchUserDataMethod.validateResponseAgainstSchema("github/api/user/_patch/rs.schema");

        JsonPath jsonGet = new JsonPath(rsUserInfoBefore);
        String oldCompany = jsonGet.getString("company");
        String oldLocation = jsonGet.getString("location");
        JsonPath jsonPatch = new JsonPath(rsBody);
        String actualCompany = jsonPatch.getString("company");
        String actualLocation = jsonPatch.getString("location");
        Properties userProperties = new Properties();
        try {
            FileReader userPropertiesFile = new FileReader("src/test/resources/github/api/user/_patch/user-data.properties");
            userProperties.load(userPropertiesFile);
        } catch (FileNotFoundException e) {
            Assert.fail("File with data isn't read! " + e);
        } catch (IOException e) {
            Assert.fail("File with data isn't loaded! " + e);
        }
        String expectedCompany = userProperties.getProperty("company");
        String expectedLocation = userProperties.getProperty("location");
        Assert.assertEquals(actualCompany, expectedCompany, "Company isn't updated right!");
        Assert.assertEquals(actualLocation, expectedLocation, "Location isn't updated right!");
        Assert.assertNotEquals(actualCompany, oldCompany, "Company isn't updated!");
        Assert.assertNotEquals(actualLocation, oldLocation, "Location isn't updated!");
    }


}
