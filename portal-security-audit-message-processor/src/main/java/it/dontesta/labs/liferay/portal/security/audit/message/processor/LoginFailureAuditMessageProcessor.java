/**
 * Copyright 2018 Antonio Musarra's Blog - https://www.dontesta.it
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package it.dontesta.labs.liferay.portal.security.audit.message.processor;

import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailServiceUtil;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.security.audit.AuditMessageProcessor;

import com.liferay.portal.security.audit.event.generators.constants.EventTypes;
import it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.LoginFailureAuditMessageProcessorConfiguration;

import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * @author Antonio Musarra
 */
@Component(
	configurationPid = "it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.LoginFailureAuditMessageProcessorConfiguration",
	immediate = true, property = "eventTypes=" + EventTypes.LOGIN_FAILURE,
	service = AuditMessageProcessor.class
)
public class LoginFailureAuditMessageProcessor
	implements AuditMessageProcessor {

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
		_loginFailureAuditMessageProcessorConfiguration =
			ConfigurableUtil.createConfigurable(
				LoginFailureAuditMessageProcessorConfiguration.class,
				properties);
	}

	protected void doProcess(AuditMessage auditMessage) throws Exception {
		if (_loginFailureAuditMessageProcessorConfiguration.enabled()) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Login Failure processor processing " +
					" this Audit Message => " + auditMessage.toJSONObject());
			}

			InternetAddress fromAddress = null;
			InternetAddress toAddress = null;

			try {
				fromAddress = new InternetAddress(
					_loginFailureAuditMessageProcessorConfiguration.from());
				toAddress = new InternetAddress(
					_loginFailureAuditMessageProcessorConfiguration.reportTo());

				MailMessage mailMessage = new MailMessage();

				mailMessage.setTo(toAddress);
				mailMessage.setFrom(fromAddress);
				mailMessage.setSubject(
					_loginFailureAuditMessageProcessorConfiguration.emailSubject());
				mailMessage.setBody(auditMessage.toJSONObject().toString());

				MailServiceUtil.sendEmail(mailMessage);

				if (_log.isInfoEnabled()) {
					_log.info(
						"Send report audit email to " +
						_loginFailureAuditMessageProcessorConfiguration.reportTo());
				}
			} catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn("Send email failed.", e);
				}
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LoginFailureAuditMessageProcessor.class);

	private LoginFailureAuditMessageProcessorConfiguration
		_loginFailureAuditMessageProcessorConfiguration;

}