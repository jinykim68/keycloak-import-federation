package com.changefirst.api.user;


import com.changefirst.model.UserCredentialsDto;
import com.changefirst.model.UserDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Stub Service class to be used with RestEasy to access user rest api
 * Created by istvano on 16/02/2017.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UserService {

    @GET
    @Path("/clients/{client}/users/{username}")
    UserDto getUserDetails(@PathParam("client") String client, @PathParam("username") String username);

    @HEAD
    @Path("/clients/{client}/users/{username}")
    Response validateUserExists(@PathParam("client") String client, @PathParam("username") String username);

    @POST
    @Path("/clients/{client}/users/{username}")
    Response validateLogin(@PathParam("client") String client, @PathParam("username") String username, UserCredentialsDto credentialsDto);
}
