# Spring Cloud Vault

Spring Cloud Vault Config provides client-side support for externalized configuration in a distributed system. With [HashiCorp’s Vault](https://www.vaultproject.io/) you have a central place to manage external secret properties for applications across all environments. Vault can manage static and dynamic secrets such as username/password for remote applications/resources and provide credentials for external services such as MySQL, PostgreSQL, Apache Cassandra, MongoDB, Consul, AWS and more.



![spring cloud microservices with vault config](https://miro.medium.com/max/1106/1*kkHZ6pBWtmLiVGvCCsFrDw.png)


[**Spring Cloud Vault**](https://projects.spring.io/spring-vault/) provides Spring abstractions to the HashiCorp’s Vault which allows applications to access secrets stored in a Vault instance in a transparent way. 
[https://cloud.spring.io/spring-cloud-vault/2.0.x/multi/multi_pr01.html](https://cloud.spring.io/spring-cloud-vault/2.0.x/multi/multi_pr01.html)

### Store configuration in Vault

Vault is a secrets management system allowing you to store sensitive data which is encrypted at rest. It’s ideal to store sensitive configuration details such as passwords, encryption keys, API keys.
