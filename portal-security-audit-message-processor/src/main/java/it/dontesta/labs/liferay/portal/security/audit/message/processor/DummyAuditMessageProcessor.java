package it.dontesta.labs.liferay.portal.security.audit.message.processor;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
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

	protected void doProcess(AuditMessage auditMessage) throws Exception {
		if (_dummyAuditMessageProcessorConfiguration.enabled()) {
			if (_log.isInfoEnabled()) {
				_log.info("Dummy processor processing this Audit Message => " +
					auditMessage.toJSONObject());
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DummyAuditMessageProcessor.class);

	private volatile DummyAuditMessageProcessorConfiguration
		_dummyAuditMessageProcessorConfiguration;

}