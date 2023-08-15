/*
  Copyright 2009-2021 Antonio Musarra's Blog - https://www.dontesta.it

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

import com.cloudbees.syslog.Facility;
import com.cloudbees.syslog.MessageFormat;
import com.cloudbees.syslog.Severity;
import com.cloudbees.syslog.sender.AbstractSyslogMessageSender;
import com.cloudbees.syslog.sender.TcpSyslogMessageSender;
import com.cloudbees.syslog.sender.UdpSyslogMessageSender;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.security.audit.AuditMessageProcessor;

import it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.SysLogAuditMessageProcessorConfiguration;

import java.io.IOException;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * @author Antonio Musarra
 */
@Component(
	configurationPid = "it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.SysLogAuditMessageProcessorConfiguration",
	immediate = true, property = "eventTypes=*",
	service = AuditMessageProcessor.class
)
public class SysLogAuditMessageProcessor implements AuditMessageProcessor {

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
		_sysLogAuditMessageProcessorConfiguration =
			ConfigurableUtil.createConfigurable(
				SysLogAuditMessageProcessorConfiguration.class, properties);
	}

	protected void doProcess(AuditMessage auditMessage) {
		if (_sysLogAuditMessageProcessorConfiguration.enabled()) {
			AbstractSyslogMessageSender messageSender =
				_getSysLogMessageSender();

			messageSender.setDefaultMessageHostname(
				auditMessage.getServerName());
			messageSender.setDefaultAppName(
				_sysLogAuditMessageProcessorConfiguration.appName());
			messageSender.setDefaultFacility(
				Facility.fromLabel(
					_sysLogAuditMessageProcessorConfiguration.
						syslogFacility()));
			messageSender.setDefaultSeverity(
				Severity.fromLabel(
					_sysLogAuditMessageProcessorConfiguration.
						syslogSeverity()));
			messageSender.setSyslogServerHostname(
				_sysLogAuditMessageProcessorConfiguration.hostName());
			messageSender.setSyslogServerPort(
				_sysLogAuditMessageProcessorConfiguration.port());

			if (MessageFormat.RFC_3164.name(
				).equals(
					_sysLogAuditMessageProcessorConfiguration.messageFormat()
				)) {

				messageSender.setMessageFormat(MessageFormat.RFC_3164);
			}
			else if (MessageFormat.RFC_5424.name(
					).equals(
						_sysLogAuditMessageProcessorConfiguration.
							messageFormat()
					)) {

				messageSender.setMessageFormat(MessageFormat.RFC_5424);
			}
			else if (MessageFormat.RFC_5425.name(
					).equals(
						_sysLogAuditMessageProcessorConfiguration.
							messageFormat()
					)) {

				messageSender.setMessageFormat(MessageFormat.RFC_5425);
			}

			if (_sysLogAuditMessageProcessorConfiguration.enableSSL() &&
				"TCP".equals(
					_sysLogAuditMessageProcessorConfiguration.protocol())) {

				((TcpSyslogMessageSender)messageSender).setSsl(true);
			}

			try {
				messageSender.sendMessage(
					auditMessage.toJSONObject(
					).toJSONString());
			}
			catch (IOException ioe) {
				if (_log.isErrorEnabled()) {
					_log.error(ioe.getMessage(), ioe);
				}
			}
		}
	}

	private AbstractSyslogMessageSender _getSysLogMessageSender() {
		if ("UDP".equals(
				_sysLogAuditMessageProcessorConfiguration.protocol()) &&
			!_sysLogAuditMessageProcessorConfiguration.enableSSL()) {

			return new UdpSyslogMessageSender();
		}

		if ("TCP".equals(
				_sysLogAuditMessageProcessorConfiguration.protocol())) {

			return new TcpSyslogMessageSender();
		}

		return new UdpSyslogMessageSender();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SysLogAuditMessageProcessor.class);

	private SysLogAuditMessageProcessorConfiguration
		_sysLogAuditMessageProcessorConfiguration;

}