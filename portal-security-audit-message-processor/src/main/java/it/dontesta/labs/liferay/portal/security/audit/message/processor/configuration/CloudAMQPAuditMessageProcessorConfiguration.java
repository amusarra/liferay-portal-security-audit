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

package it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Antonio Musarra
 */
@ExtendedObjectClassDefinition(category = "foundation")
@Meta.OCD(
	id = "it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.CloudAMQPAuditMessageProcessorConfiguration",
	localization = "content/Language",
	name = "cloudamqp-audit-message-processor-configuration-name"
)
public interface CloudAMQPAuditMessageProcessorConfiguration {

	@Meta.AD(
		deflt = "false", required = false,
		description = "audit-message-processor-enabled-help",
		name = "audit-message-processor-enabled"
	)
	public boolean enabled();

	@Meta.AD(
		deflt = "gopher.rmq.cloudamqp.com",
		description = "cloudamqp-audit-message-processor-server-address-help",
		name = "cloudamqp-audit-message-processor-server-address",
		required = false
	)
	public String serverAddress();

	@Meta.AD(
		deflt = "",
		description = "cloudamqp-audit-message-processor-username-help",
		name = "cloudamqp-audit-message-processor-username", required = false
	)
	public String userName();

	@Meta.AD(
		deflt = "",
		description = "cloudamqp-audit-message-processor-password-help",
		name = "cloudamqp-audit-message-processor-password", required = false,
		type = Meta.Type.Password
	)
	public String password();

	@Meta.AD(
		deflt = "liferay_audit_queue",
		description = "cloudamqp-audit-message-processor-queue-name-help",
		name = "cloudamqp-audit-message-processor-queue-name", required = false
	)
	public String queueName();

}