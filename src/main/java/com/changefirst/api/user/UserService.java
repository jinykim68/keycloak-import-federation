package com.changefirst.api.user;


import com.changefirst.model.UserCredentialsDto;
import com.changefirst.model.UserDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by istvano on 16/02/2017.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UserService {

    @GET
    @Path("/users/{username}")
    UserDto getUserDetails(@PathParam("username") String username);

    @HEAD
    @Path("/users/{username}")
    Response validateUserExists(@PathParam("username") String username);

    @POST
    @Path("/users/{username}")
    Response validateLogin(@PathParam("username") String username, UserCredentialsDto credentialsDto);
}
