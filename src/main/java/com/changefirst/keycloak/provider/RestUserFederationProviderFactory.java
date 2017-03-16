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
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;

import java.util.List;

/**
 * Remote user federation provider factory.
 *
 * @author Scott Rossillo
 */
public class RestUserFederationProviderFactory implements UserStorageProviderFactory<RestUserFederationProvider> {


    public static final String PROVIDER_NAME = "Rest User Migration Federation SPI";

    public static final String PROPERTY_URL = "url";
    public static final String PROPERTY_ATTRIBS = "attributes";
    public static final String PROPERTY_CLIENTID = "clientId";
    public static final String AUTO_ENABLE_ACCOUNT = "auto-enable";

    private static final Logger LOG = Logger.getLogger(RestUserFederationProviderFactory.class);

    protected static final List<ProviderConfigProperty> configMetadata;

    static {
        configMetadata = ProviderConfigurationBuilder.create()
                .property().name(PROPERTY_URL)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Remote User Information Url")
                .defaultValue("https://")
                .helpText("Remote repository url")
                .add()
                .property().name(PROPERTY_CLIENTID)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Remote Client identifier")
                .defaultValue("")
                .helpText("This value will be sent to the remote service along with the username")
                .add()
                .property().name(PROPERTY_ATTRIBS)
                .type(ProviderConfigProperty.MULTIVALUED_STRING_TYPE)
                .label("Names of attributes to process in addition to the default properties")
                .defaultValue("")
                .helpText("Using this property, one can pass in attributes onto the user")
                .add()
                .property().name(AUTO_ENABLE_ACCOUNT)
                .type(ProviderConfigProperty.BOOLEAN_TYPE)
                .label("Auto enable migrated user accounts")
                .defaultValue(true)
                .helpText("Using this property, one can mark all migrated accounts enabled")
                .add()
                .build();
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configMetadata;
    }

    @Override
    public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) throws ComponentValidationException {
       String url = config.getConfig().getFirst(PROPERTY_URL);
       String clientId = config.getConfig().getFirst(PROPERTY_CLIENTID);

       boolean valid = false;

       if ( url != null && url.length() > 10) {
           valid =  true;
       }


       if ( valid && clientId != null && clientId.length() > 0 ) {
           valid = true;
       } else {
           valid = false;
       }

       LOG.debugf("validating module config %s", valid);

       if (!valid) {
           throw new ComponentValidationException("Invalid configuration. Please check the url");
       }
    }

    @Override
    public String getId() {
        return PROVIDER_NAME;
    }

    @Override
    public String getHelpText() {
        return "This is a read-only user federation provider that can be used to sync user data one way from a remote REST API";
    }

    @Override
    public RestUserFederationProvider create(KeycloakSession session, ComponentModel model) {
        String url = model.getConfig().getFirst(PROPERTY_URL);
        String clientId = model.getConfig().getFirst(PROPERTY_CLIENTID);
        Boolean autoEnable = Boolean.valueOf(model.getConfig().getFirst(AUTO_ENABLE_ACCOUNT));
        List<String> attribList = model.getConfig().getList(PROPERTY_ATTRIBS);
        UserRepository repository = new UserRepository(url, clientId);
        return new RestUserFederationProvider(session, model, repository, attribList, autoEnable);
    }

}
