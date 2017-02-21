/*
 * Copyright 2015 Smartling, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.changefirst.keycloak.provider;

import com.changefirst.api.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.keycloak.Config.Scope;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Remote user federation provider factory tests.
 */
public class RestUserFederationProviderTest {

    RestUserFederationProviderFactory factory;

    @Mock
    private KeycloakSessionFactory keycloakSessionFactory;

    @Mock
    private KeycloakSession keycloakSession;

    @Mock
    private Scope config;

    @Mock
    private ComponentModel userFederationProviderModel;

    @Mock
    private RealmModel realm;

    @Mock
    private UserModel user;

    @Mock
    private UserCredentialModel input;

    @Mock
    private UserRepository repository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);


        factory = new RestUserFederationProviderFactory();
        MultivaluedHashMap<String, String> config = new MultivaluedHashMap<String, String>();
        config.putSingle(RestUserFederationProviderFactory.PROPERTY_URL, "http://localhost.com");
        when(userFederationProviderModel.getConfig())
                .thenReturn(config);

        when(user.getUsername()).thenReturn("user1@changefirst.com");
        when(input.getValue()).thenReturn("password");
        when(input.getType()).thenReturn(CredentialModel.PASSWORD);

    }

    @Test
    public void testGetInstance() throws Exception {
        Object provider = factory.create(keycloakSession, userFederationProviderModel);
        assertNotNull(provider);
        assertTrue(provider instanceof RestUserFederationProvider);
    }

    @Test
    public void testclose() throws Exception {
        RestUserFederationProvider  provider = factory.create(keycloakSession, userFederationProviderModel);
        provider.close();
        verifyZeroInteractions(config);
    }


}