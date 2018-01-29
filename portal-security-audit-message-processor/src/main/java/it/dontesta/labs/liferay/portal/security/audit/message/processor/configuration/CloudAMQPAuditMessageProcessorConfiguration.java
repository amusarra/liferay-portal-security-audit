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
	name = "cloudamqp.audit.message.processor.configuration.name"
)
public interface CloudAMQPAuditMessageProcessorConfiguration {

	@Meta.AD(deflt = "false", required = false)
	public boolean enabled();

	@Meta.AD(deflt = "gopher.rmq.cloudamqp.com", required = false)
	public String serverAddress();

	@Meta.AD(deflt = "", required = false)
	public String userName();

	@Meta.AD(deflt = "", required = false, type = Meta.Type.Password)
	public String password();

	@Meta.AD(deflt = "liferay_audit_login_failure_queue", required = false)
	public String queueName();

}