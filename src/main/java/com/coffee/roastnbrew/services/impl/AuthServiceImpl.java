package com.coffee.roastnbrew.services.impl;

import com.coffee.roastnbrew.constants.Constants;
import com.coffee.roastnbrew.daos.UsersDAO;
import com.coffee.roastnbrew.exceptions.CoffeeException;
import com.coffee.roastnbrew.exceptions.UnauthorizedRequest;
import com.coffee.roastnbrew.models.GoogleAuthStateDTO;
import com.coffee.roastnbrew.models.LoginResponse;
import com.coffee.roastnbrew.models.LoginResponse.LoginResponseBuilder;
import com.coffee.roastnbrew.models.User;
import com.coffee.roastnbrew.services.AuthService;
import com.coffee.roastnbrew.utils.AuthUtils;
import com.coffee.roastnbrew.utils.URLUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.PlusScopes;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jvnet.hk2.annotations.Service;

@Service
@Singleton
public class AuthServiceImpl implements AuthService {
    
    private final UsersDAO usersDAO;
    private final GoogleAuthorizationCodeFlow flow;
    
    @Inject
    public AuthServiceImpl(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;

        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        this.flow = new GoogleAuthorizationCodeFlow.Builder(
            httpTransport, jsonFactory, "608274432588-5okin3qfauifet8121k565gqvgv4n760.apps.googleusercontent.com", "4ESzT6BDmxqIOKpCfP_Va7rf",
            Arrays.asList(PlusScopes.USERINFO_PROFILE, PlusScopes.USERINFO_EMAIL))
            .setAccessType("offline")
            .setApprovalPrompt("force")
            .build();
    }
    
    public String getAuthorizationUrl(String next, String code, String errorPath) {
        GoogleAuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl()
            .setRedirectUri(getRedirectURL())
            .setApprovalPrompt("auto")
            .set("hd", "hevodata.com");
        
        next = URLUtils.cleanNextUrl(next);
        errorPath = URLUtils.cleanNextUrl(errorPath);
    
        GoogleAuthStateDTO stateDTO = GoogleAuthStateDTO.builder()
            .next(next)
            .loginCode(code)
            .errorPath(errorPath)
            .build();
        
        authorizationUrl.setState(encodeOAuthState(new GsonJsonProvider().toJson(stateDTO)));
        
        return authorizationUrl.build();
    }
    
    private String getRedirectURL() {
        return "http://localhost:8080/google-login";
    }
    
    public static String encodeOAuthState(String state) {
        return new String(Base64.getEncoder().encode(state.getBytes()));
    }
    
    public LoginResponse createOrUpdateUser(String code) throws CoffeeException {
        final GoogleTokenResponse response;
        
        try {
            response = flow.newTokenRequest(code)
                .setRedirectUri(getRedirectURL())
                .execute();
        } catch (IOException e) {
            throw new CoffeeException("IO exception", e);
        }
        
        Map<String, Object> profile = this.getGoogleProfile(response);
        String email = (String) profile.get("email");
    
        if (!AuthUtils.isHevoEmail(email)) {
            throw new UnauthorizedRequest(String.format("only %s emails are allowed.", Constants.HEVO_EMAIL_AT_SUFFIX));
        }
        
        User user = usersDAO.getByEmail(email);
    
        LoginResponseBuilder responseBuilder = LoginResponse.builder();
        if (user == null) {
            user = new User();
            user.setEmailId(email);
            user.setImageUrl((String) profile.get("picture"));
            user.setFirstName((String) profile.get("given_name"));
            user.setLastName((String) profile.get("family_name"));
    
            usersDAO.addUser(user);
            responseBuilder.shouldAskDetails(true);
        } else {
            responseBuilder.shouldAskDetails(false);
        }
        responseBuilder.user(user);

        return responseBuilder.build();
    }
    
    private Map<String, Object> getGoogleProfile(GoogleTokenResponse googleTokenResponse) throws CoffeeException {
        try {
            String idToken = googleTokenResponse.getIdToken();

            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setSkipSignatureVerification()
                .setSkipDefaultAudienceValidation()
                .build();

            JwtClaims jwtClaims = jwtConsumer.processToClaims(idToken);
            return jwtClaims.getClaimsMap();
        } catch (InvalidJwtException e) {
            throw new CoffeeException("Failed to login with Google.", e);
        }
    }
}
