# Liferay 7 Portal Security Audit
[![Antonio Musarra's Blog](https://img.shields.io/badge/maintainer-Antonio_Musarra's_Blog-purple.svg?colorB=6e60cc)](https://www.dontesta.it)
[![Build Status](https://travis-ci.org/amusarra/liferay-portal-security-audit.svg?branch=master)](https://travis-ci.org/amusarra/liferay-portal-security-audit)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=amusarra_liferay-portal-security-audit&metric=alert_status)](https://sonarcloud.io/dashboard?id=amusarra_liferay-portal-security-audit)
[![Twitter Follow](https://img.shields.io/twitter/follow/antonio_musarra.svg?style=social&label=%40antonio_musarra%20on%20Twitter&style=plastic)](https://twitter.com/antonio_musarra)


This project refers to the ebook [Liferay Portal Security Audit](https://goo.gl/AC8VRo) published by
[Antonio Musarra's Blog](https://www.dontesta.it) on the Amazon Kindle Store.

At the beginning of the article the source code of
Liferay Portal Security Audit (freely available on GitHub) was examined.
Later, in the article we also discussed how to implement the OSGi components
necessary to obtain a Audit Service system running on the Community Edition of
Liferay. The project is organized as described in Table 1.

![Liferay Portal Security Audit - Architecture](https://www.dontesta.it/wp-content/uploads/2018/01/LiferayPortalSecurityAuditArchitecture_v1.0.0.png)

Figure 1. Macro Architecture of Liferay Portal Security Audit

| Name of the module  | Description |
| ------------- | ------------- |
| **portal-security-audit-capture-events**  | This module contains components that capture portal events such as AuthFailure. These components trace events by sending them to message processors through the Audit Router  |
| **portal-security-audit-router**  | This module contains the Standard Audit router component that implements the Audit Router interface  |
| **portal-security-message-processor** | This module contains the two message processors that we have implemented in the course of this article and which are: Dummy Audit Message Processor and Login Failure Message Processor  |

Table 1. New modules added to the Liferay Portal Security Audit system

Version 7.1 of Liferay has *introduced the implementation of a default router*, 
for this reason in this version of the project there is no longer the bundle
**portal-security-audit-router**.

The module **portal-security-audit-capture-events** contains the follow OSGi components for capture this events:
1. Login Failure
2. Login Post Action
3. Logout Post Action

The module **portal-security-message-processor** contains the follow OSGi components for processing audit messages:
1. Dummy Audit Message Processor
2. Login Failure Message Processor
3. Cloud AMQP Audit Message Processor

For more information about the *Cloud AMQP Audit Message Processor* I advise you to read
[CloudAMQP Audit Message Processor for Liferay 7/DXP](https://dzone.com/articles/liferay-7-cloud-amqp-audit-message-processor)
that I published on DZone portal.

This project is an excellent starting point that you can certainly extend
according to your needs, thus obtaining an Audit Service system starting
from the framework at the base of the Liferay Portal Security Audit.
Shows the steps necessary to obtain and install the three modules
shown in Table 1.

```
$ git clone https://github.com/amusarra/liferay-portal-security-audit.git
$ cd liferay-portal-security-audit
$ ./gradlew clean deploy
$ cp ../bundles/osgi/modules/*.jar $LIFERAY_HOME/deploy/
```

In the my case $LIFERAY_HOME is set on this directory /Users/antoniomusarra/dev/liferay/liferay-ce-portal-7.1.0-ga1

Verify the correct deployment of the two bundles via the Liferay log file or
through the Gogo Shell using the lb command, making sure that the status is
Active.

From Liferay version 7.1 GA1 access to the GogoShell via telnet has been disabled. 
To re-enable access, you need to set the portal in developer mode. Form more info
read this [setting developer mode for your server using portal-developer.properties](https://dev.liferay.com/en/develop/tutorials/-/knowledge_base/7-1/using-developer-mode-with-themes#setting-developer-mode-for-your-server-using-portal-developer-properties)

```
$ telnet localhost 11311

g! lb | grep Audit
  621|Active     |   10|Liferay CE Foundation - Liferay CE Security Audit - API (1.0.0)
  622|Active     |   10|Liferay Portal Security Audit API (3.0.1)
  623|Active     |   10|Liferay Portal Security Audit Event Generators API (2.0.0)
  624|Active     |   10|Liferay Portal Security Audit Storage API (3.0.0)
  716|Active     |   10|Liferay CE Foundation - Liferay CE Security Audit - Impl (1.0.0)
  717|Active     |   10|Liferay Portal Security Audit Event Generators User Management (2.0.0)
  718|Active     |   10|Liferay Portal Security Audit Implementation (1.0.1)
  719|Active     |   10|Liferay Portal Security Audit Router (3.0.1)
  720|Active     |   10|Liferay Portal Security Audit Storage Service (3.0.0)
  721|Active     |   10|Liferay Portal Security Audit Wiring (3.0.0)
  943|Active     |   10|Liferay Portal Security Audit Capture Events (1.1.0)
  944|Active     |   10|Liferay Portal Security Audit Message Processor (1.1.0)
```
As you can see, version 7.1 of Liferay has introduced several more bundles about 
the audit framework. One of the most important bundles is the one that implements 
the Audit Router.

After installing the two bundles, you can access the configuration via the
Liferay control panel.

![Liferay Portal Security Audit - Configuration](https://www.dontesta.it/wp-content/uploads/2018/09/LiferayPortalSecurityAudit_Configuration_1.png)

Figure 1. OSGi Configuration of the Audit bundles.

![Liferay Portal Security Audit - Audit Configuration](https://www.dontesta.it/wp-content/uploads/2018/09/LiferayPortalSecurityAudit_Configuration_2.png)

Figure 2. General Audit Configuration and configuration for the custom Audit Message Processor

![Liferay Portal Security Audit - Dummy Message Processor Configuration](https://www.dontesta.it/wp-content/uploads/2018/09/LiferayPortalSecurityAudit_Configuration_3.png)

Figure 3. OSGi Configuration of the Dummy Message Audit Processor

![Liferay Portal Security Audit - Login Failure Message Processor Configuration](https://www.dontesta.it/wp-content/uploads/2018/09/LiferayPortalSecurityAudit_Configuration_4.png)

Figure 4. OSGi Configuration of the Login Failure Message Audit Processor

![Liferay Portal Security Audit - CloudAMQP Message Processor Configuration](https://www.dontesta.it/wp-content/uploads/2018/09/LiferayPortalSecurityAudit_Configuration_5.png)

Figure 4. OSGi Configuration of the CloudAMQP Message Audit Processor

If you enable Audit, then the two message processors and finally the Scheduler
Helper Engine, on Liferay log files, you will see the audit messages (of the
running jobs, of the login processes, etc.). If you were to fail the login
process, you should see the attempt to send the email containing the audit
message to the log file.

```
15:30:42,954 INFO  [liferay/audit-1][DummyAuditMessageProcessor:48] Dummy
processor processing this Audit Message =>
{"companyId":"20116","classPK":"20156","clientHost":"127.0.0.1","clientIP":
"127.0.0.1","serverName":"localhost","className":"com.liferay.portal.kernel.model.User",
"sessionID":"6C77D209E6068DAC47FFA4435B7B05B6","eventType":"LOGIN",
"serverPort":8080,"userName":"Test Test","userId":"20156",
"timestamp":"20180128153042954"}
```
Log 1. Dummy Audit Message Processor that trace the LOGIN event

```
15:56:13,993 INFO  [liferay/audit-1][LoginFailureAuditMessageProcessor:75]
Send report audit email to antonio.musarra@gmail.com
```
Log 2. Login Failure Audit Message Processor that trace LOGIN_FAILURE event
and send email

```
2018-09-11 20:12:45.037 INFO  [liferay/audit-1][CloudAMQPAuditMessageProcessor:125]
 Message Audit processed and published on liferay_audit_queue Cloud AMQP queue. 
 Details {{product=RabbitMQ, copyright=Copyright (c) 2007-2017 Pivotal Software, 
 Inc., capabilities=
 {exchange_exchange_bindings=true, connection.blocked=true, 
 authentication_failure_close=true, basic.nack=true, publisher_confirms=true, 
 consumer_cancel_notify=true}, information=Licensed under the MPL. 
 See http://www.rabbitmq.com/, version=5.1.2, platform=Java}}
```
Log 3. CloudAMQP Audit Message Processor that trace LOGIN_FAILURE event

![Liferay PortalSecurity Audit - Login Failure Audit Message Processor Email Report](https://www.dontesta.it/wp-content/uploads/2018/01/LiferayPortalSecurityAuditConfiguration_4.png)

Figure 5. Email send by Login Failure Audit Message Processor

## Team Tools

[![alt tag](http://pylonsproject.org/img/logo-jetbrains.png)](https://www.jetbrains.com/) 

Antonio Musarra's Blog Team would like inform that JetBrains is helping by 
provided IDE to develop the application. Thanks to its support program for 
an Open Source projects !

[![alt tag](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/dashboard?id=amusarra_liferay-portal-security-audit)

Liferay Portal Security Audit project is using SonarCloud for code quality. 
Thanks to SonarQube Team for free analysis solution for open source projects.

## License
MIT License

Copyright 2018 Antonio Musarra's Blog - https://www.dontesta.it

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
