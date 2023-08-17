/*
 Copyright 2009-2023 Antonio Musarra's Blog - https://www.dontesta.it

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

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.security.audit.AuditMessageProcessor;

import it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.SlackAuditMessageProcessorConfiguration;

import java.io.IOException;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * @author Antonio Musarra
 */
@Component(
	configurationPid = "it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.SlackAuditMessageProcessorConfiguration",
	immediate = true, property = "eventTypes=*",
	service = AuditMessageProcessor.class
)
public class SlackAuditMessageProcessor implements AuditMessageProcessor {

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
		_slackAuditMessageProcessorConfiguration =
			ConfigurableUtil.createConfigurable(
				SlackAuditMessageProcessorConfiguration.class, properties);

		if (_log.isInfoEnabled()) {
			_log.info(
				"Slack Audit Message Processor enabled: " +
					_slackAuditMessageProcessorConfiguration.enabled());
		}
	}

	protected void doProcess(AuditMessage auditMessage)
		throws IOException, JSONException {

		if (_slackAuditMessageProcessorConfiguration.enabled()) {
			_log.info(
				"Slack processor processing this Audit Message => " +
					auditMessage.toJSONObject());

			JSONObject jsonObjectSectionHeader =
				JSONFactoryUtil.createJSONObject();
			JSONObject jsonObjectSectionBody =
				JSONFactoryUtil.createJSONObject();
			JSONObject jsonObjectSectionContext =
				JSONFactoryUtil.createJSONObject();
			JSONArray jsonArrayBlocks = JSONFactoryUtil.createJSONArray();
			JSONArray jsonArrayContext = JSONFactoryUtil.createJSONArray();

			jsonObjectSectionHeader.put(
				"text",
				JSONFactoryUtil.createJSONObject(
				).put(
					"type", "plain_text"
				).put(
					"text",
					String.format(
						"Audit Message from: %s%nBelow the Audit Message details.",
						SlackAuditMessageProcessor.class.getSimpleName())
				));
			jsonObjectSectionHeader.put("type", "section");

			jsonObjectSectionBody.put("type", "section");

			jsonObjectSectionBody.put(
				"text",
				JSONFactoryUtil.createJSONObject(
				).put(
					"type", "mrkdwn"
				).put(
					"text",
					String.format(
						"```%s```",
						auditMessage.toJSONObject(
						).toString(
							4
						))
				));

			jsonObjectSectionContext.put("type", "context");
			jsonArrayContext.put(
				JSONFactoryUtil.createJSONObject(
				).put(
					"type", "plain_text"
				).put(
					"text", "Author: Slack Audit Message Processor"
				));
			jsonObjectSectionContext.put("elements", jsonArrayContext);

			jsonArrayBlocks.put(jsonObjectSectionHeader);
			jsonArrayBlocks.put(jsonObjectSectionBody);
			jsonArrayBlocks.put(jsonObjectSectionContext);

			Http.Options options = new Http.Options();
			options.setLocation(
				_slackAuditMessageProcessorConfiguration.webHookUrl());
			options.setPost(true);
			options.setBody(
				JSONFactoryUtil.createJSONObject(
				).put(
					"blocks", jsonArrayBlocks
				).toString(),
				"application/json", "UTF-8");

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Slack processor request body => " +
						options.getBody(
						).getContent());
			}

			String responseContent = HttpUtil.URLtoString(options);

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Slack processor response content => " + responseContent);
			}

			Http.Response response = options.getResponse();

			if (response.getResponseCode() !=
					Response.Status.OK.getStatusCode()) {

				_log.error(
					String.format(
						"Error on send Audit Message to Slack: {response code: %s, response content: %s}",
						response.getResponseCode(), responseContent));
			}

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Slack processor response code => " +
						response.getResponseCode());
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SlackAuditMessageProcessor.class);

	private SlackAuditMessageProcessorConfiguration
		_slackAuditMessageProcessorConfiguration;

}