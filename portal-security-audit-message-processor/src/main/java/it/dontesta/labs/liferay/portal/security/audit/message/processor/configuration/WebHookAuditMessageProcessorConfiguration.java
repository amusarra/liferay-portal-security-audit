/*
  Copyright 2009-present Antonio Musarra's Blog - https://www.dontesta.it

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
 */

package it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Antonio Musarra
 */
@ExtendedObjectClassDefinition(category = "audit")
@Meta.OCD(
	id = "it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.WebHookAuditMessageProcessorConfiguration",
	localization = "content/Language",
	name = "webhook-audit-message-processor-configuration-name"
)
public interface WebHookAuditMessageProcessorConfiguration {

	@Meta.AD(
		deflt = "false", description = "audit-message-processor-enabled-help",
		name = "audit-message-processor-enabled", required = false
	)
	public boolean enabled();

	@Meta.AD(
		deflt = "https://webhook.site/audit/message/processor",
		description = "webhook-audit-message-processor-endpoint-url-help",
		name = "webhook-audit-message-processor-endpoint-url",
		required = false
	)
	public String endPointUrl();

	@Meta.AD(
			deflt = "true", description = "webhook-common-name-check-description",
			name = "webhook-common-name-check-name", required = false
	)
	public boolean commonNameCheck();

	@Meta.AD(
			deflt = "true", description = "webhook-hostname-verifier-description",
			name = "webhook-hostname-verifier-name", required = false
	)
	public boolean hostnameVerifier();

	@Meta.AD(
			deflt = "60000", description = "webhook-client-connection-timeout-description",
			name = "webhook-client-connection-timeout-name", required = false
	)
	public long clientConnectionTimeOut();

	@Meta.AD(
			deflt = "60000", description = "webhook-client-receive-timeout-description",
			name = "webhook-client-receive-timeout-name", required = false
	)
	public long clientReceiveTimeout();

	@Meta.AD(
			deflt = "PKCS12", description = "webhook-trust-store-description",
			name = "webhook-trust-store-name",
			optionLabels = {
					"JKS (Java Keystore)",
					"PKCS (Public-Key Cryptography Standards) #12"
			},
			optionValues = {"JKS", "PKCS12"}, required = false
	)
	public String trustStoreType();

	@Meta.AD(
			deflt = "PKCS12", description = "webhook-key-store-description",
			name = "webhook-key-store-name",
			optionLabels = {
					"JKS (Java Keystore)",
					"PKCS (Public-Key Cryptography Standards) #12"
			},
			optionValues = {"JKS", "PKCS12"}, required = false
	)
	public String keyStoreType();

	@Meta.AD(
			deflt = "", description = "webhook-trust-store-file-description",
			name = "webhook-trust-store-file-name", required = false
	)
	public String trustStoreFile();

	@Meta.AD(
			deflt = "", description = "webhook-key-store-file-description",
			name = "webhook-key-store-file-name", required = false
	)
	public String keyStoreFile();

	@Meta.AD(
			deflt = "", description = "webhook-trust-store-password-description",
			name = "webhook-trust-store-password-name", required = false,
			type = Meta.Type.Password
	)
	public String trustStorePassword();

	@Meta.AD(
			deflt = "", description = "webhook-key-store-password-description",
			name = "webhook-key-store-password-name", required = false,
			type = Meta.Type.Password
	)
	public String keyStorePassword();

	@Meta.AD(
			deflt = "TLSv1.2", description = "webhook-https-protocol-description",
			name = "webhook-https-protocol-name",
			optionLabels = {
					"Transport Layer Security (TLS) 1.1",
					"Transport Layer Security (TLS) 1.2",
					"Transport Layer Security (TLS) 1.3"
			},
			optionValues = {"TLSv1.1", "TLSv1.2", "TLSv1.3"}, required = false
	)
	public String httpsProtocol();

	@Meta.AD(
			deflt = "", description = "webhook-api-key-description",
			name = "webhook-api-key-name", required = false,
			type = Meta.Type.Password
	)
	public String apiKey();

	@Meta.AD(
			deflt = "header", description = "webhook-api-key-location-description",
			name = "webhook-api-key-location-name",
			optionLabels = {
					"HTTP Header",
					"HTTP Cookie",
					"HTTP URL Query Parameter"
			},
			optionValues = {"header", "cookie", "urlQuery"}, required = false
	)
	public String apiKeyLocation();

	@Meta.AD(
			deflt = "", description = "webhook-api-key-param-description",
			name = "webhook-api-key-param-name", required = false
	)
	public String apiKeyParam();

	@Meta.AD(
			deflt = "api-key", description = "webhook-auth-type-description",
			name = "webhook-auth-type-name",
			optionLabels = {
					"None",
					"HTTP Basic",
					"HTTPS Mutual Authentication"
			},
			optionValues = {"none", "basic", "mutual"}, required = false
	)
	public String authType();

}
