/**
 * Copyright 2009-2020 Antonio Musarra's Blog - https://www.dontesta.it
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

package it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Antonio Musarra
 */
@ExtendedObjectClassDefinition(category = "audit")
@Meta.OCD(
	id = "it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.LoginFailureAuditMessageProcessorConfiguration",
	localization = "content/Language",
	name = "login-failure-audit-message-processor-configuration-name"
)
public interface LoginFailureAuditMessageProcessorConfiguration {

	@Meta.AD(
		deflt = "false", required = false,
		description = "audit-message-processor-enabled-help",
		name = "audit-message-processor-enabled"
	)
	public boolean enabled();

	@Meta.AD(
		deflt = "antonio.musarra@gmail.com",
		description = "login-failure-audit-message-processor-report-to-help",
		name = "login-failure-audit-message-processor-report-to",
		required = false
	)
	public String reportTo();

	@Meta.AD(
		deflt = "noreply-loginfailure@example.com",
		description = "login-failure-audit-message-processor-from-help",
		name = "login-failure-audit-message-processor-from", required = false
	)
	public String from();

	@Meta.AD(
		deflt = "Report Login Failure",
		description = "login-failure-audit-message-processor-email-subject-help",
		name = "login-failure-audit-message-processor-email-subject",
		required = false
	)
	public String emailSubject();

}