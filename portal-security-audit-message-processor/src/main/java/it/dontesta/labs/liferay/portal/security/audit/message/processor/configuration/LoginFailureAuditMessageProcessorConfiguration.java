package it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Antonio Musarra
 */
@ExtendedObjectClassDefinition(category = "foundation")
@Meta.OCD(
	id = "it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.LoginFailureAuditMessageProcessorConfiguration",
	localization = "content/Language",
	name = "login.failure.audit.message.processor.configuration.name"
)
public interface LoginFailureAuditMessageProcessorConfiguration {

	@Meta.AD(deflt = "false", required = false)
	public boolean enabled();

	@Meta.AD(deflt = "antonio.musarra@gmail.com", required = false)
	public String reportTo();

	@Meta.AD(deflt = "noreply-loginfailure@dontesta.it", required = false)
	public String from();

	@Meta.AD(deflt = "Report Login Failure", required = false)
	public String emailSubject();

}