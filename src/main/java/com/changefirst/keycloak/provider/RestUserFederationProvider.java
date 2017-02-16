/*
 * Copyright 2015 Changefirst Ltd.
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
import com.changefirst.model.UserDto;
import org.jboss.logging.Logger;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.*;
import org.keycloak.models.utils.UserModelDelegate;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.user.ImportedUserValidation;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;


/**
 * Dummy User federation for local development
 *
 * @author Istvan Orban
 */
public class RestUserFederationProvider implements UserLookupProvider, ImportedUserValidation, CredentialInputValidator, UserStorageProvider {

    private static final Logger LOG = Logger.getLogger(RestUserFederationProvider.class);

    protected KeycloakSession session;
    protected ComponentModel model;
    protected UserRepository repository;

    public RestUserFederationProvider(KeycloakSession session, ComponentModel model, UserRepository repository) {
        this.session = session;
        this.model = model;
        this.repository = repository;
    }

    protected UserModel createAdapter(RealmModel realm, String username) {
        UserModel local = session.userLocalStorage().getUserByUsername(username, realm);
        if (local == null) {
            //fetch user remotely
            UserDto remote = this.repository.findUserByUsername(username);
            if ( remote != null) {
                local = session.userLocalStorage().addUser(realm, username);
                local.setFederationLink(model.getId());

                //merge data from remote to local

            }
        }
        if ( local != null ) {
            return new UserModelDelegate(local) {
                @Override
                public void setUsername(String username) {
                    //showing we can proxy every request to our repo if needed
                    super.setUsername(username);
                }
            };
        } else {
            return null;
        }
    }


    @Override
    public boolean supportsCredentialType(String credentialType) {
        return CredentialModel.PASSWORD.equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        return supportsCredentialType(credentialType);
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) {
            return false;
        }
        UserCredentialModel cred = (UserCredentialModel) input;
        return repository.validateCredentials(user.getUsername(), cred.getValue());
    }

    @Override
    public void close() {
        //n/a
    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        StorageId storageId = new StorageId(id);
        String username = storageId.getExternalId();
        return this.getUserByUsername(username, realm);
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        return getUserByUsername(email, realm);
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {

        LOG.infof("Get by username: %s", username);
        return this.createAdapter(realm, username);
    }

    @Override
    public void preRemove(RealmModel realm) {
        //n/a
    }

    @Override
    public void preRemove(RealmModel realm, GroupModel group) {
        //n/a
    }

    @Override
    public void preRemove(RealmModel realm, RoleModel role) {
        //n/a
    }

    //see https://keycloak.gitbooks.io/server-developer-guide/content/topics/user-storage/import.html
    @Override
    public UserModel validate(RealmModel realm, UserModel user) {
        return user;
    }
}
