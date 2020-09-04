/*
  Copyright 2009-2020 Antonio Musarra's Blog - https://www.dontesta.it

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

import it.dontesta.labs.liferay.portal.security.audit.message.processor.syslog.util.FacilityNames;
import it.dontesta.labs.liferay.portal.security.audit.message.processor.syslog.util.SeverityNames;

/**
 * @author Antonio Musarra
 */
@ExtendedObjectClassDefinition(category = "audit")
@Meta.OCD(
	id = "it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.SysLogAuditMessageProcessorConfiguration",
	localization = "content/Language",
	name = "syslog-audit-message-processor-configuration-name"
)
public interface SysLogAuditMessageProcessorConfiguration {

	@Meta.AD(
		deflt = "false", description = "audit-message-processor-enabled-help",
		name = "audit-message-processor-enabled", required = false
	)
	public boolean enabled();

	@Meta.AD(
		deflt = "myLiferayInstance",
		description = "syslog-audit-message-processor-app-name-help",
		name = "syslog-audit-message-processor-app-name", required = false
	)
	public String appName();

	@Meta.AD(
		deflt = "127.0.0.0",
		description = "syslog-audit-message-processor-hostname-help",
		name = "syslog-audit-message-processor-hostname", required = false
	)
	public String hostName();

	@Meta.AD(
		deflt = "514", description = "syslog-audit-message-processor-port-help",
		name = "syslog-audit-message-processor-port", required = false
	)
	public int port();

	@Meta.AD(
		deflt = "RFC_3164",
		description = "syslog-audit-message-processor-message-format-help",
		name = "syslog-audit-message-processor-message-format",
		optionLabels = {"RFC-3164", "RFC-5424", "RFC-5425"},
		optionValues = {"RFC_3164", "RFC_5424", "RFC_5425"}, required = false
	)
	public String messageFormat();

	@Meta.AD(
		deflt = "UDP",
		description = "syslog-audit-message-processor-protocol-help",
		name = "syslog-audit-message-processor-protocol",
		optionLabels = {"UDP", "TCP"}, optionValues = {"UDP", "TCP"},
		required = false
	)
	public String protocol();

	@Meta.AD(
		deflt = "false",
		description = "syslog-audit-message-processor-ssl-help",
		name = "syslog-audit-message-processor-ssl", required = false
	)
	public boolean enableSSL();

	@Meta.AD(
		deflt = FacilityNames.FACILITY_AUDIT_LABEL,
		description = "syslog-audit-message-processor-facility-help",
		name = "syslog-audit-message-processor-facility",
		optionLabels = {
			FacilityNames.FACILITY_USER_LABEL,
			FacilityNames.FACILITY_AUDIT_LABEL,
			FacilityNames.FACILITY_ALERT_LABEL,
			FacilityNames.FACILITY_AUTH_LABEL,
			FacilityNames.FACILITY_AUTHPRIV_LABEL,
			FacilityNames.FACILITY_CLOCK_LABEL,
			FacilityNames.FACILITY_CRON_LABEL,
			FacilityNames.FACILITY_DAEMON_LABEL,
			FacilityNames.FACILITY_FTP_LABEL, FacilityNames.FACILITY_KERN_LABEL,
			FacilityNames.FACILITY_LPR_LABEL, FacilityNames.FACILITY_MAIL_LABEL,
			FacilityNames.FACILITY_NTP_LABEL,
			FacilityNames.FACILITY_SYSLOG_LABEL,
			FacilityNames.FACILITY_UUCP_LABEL
		},
		optionValues = {
			FacilityNames.FACILITY_USER_LABEL,
			FacilityNames.FACILITY_AUDIT_LABEL,
			FacilityNames.FACILITY_ALERT_LABEL,
			FacilityNames.FACILITY_AUTH_LABEL,
			FacilityNames.FACILITY_AUTHPRIV_LABEL,
			FacilityNames.FACILITY_CLOCK_LABEL,
			FacilityNames.FACILITY_CRON_LABEL,
			FacilityNames.FACILITY_DAEMON_LABEL,
			FacilityNames.FACILITY_FTP_LABEL, FacilityNames.FACILITY_KERN_LABEL,
			FacilityNames.FACILITY_LPR_LABEL, FacilityNames.FACILITY_MAIL_LABEL,
			FacilityNames.FACILITY_NTP_LABEL,
			FacilityNames.FACILITY_SYSLOG_LABEL,
			FacilityNames.FACILITY_UUCP_LABEL
		},
		required = false
	)
	public String syslogFacility();

	@Meta.AD(
		deflt = SeverityNames.SEVERITY_INFORMATIONAL_LABEL,
		description = "syslog-audit-message-processor-severity-help",
		name = "syslog-audit-message-processor-severity",
		optionLabels = {
			SeverityNames.SEVERITY_ALERT_LABEL,
			SeverityNames.SEVERITY_CRITICAL_LABEL,
			SeverityNames.SEVERITY_DEBUG_LABEL,
			SeverityNames.SEVERITY_EMERGENCY_LABEL,
			SeverityNames.SEVERITY_ERROR_LABEL,
			SeverityNames.SEVERITY_INFORMATIONAL_LABEL,
			SeverityNames.SEVERITY_NOTICE_LABEL,
			SeverityNames.SEVERITY_WARNING_LABEL
		},
		optionValues = {
			SeverityNames.SEVERITY_ALERT_LABEL,
			SeverityNames.SEVERITY_CRITICAL_LABEL,
			SeverityNames.SEVERITY_DEBUG_LABEL,
			SeverityNames.SEVERITY_EMERGENCY_LABEL,
			SeverityNames.SEVERITY_ERROR_LABEL,
			SeverityNames.SEVERITY_INFORMATIONAL_LABEL,
			SeverityNames.SEVERITY_NOTICE_LABEL,
			SeverityNames.SEVERITY_WARNING_LABEL
		},
		required = false
	)
	public String syslogSeverity();

}