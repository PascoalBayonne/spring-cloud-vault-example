# Spring Cloud Vault

Spring Cloud Vault Config provides client-side support for externalized configuration in a distributed system. With [HashiCorp’s Vault](https://www.vaultproject.io/) you have a central place to manage external secret properties for applications across all environments. Vault can manage static and dynamic secrets such as username/password for remote applications/resources and provide credentials for external services such as MySQL, PostgreSQL, Apache Cassandra, MongoDB, Consul, AWS and more.



![spring cloud microservices with vault config](https://miro.medium.com/max/1106/1*kkHZ6pBWtmLiVGvCCsFrDw.png)


[**Spring Cloud Vault**](https://projects.spring.io/spring-vault/) provides Spring abstractions to the HashiCorp’s Vault which allows applications to access secrets stored in a Vault instance in a transparent way. 
[https://cloud.spring.io/spring-cloud-vault/2.0.x/multi/multi_pr01.html](https://cloud.spring.io/spring-cloud-vault/2.0.x/multi/multi_pr01.html)

### Store configuration in Vault

Vault is a secrets management system allowing you to store sensitive data which is encrypted at rest. It’s ideal to store sensitive configuration details such as passwords, encryption keys, API keys.






## WARNING: NOT REQUIRED TO READ BUT NICE TO EXPLORE:
###  Secrets as a Service: Dynamic 
Read the officail docmuemtation
[https://learn.hashicorp.com/vault/secrets-management/sm-dynamic-secrets](https://learn.hashicorp.com/vault/secrets-management/sm-dynamic-secrets)

### TL;DR
Vault can generate secrets on-demand for some systems. For example, when an app needs to access an Amazon S3 bucket, it asks Vault for AWS credentials. Vault will automatically revoke this credential after the TTL is expired.

Applications ask Vault for database credentials rather than setting them as environment variables.

![flow](https://d33wubrfki0l68.cloudfront.net/56c5fc77f04b5ab89b7f46795dc9aad585102e2d/7eb3b/img/vault-dynamic-secrets.png)

Resuming: Every service instance gets a unique set of database credentials instead of sharing one.

The vault policies can be found on the link below
[https://learn.hashicorp.com/vault/identity-access-management/iam-policies](https://learn.hashicorp.com/vault/identity-access-management/iam-policies)

### Plus
[Database Root Credential Rotation](https://learn.hashicorp.com/vault/secrets-management/db-root-rotation#challenge)

[TTL related topics](https://hackernoon.com/hashicorp-vault-max_ttl-killed-my-spring-app-zk7p367z)

[Vault Lease renewal](https://cloud.spring.io/spring-cloud-vault/multi/multi_vault-lease-renewal.html)

[Vault Secrets rollback | versioning](https://www.vaultproject.io/docs/commands/kv/rollback)

[Storage Backends](https://www.vaultproject.io/docs/configuration/storage/mssql)

[Web UI](https://learn.hashicorp.com/vault/getting-started/ui)
