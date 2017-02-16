package com.changefirst.api.user;


import com.changefirst.model.UserCredentialsDto;
import com.changefirst.model.UserDto;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by istvano on 16/02/2017.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UserService {

    @GET
    @Path("/api/users/{username}/")
    UserDto getUserDetails(@PathParam("username") String username);

    @HEAD
    @Path("/api/users/{username}/")
    Response validateUserExists(@PathParam("username") String username);

    @POST
    @Path("/api/users/{username}/")
    Response validateLogin(@PathParam("username") String username, UserCredentialsDto credentialsDto);

}
