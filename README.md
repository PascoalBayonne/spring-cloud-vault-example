# Spring Cloud Vault

Spring cloud vault can manage static and dynamic secrets such as username/password for remote applications/resources and provide credentials for external services such as MySQL, PostgreSQL, Apache Cassandra, MongoDB, Consul, AWS, etc.



![spring cloud microservices with vault config](https://miro.medium.com/max/1106/1*kkHZ6pBWtmLiVGvCCsFrDw.png)

[**Spring Cloud Vault**](https://projects.spring.io/spring-vault/) provides Spring abstractions to the HashiCorp’s Vault which allows applications to access secrets stored in a Vault instance in a transparent way. 

### Store configuration in Vault

Vault is a secrets management system allowing you to store sensitive data which is encrypted at rest. It’s ideal to store sensitive configuration details such as passwords, encryption keys, API keys.

### Get started

First things first, we need to *Install and launch HashiCorp Vault*

### Installation:

> In this technical example we will be installing on a windows machine, please check the link and download the compatible HashiCorp Vault version for your OS .

 [https://www.vaultproject.io/downloads](https://www.vaultproject.io/downloads)

### Start Vault:
Unzip the rar file and execute the vault.exe

    unzip vault_1.4.0_windows_amd64.zip

    vault.exe

After sucessful installation and execution you will see in the vault usage commands: `Usage: vault <command> [args]` and list of commands.

Now you can start the server *(command line cowpoke)*

    vault server --dev --dev-root-token-id="00000000-0000-0000-0000-000000000000"


You should see the following as one of the last output lines:

    vault server --dev --dev-root-token-id="00000000-0000-0000-0000-000000000000"
    
    ==> Vault server configuration:
    
                 Api Address: http://127.0.0.1:8200
                         Cgo: disabled
             Cluster Address: https://127.0.0.1:8201
                  Listener 1: tcp (addr: "127.0.0.1:8200", cluster address: "127.0.0.1:8201", max_request_duration: "1m30s", max_request_size: "33554432", tls: "disabled")
                   Log Level: info
                       Mlock: supported: false, enabled: false
               Recovery Mode: false
                     Storage: inmem
                     Version: Vault v1.4.0
    
    WARNING! dev mode is enabled! In this mode, Vault runs entirely in-memory
    and starts unsealed with a single unseal key. The root token is already
    authenticated to the CLI, so you can immediately begin using Vault.
    
    You may need to set the following environment variable:
    
    PowerShell:
        $env:VAULT_ADDR="http://127.0.0.1:8200"
    cmd.exe:
        set VAULT_ADDR=http://127.0.0.1:8200
    
    The unseal key and root token are displayed below in case you want to
    seal/unseal the Vault or re-authenticate.
    
    Unseal Key: 5SOYvbuNhraOd0SeMQdaCTB1t1MTyNZ6Fz0ctipxY/s=
    Root Token: 00000000-0000-0000-0000-000000000000
    
    Development mode should NOT be used in production installations!

The command above starts Vault in development mode using in-memory storage without transport encryption. This is fine for evaluating Vault locally. Make sure to use proper SSL certificates and a reliable storage backend for production use. Consult Vault’s [Production Hardening guide](https://www.vaultproject.io/guides/production.html) for further details. Alternativelly, we will add in another git branch more advanced topics like database store.

### Store secrets in Vault
Launch another console window to store application configuration in Vault using the Vault command line but first you need to set one environment variables to point the Vault CLI to the Vault endpoint.

    set VAULT_ADDR=http://127.0.0.1:8200

Now you can store a configuration key-value pairs inside Vault

### Insert/Update Va
ult Key/Values and Versioning

***optional hint:*** in your console,  type `vault kv` and take a look to the subcommands and options `Usage: vault kv <subcommand> [options] [args]`

#### Insert:

    vault kv put secret/spring-vault-config-example example.username=pbayona example.password=qwerty

**note**:

    spring-vault-config-example : It is name of application in bootstrap.properties
    example: it is the configuration properties
    username, password: these are properties of configuration

#### Read:

         vault kv get secret/spring-vault-config-example 
    ====== Metadata ======                            
    Key              Value                            
    ---              -----                            
    created_time     2020-04-15T10:31:44.4431864Z     
    deletion_time    n/a                              
    destroyed        false                            
    version          1                                
                                                      
    ========== Data ==========                        
    Key                 Value                         
    ---                 -----                         
    example.password    qwerty                        
    example.username    pbayona                       

## Spring Boot Dependency:
Once we have our secrets configured in HashCorp Vault server, we can take a look how Spring-Boot apps can be easily integrated with Vault using ***Spring-Cloud-Vault*** 

Required dependencies:

```xml
 <!-- Vault Starter -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-vault-config</artifactId>
        </dependency>

```
For this technical example we've created a spring-boot app which can be found in the current git repository. 

##### Configure your application

    bootstrap.properties | yaml

    spring.application.name=spring-vault-config-example  
    spring.cloud.vault.token=00000000-0000-0000-0000-000000000000  
    #spring.cloud.vault.host=192.168.113.65  
    spring.cloud.vault.scheme=http  
    spring.cloud.vault.kv.enabled=true  
    # You can use different profiles in vault by adding /profile-name while creating your secret and then use it in here like spring.profiles.active=qld

Start the application using mvn command:

    mvn spring-boot::run
output stream:

         .   ____          _            __ _ _
     /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
    ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
     \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
      '  |____| .__|_| |_|_| |_\__, | / / / /
     =========|_|==============|___/=/_/_/_/
     :: Spring Boot ::        (v2.2.6.RELEASE)
    
    2020-04-15 17:33:22.072  INFO 7228 --- [           main] o.s.v.c.e.LeaseAwareVaultPropertySource  : Vault location [secret/application] not resolvable: Not found
    2020-04-15 17:33:22.074  INFO 7228 --- [           main] b.c.PropertySourceBootstrapConfiguration : Located property source: [BootstrapPropertySource {name='bootstrapProperties-secret/spring-vault-config-example'}, BootstrapPropertySource {name='bootstrapProperties-secret/application'}]
    2020-04-15 17:33:22.085  INFO 7228 --- [           main] SpringCloudVaultConfigExampleApplication : No active profile set, falling back to default profiles: default
    2020-04-15 17:33:22.832  INFO 7228 --- [           main] o.s.cloud.context.scope.GenericScope     : BeanFactory id=776a2033-cf95-3671-b56e-151864cbe8f4
    2020-04-15 17:33:23.289  INFO 7228 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
    2020-04-15 17:33:23.316  INFO 7228 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
    2020-04-15 17:33:23.317  INFO 7228 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.33]
    2020-04-15 17:33:23.887  INFO 7228 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
    2020-04-15 17:33:23.888  INFO 7228 --- [           main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 1780 ms
    2020-04-15 17:33:24.103  INFO 7228 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
    2020-04-15 17:33:25.036  INFO 7228 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
    2020-04-15 17:33:25.579  INFO 7228 --- [           main] SpringCloudVaultConfigExampleApplication : Started SpringCloudVaultConfigExampleApplication in 7.625 seconds (JVM running for 8.558)
    2020-04-15 17:33:25.580  INFO 7228 --- [           main] SpringCloudVaultConfigExampleApplication : ----------------------------------------
    2020-04-15 17:33:25.581  INFO 7228 --- [           main] SpringCloudVaultConfigExampleApplication : Configuration properties
    2020-04-15 17:33:25.581  INFO 7228 --- [           main] SpringCloudVaultConfigExampleApplication :    example.username is pbayona
    2020-04-15 17:33:25.583  INFO 7228 --- [           main] SpringCloudVaultConfigExampleApplication :    example.password is qwerty
    2020-04-15 17:33:25.584  INFO 7228 --- [           main] SpringCloudVaultConfigExampleApplication : ----------------------------------------


As result, you can see that the application took from Vault the configuration that we've set in above steps. Actually the application is logging the vault config which specified using commandLineRunner in our spring boot app.

        @Slf4j  
    @SpringBootApplication  
    @RequiredArgsConstructor  
    @EnableConfigurationProperties(VaultCustomConfig.class)  
    public class  
    SpringCloudVaultConfigExampleApplication implements CommandLineRunner {  
      
       private final VaultCustomConfig vaultCustomConfig;  
      
     public static void main(String[] args) {  
          SpringApplication.run(SpringCloudVaultConfigExampleApplication.class, args);  
      }  
      
      
       @Override  
      public void run(String... args) throws Exception {  
          log.info("----------------------------------------");  
      log.info("Configuration properties");  
      log.info("   example.username is {}", vaultCustomConfig.getUsername());  
      log.info("   example.password is {}", vaultCustomConfig.getPassword());  
      log.info("----------------------------------------");  
      }  
    }

Take a look to the entire application and inspect it.

## Advanced Configuration

More advanced topics will be covered in a different branch, topics like running spring-cloud-vault app with different profiles, using the Generic Secrets Backend, using the Database Secret Backend and a quick exploration of VaultTemplate.


