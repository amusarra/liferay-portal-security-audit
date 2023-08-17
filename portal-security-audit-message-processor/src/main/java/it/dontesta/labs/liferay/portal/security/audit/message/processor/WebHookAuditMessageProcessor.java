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

package it.dontesta.labs.liferay.portal.security.audit.message.processor;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.audit.AuditMessageProcessor;

import it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.WebHookAuditMessageProcessorConfiguration;

import java.io.IOException;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * @author Antonio Musarra
 */
@Component(
	configurationPid = "it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.WebHookAuditMessageProcessorConfiguration",
	immediate = true, property = "eventTypes=" + StringPool.STAR,
	service = AuditMessageProcessor.class
)
public class WebHookAuditMessageProcessor implements AuditMessageProcessor {

	@Override
	public void process(AuditMessage auditMessage) {
		try {
			doProcess(auditMessage);
		}
		catch (Exception e) {
			_log.fatal("Unable to process audit message " + auditMessage, e);
		}
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_webHookAuditMessageProcessorConfiguration =
			ConfigurableUtil.createConfigurable(
				WebHookAuditMessageProcessorConfiguration.class, properties);

		if (_log.isInfoEnabled()) {
			_log.info(
				"Web Hook Audit Message Processor enabled: " +
					_webHookAuditMessageProcessorConfiguration.enabled());
		}
	}

	protected void doProcess(AuditMessage auditMessage) throws IOException {
		if (_webHookAuditMessageProcessorConfiguration.enabled()) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"WebHook Audit Message processor processing " +
						"this Audit Message => " + auditMessage.toJSONObject());
			}

			RequestConfig requestConfig = RequestConfig.custom(
			).setConnectTimeout(
				_getClientConnectionTimeOut()
			).setSocketTimeout(
				_getClientReceiveTimeout()
			).build();

			try (CloseableHttpClient httpClient = HttpClients.custom(
				).setDefaultRequestConfig(
					requestConfig
				).build()) {

				HttpPost httpPost = new HttpPost(_getEndPointUrl());

				if (Validator.isNotNull(_getApiKey()) &&
					_getApiKeyLocationType().equals(
						WebHookAuditMessageProcessorConfiguration.
							API_KEY_LOCATION_HEADER)) {

					httpPost.setHeader(
						_getApiKeyLocationHeaderName(), _getApiKey());
				}

				if (Validator.isNotNull(_getApiKey()) &&
					_getApiKeyLocationType().equals(
						WebHookAuditMessageProcessorConfiguration.
							API_KEY_LOCATION_URL_QUERY)) {

					httpPost.setURI(
						httpPost.getURI(
						).resolve(
							String.format(
								"?%s=%s", _getApiKeyQueryParam(), _getApiKey())
						));
				}

				StringEntity entity = new StringEntity(
					auditMessage.toJSONObject(
					).toJSONString());

				entity.setContentType("application/json");
				httpPost.setEntity(entity);

				try (CloseableHttpResponse response = httpClient.execute(
						httpPost)) {

					int statusCode = response.getStatusLine(
					).getStatusCode();

					if (statusCode != Response.Status.OK.getStatusCode()) {
						_log.error(
							String.format(
								"Error on send Audit Message to Slack: {response code: %s, response content: %s}",
								statusCode,
								response.getEntity(
								).getContent()));
					}

					if (_log.isDebugEnabled()) {
						_log.debug(
							String.format(
								"Audit Message sent to Slack: {response code: %s, response content: %s}",
								statusCode,
								response.getEntity(
								).getContent()));
					}
				}
				catch (IOException ioException) {
					throw ioException;
				}
			}
			catch (IOException ioException) {
				throw ioException;
			}
		}
	}

	private String _getApiKey() {
		return _webHookAuditMessageProcessorConfiguration.apiKey();
	}

	private String _getApiKeyLocationHeaderName() {
		return _webHookAuditMessageProcessorConfiguration.
			apiKeyLocationHeaderName();
	}

	private String _getApiKeyLocationType() {
		return _webHookAuditMessageProcessorConfiguration.apiKeyLocationType();
	}

	private String _getApiKeyQueryParam() {
		return _webHookAuditMessageProcessorConfiguration.apiKeyQueryParam();
	}

	private int _getClientConnectionTimeOut() {
		return _webHookAuditMessageProcessorConfiguration.
			clientConnectionTimeOut();
	}

	private int _getClientReceiveTimeout() {
		return _webHookAuditMessageProcessorConfiguration.
			clientReceiveTimeout();
	}

	private String _getEndPointUrl() {
		return _webHookAuditMessageProcessorConfiguration.endPointUrl();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		WebHookAuditMessageProcessor.class);

	private WebHookAuditMessageProcessorConfiguration
		_webHookAuditMessageProcessorConfiguration;

}