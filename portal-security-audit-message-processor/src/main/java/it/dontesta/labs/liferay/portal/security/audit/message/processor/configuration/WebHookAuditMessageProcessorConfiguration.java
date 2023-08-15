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
		description = "webhook-audit-message-processor-endpoint-url-description",
		name = "webhook-audit-message-processor-endpoint-url-name",
		required = false
	)
	public String endPointUrl();

	@Meta.AD(
			deflt = "60000", description = "webhook-client-connection-timeout-description",
			name = "webhook-client-connection-timeout-name", required = false
	)
	public int clientConnectionTimeOut();

	@Meta.AD(
			deflt = "60000", description = "webhook-client-receive-timeout-description",
			name = "webhook-client-receive-timeout-name", required = false
	)
	public int clientReceiveTimeout();

	@Meta.AD(
			deflt = "", description = "webhook-api-key-description",
			name = "webhook-api-key-name", required = false,
			type = Meta.Type.Password
	)
	public String apiKey();

	@Meta.AD(
			deflt = API_KEY_LOCATION_HEADER, description = "webhook-api-key-location-description",
			name = "webhook-api-key-location-name",
			optionLabels = {
					"HTTP Header",
					"HTTP URL Query Parameter"
			},
			optionValues = {API_KEY_LOCATION_HEADER, API_KEY_LOCATION_URL_QUERY}, required = false
	)
	public String apiKeyLocationType();

	@Meta.AD(
			deflt = "", description = "webhook-api-key-location-header-name-description",
			name = "webhook-api-key-location-header-name-name",
			required = false
	)
	public String apiKeyLocationHeaderName();

	@Meta.AD(
			deflt = "", description = "webhook-api-key-location-query-description",
			name = "webhook-api-key-location-query-name", required = false
	)
	public String apiKeyQueryParam();

	public String API_KEY_LOCATION_HEADER = "header";
	public String API_KEY_LOCATION_URL_QUERY = "urlQuery";
}
