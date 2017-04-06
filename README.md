# Keycloak Rest User Provider
  
Provides a [Keycloak][0] REST based user federation provider to sync users from third party on the fly 

## Key features of the migration provider

* Allows syncing remote user information to keycloak using a rest api
* Allows keeping the old passwords of the newly linked users
* Import flow has two stages:

1, import user data
2, validate password

* Allows to import third party identifier

> **NOTE:** This provider is an example that shows certain features that can be useful. E.g. importing roles etc.

## How it Works

**RestUserFederationProviderFactory** defines the parameters the provider can accept

**RestUserFederationProvider** implements user import calling **UserRepository** 
 resteasy is used to implement a UserService class.

The **UserDto** class defines the interface for the Rest Service class

> **Example:** Using 

## Installing the Federation Provider

* build the project
<code>
./gradlew clean build
</code>
* check if the jar was built
<code>
ls ./build/libs/
</code>
* deploy the jar file to a keycloak instance for example:
<code>
cp keycloak-import-federation-*.jar /opt/jboss/keycloak/providers/
</code>

[0]: http://keycloak.jboss.org/

