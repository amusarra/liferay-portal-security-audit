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

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.audit.AuditException;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.security.audit.AuditMessageProcessor;

import it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.DummyAuditMessageProcessorConfiguration;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * @author Antonio Musarra
 */
@Component(
	configurationPid = "it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.DummyAuditMessageProcessorConfiguration",
	immediate = true, property = "eventTypes=*",
	service = AuditMessageProcessor.class
)
public class DummyAuditMessageProcessor implements AuditMessageProcessor {

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
		_dummyAuditMessageProcessorConfiguration =
			ConfigurableUtil.createConfigurable(
				DummyAuditMessageProcessorConfiguration.class, properties);
	}

	protected void doProcess(AuditMessage auditMessage) {
		if (_dummyAuditMessageProcessorConfiguration.enabled()) {
			if (_log.isInfoEnabled()) {
				_log.info(
					"Dummy processor processing this Audit Message => " +
					auditMessage.toJSONObject());
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DummyAuditMessageProcessor.class);

	private DummyAuditMessageProcessorConfiguration
		_dummyAuditMessageProcessorConfiguration;

}