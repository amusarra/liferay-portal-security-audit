# Liferay Portal Security Audit

This project refers to the article [Liferay Portal Security Audit](https://goo.gl/2Gx7tP) published by 
Antonio Musarra's Blog. 

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
| **portal-security-message-processor** | Questo modulo contiene i due message processor che abbiamo implementato nel corso di quest’articolo e che sono: Dummy Audit Message Processor e Login Failure Message Processor  |
Table 1. New modules added to the Liferay Portal Security Audit system


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

In the my case $LIFERAY_HOME is set on this directory /opt/liferay-ce-portal-7.0-ga5

Verify the correct deployment of the three bundles via the Liferay log file or 
through the Gogo Shell using the lb command, making sure that the status is 
Active.

```
$ telnet localhost 11311

g! lb|grep Audit
  342|Active     |   10|Liferay Portal Security Audit API (2.0.2)
  343|Active     |   10|Liferay Portal Security Audit Wiring (2.0.4)
  584|Active     |   10|Liferay Portal Security Audit Capture Events (1.0.0)
  586|Active     |   10|Liferay Portal Security Audit Message Processor (1.0.0)
  587|Active     |   10|Liferay Portal Security Audit Router (1.0.0)
```

After installing the three bundles, you can access the configuration via the 
Liferay control panel.

![Liferay PortalSecurity Audit - Configuration](https://www.dontesta.it/wp-content/uploads/2018/01/LiferayPortalSecurityAuditConfiguration_1.png)

Figure 1. OSGi Configuration of the three bundles.

![Liferay PortalSecurity Audit - Dummy Message Processor Configuration](https://www.dontesta.it/wp-content/uploads/2018/01/LiferayPortalSecurityAuditConfiguration_2.png)

Figure 2. OSGi Configuration of the Dummy Message Audit Processor

![Liferay PortalSecurity Audit - Login Failure Message Processor Configuration_3](https://www.dontesta.it/wp-content/uploads/2018/01/LiferayPortalSecurityAuditConfiguration_3.png)

Figure 3. OSGi Configuration of the Login Failure Message Audit Processor

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

![Liferay PortalSecurity Audit - Login Failure Audit Message Processor Email Report](https://www.dontesta.it/wp-content/uploads/2018/01/LiferayPortalSecurityAuditConfiguration_4.png)

Figure 4. Email send by Login Failure Audit Message Processor

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