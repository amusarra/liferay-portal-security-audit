package it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Antonio Musarra
 */
@ExtendedObjectClassDefinition(category = "foundation")
@Meta.OCD(
	id = "it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.DummyAuditMessageProcessorConfiguration",
	localization = "content/Language",
	name = "dummy.audit.message.processor.configuration.name"
)
public interface DummyAuditMessageProcessorConfiguration {

	@Meta.AD(deflt = "false", required = false)
	public boolean enabled();

}