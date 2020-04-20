## Advanced Configuration

### Database backends

Vault supports several database secret backends to generate database credentials dynamically based on configured roles. This means services that need to access a database no longer need to configure credentials: they can request them from Vault, and use Vault’s leasing mechanism to more easily roll keys.

Using a database secret backend requires to enable the backend in the configuration and the `spring-cloud-vault-config-databases` dependency.

    <dependency>  
     <groupId>org.springframework.cloud</groupId>  
     <artifactId>spring-cloud-vault-config-databases</artifactId>  
    </dependency>
  
  Spring Cloud Vault can obtain credentials for any database listed at [https://www.vaultproject.io/api/secret/databases/index.html](https://www.vaultproject.io/api/secret/databases/index.html). The integration can be enabled by setting `spring.cloud.vault.database.enabled=true` (default `false`) and providing the role name with `spring.cloud.vault.database.role=…`. Our bootstrap.properties looks like this:


    spring.cloud.vault.database.enabled=true  
    spring.cloud.vault.database.role=ecommerceDB-role-no-delete

In that order, you don't need to specify the `spring.datasource.username` and `spring.datasource.password` because Spring Boot will pick up the generated credentials fir your DataSource. Let's move forward.

 Spring Cloud Vault can obtain credentials for any database listed at https://www.vaultproject.io/api/secret/databases/index.html.  
[Read more about Vault Database Secrets with Spring Cloud Vault](https://cloud.spring.io/spring-cloud-vault/2.0.x/multi/multi_vault.config.backends.database-backends.html#vault.config.backends.database)

## Configuring Vault with MySQL

Create a new user in MySQL so that user  will be used by Vault (root user avoided here)
> -- create user 'vaultdb-admin'@'%' identified by 'reddeadredemption#2'; 

> -- grant all privileges on vaultdb.* to 'vaultdb-admin'@'%' with grant option; 
> grant create user on *.* to 'vaultdb-admin' with grant option;
> 
> flush privileges;

Start the Vault & MySQL using the docker compose file located in the project 

> ../docker/docker-compose.yaml



The database secret engine is not enabled by default, so we must fix this before we can proceed:


    λ vault secrets enable database
    Success! Enabled the database secrets engine at: database/

Now its time to create the database configuration resource:

    λ vault write database/config/ecommerceDB plugin_name=mysql-legacy-database-plugin connection_url="{{username}}:{{password}}@tcp(127.0.0.1:3306)/ecommerceDB" allowed_roles="*" username="ecommerceDB-admin" password="R3dDe@dRed&mption#319i1s"
  
  Explaining the abive command:
  **database/config** is where all database configurations must be stored.
  
  **plugin_name**: Defines which database plugin will be used. The available plugin names are described in [Vault's docs](https://www.vaultproject.io/docs/secrets/databases/index.html)
  
  **connection_url:** This is a template used by the plugin when connecting to the database.
  
  **username & password:** This is the account that Vault will use to perform database operations, such as creating a new user and revoking its privileges
  
  Now setup the Vault Database Role
  
To generate database credentials, you need to set up a role first. Roles control the permission context for database credentials generation. A role defines the permissions that are associated with the credentials. A role also defines the max lease time for obtained credentials. The lease is in Vault-speak the duration the credentials are valid. Vault revokes credentials from the database system once they are expired.

  

     λ vault write database/roles/ecommerceDB-role-no-delete db_name=ecommerceDB creation_statements="CREATE USER '{{name}}'@'%' IDENTIFIED BY '{{password}}';GRANT INSERT, UPDATE, SELECT ON ecommerceDB.* TO '{{name}}'@'%';"
    Success! Data written to: database/roles/ecommerceDB-role-no-delete


**ecommerceDB-role-no-delete** is the role that our application is going to use in bootstrap.propeties on : `spring.cloud.vault.database.role=ecommerceDB-role-no-delete`

For user `ecommerceDB-admin` mentioned earlier, we are giving some roles, take a look that that user can **only** perform a `select`, `Insert` and `Update` operation.

Let's read the role above created:

    λ vault read database/creds/ecommerceDB-role-no-delete
    Key                Value
    ---                -----
    lease_id           database/creds/ecommerceDB-role-no-delete/CPKTXqx0vDurDvGzKphdzWv8
    lease_duration     768h
    lease_renewable    true
    password           A1a-svjcksRafH3W9HTM
    username           v-ecom-gbYuOlb9a

Voila! vault has created the `username` and `password` so our spring boot application can now use it. Resuming our application is using secrets given/generated/saved in vault server. This allows the application secrets to be hiden from final users.

The bootstrap.properties seems so clear now. It doesn't expose any our credential.

    spring.cloud.vault.token=00000000-0000-0000-0000-000000000000  
    spring.cloud.vault.application-name=spring-vault-config-example  
    spring.cloud.vault.scheme=http  
    spring.cloud.vault.kv.enabled=true  
    spring.cloud.vault.database.enabled=true  
    spring.cloud.vault.database.role=ecommerceDB-role-no-delete

I've set up even the `spring.datasource.url=jdbc:mysql://127.0.0.1:3306/ecommerceDB` in Vault server under the name of our application by using the property `spring.cloud.vault.application-name=spring-vault-config-example`  or even the simpliest one `spring.application.name=spring-vault-config-example`

[Want to be Pro? Read de documentation](https://cloud.spring.io/spring-cloud-vault/2.0.x/multi/multi_vault.config.backends.database-backends.html)

## Test your might (Run the application)

    mvn spring-boot::run


        2020-04-20 16:58:52.574  INFO 19344 --- [           main] o.s.s.c.ThreadPoolTaskScheduler          : Initializing ExecutorService
    
      .   ____          _            __ _ _
     /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
    ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
     \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
      '  |____| .__|_| |_|_| |_\__, | / / / /
     =========|_|==============|___/=/_/_/_/
     :: Spring Boot ::        (v2.2.6.RELEASE)
    
    2020-04-20 16:58:53.246  INFO 19344 --- [           main] o.s.v.c.e.LeaseAwareVaultPropertySource  : Vault location [secret/spring-vault-config-example] not resolvable: Not found
    2020-04-20 16:58:53.250  INFO 19344 --- [           main] o.s.v.c.e.LeaseAwareVaultPropertySource  : Vault location [secret/application] not resolvable: Not found
    2020-04-20 16:58:53.266  INFO 19344 --- [           main] b.c.PropertySourceBootstrapConfiguration : Located property source: [BootstrapPropertySource {name='bootstrapProperties-secret/spring-vault-config-example'}, BootstrapPropertySource {name='bootstrapProperties-secret/application'}, BootstrapPropertySource {name='bootstrapProperties-database with Role ecommerceDB-role-no-delete'}]
    2020-04-20 16:58:53.271  INFO 19344 --- [           main] SpringCloudVaultConfigExampleApplication : No active profile set, falling back to default profiles: default
    2020-04-20 16:58:53.790  INFO 19344 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
    2020-04-20 16:58:53.852  INFO 19344 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 51ms. Found 1 JPA repository interfaces.
    2020-04-20 16:58:54.018  INFO 19344 --- [           main] o.s.cloud.context.scope.GenericScope     : BeanFactory id=2a11aae9-bcca-3195-8511-cefdb6524e82
    2020-04-20 16:58:54.387  INFO 19344 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8081 (http)
    2020-04-20 16:58:54.387  INFO 19344 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
    2020-04-20 16:58:54.402  INFO 19344 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.33]
    2020-04-20 16:58:54.588  INFO 19344 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
    2020-04-20 16:58:54.589  INFO 19344 --- [           main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 1303 ms
    2020-04-20 16:58:54.721  INFO 19344 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
    2020-04-20 16:58:54.863  INFO 19344 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
    2020-04-20 16:58:54.911  INFO 19344 --- [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
    
    2020-04-20 16:58:55.024  INFO 19344 --- [           main] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 5.4.12.Final
    2020-04-20 16:58:55.198  INFO 19344 --- [           main] o.hibernate.annotations.common.Version   : HCANN000001: Hibernate Commons Annotations {5.1.0.Final} 2020-04-20 16:58:55.327  INFO 19344 --- [           main] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.MySQL5Dialect
    2020-04-20 16:58:55.984  INFO 19344 --- [           main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000490: Using JtaPlatform implementation: [org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform]
    2020-04-20 16:58:55.992  INFO 19344 --- [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
    2020-04-20 16:58:56.284  WARN 19344 --- [           main] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
    2020-04-20 16:58:56.438  INFO 19344 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
    2020-04-20 16:58:57.142  INFO 19344 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8081 (http) with context path ''
    2020-04-20 16:58:57.521  INFO 19344 --- [           main] SpringCloudVaultConfigExampleApplication : Started SpringCloudVaultConfigExampleApplication in 6.955 seconds (JVM running for 7.539)



If you take a look into the log you will see in the 3rd line the output stream:

    2020-04-20 16:58:53.266 INFO 19344 --- [ main] b.c.PropertySourceBootstrapConfiguration : Located property source: [BootstrapPropertySource {name='bootstrapProperties-secret/spring-vault-config-example'}, BootstrapPropertySource {name='bootstrapProperties-secret/application'}, BootstrapPropertySource {name='bootstrapProperties-database with Role ecommerceDB-role-no-delete'}]

You can create, update and get product in our application. Eveything will work as expected but when you try to delete an exception will be thrown:

    λ curl -X DELETE http://localhost:8081/product/a3a38232-37b7-4072-929a-f87114df57e8

    {"timestamp":"2020-04-20T16:16:48.223+0000","status":500,"error":"Internal Server Error","message":"could not execute statement; SQL [n/a]; nested exception is org.hibernate.exception.SQLGrammarException: could not execute statement","path":"/product/a3a38232-37b7-4072-929a-f87114df57e8"}

If you check the log of our application you will see that Vault has provided username and password as well as role for our application user which cannot perform the delete operation.

    2020-04-20 17:16:48.191 ERROR 15640 --- [nio-8081-exec-3] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is org.springframework.dao.InvalidDataAccessResourceUsageException: could not execute statement; SQL [n/a]; nested exception is org.hibernate.exception.SQLGrammarException: could not execute statement] with root cause
    
    java.sql.SQLSyntaxErrorException: DELETE command denied to user 'v-ecom-hwoXJvwgk'@'localhost' for table 'product'
