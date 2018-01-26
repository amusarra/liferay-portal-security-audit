package it.dontesta.labs.liferay.portal.security.audit.message.processor;

import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailServiceUtil;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.security.audit.AuditMessageProcessor;
import it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.DummyAuditMessageProcessorConfiguration;
import it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.LoginFailureAuditMessageProcessorConfiguration;
import it.dontesta.labs.liferay.portal.security.audit.router.constants.EventTypes;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import javax.mail.internet.InternetAddress;
import java.util.Map;

/**
 * @author Antonio Musarra
 */
@Component(
	configurationPid = "it.dontesta.labs.liferay.portal.security.audit.message.processor.configuration.LoginFailureAuditMessageProcessorConfiguration",
	immediate = true,
	property = "eventTypes=" + EventTypes.LOGIN_FAILURE,
	service = AuditMessageProcessor.class
)
public class LoginFailureAuditMessageProcessor implements AuditMessageProcessor {

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
				LoginFailureAuditMessageProcessorConfiguration.class, properties);
	}

	protected void doProcess(AuditMessage auditMessage) throws Exception {
		if (_loginFailureAuditMessageProcessorConfiguration.enabled()) {
			if(_log.isDebugEnabled()) {
				_log.debug("Login Failure processor processing this Audit Message => "
					+ auditMessage.toJSONObject());
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

				if(_log.isInfoEnabled()) {
					_log.info("Send report audit email to "
						+ _loginFailureAuditMessageProcessorConfiguration.reportTo());
				}
			} catch (Exception e) {
				if(_log.isWarnEnabled()) {
					_log.warn("Send email failed.", e);
				}
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LoginFailureAuditMessageProcessor.class);

	private volatile LoginFailureAuditMessageProcessorConfiguration
		_loginFailureAuditMessageProcessorConfiguration;
}
