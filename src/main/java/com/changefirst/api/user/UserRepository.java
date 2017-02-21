package com.changefirst.api.user;

import com.changefirst.model.UserCredentialsDto;
import com.changefirst.model.UserDto;
import org.apache.http.HttpStatus;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Remote repository to load remote user data
 * Created by istvano on 16/02/2017.
 */
public class UserRepository {

    private static UserService buildClient(String uri) {

        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target =  client.target(uri);

        return target
                .proxyBuilder(UserService.class)
                .classloader(UserService.class.getClassLoader())
                .build();

    }

    private String url;
    private List<String> attributes;
    private UserService remoteService;

    public UserRepository(String url) {
        this(url, new ArrayList<String>());
    }

    public UserRepository(String url, List<String> attributes) {
        this.url = url;
        this.attributes = attributes;
        this.remoteService = buildClient(url);
    }

    /**
     * talks a remote API to check if the user credentials are correct
     * @param userName
     * @param credential
     * @return
     */
    public boolean validateCredentials(String userName, String credential) {
        Response response = remoteService.validateLogin(userName, new UserCredentialsDto(credential));
        return HttpStatus.SC_OK == response.getStatus();
    }

    /**
     * talks to a remote API to retrieve user info
     * @param userName
     * @return
     */
    public UserDto findUserByUsername(String userName) {
        UserDto remoteUser = remoteService.getUserDetails(userName);
        return remoteUser;
    }
}
