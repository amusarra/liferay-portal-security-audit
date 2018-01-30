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

package it.dontesta.labs.liferay.portal.security.audit.router;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.audit.AuditException;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.audit.AuditRouter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.messaging.proxy.ProxyMessageListener;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.audit.AuditMessageProcessor;
import com.liferay.portal.security.audit.configuration.AuditConfiguration;

import it.dontesta.labs.liferay.portal.security.audit.router.constants.AuditConstants;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Antonio Musarra
 */
@Component(
	configurationPid = "com.liferay.portal.security.audit.configuration.AuditConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true,
	service = StandardAuditRouter.class
)
public class StandardAuditRouter implements AuditRouter {

	@Override
	public boolean isDeployed() {
		int auditMessageProcessorsCount = _auditMessageProcessors.size();

		if ((auditMessageProcessorsCount > 0) ||
			!_globalAuditMessageProcessors.isEmpty()) {

			return true;
		}

		return false;
	}

	@Override
	public void route(AuditMessage auditMessage) throws AuditException {
		if (!getAuditEnabled()) {
			if (_log.isWarnEnabled()) {
				_log.warn("Liferay Portal Security Audit disabled, " +
					"not processing this message: => " +
					auditMessage.toJSONObject());
			}

			return;
		}

		if (!isDeployed()) {
			if (_log.isWarnEnabled()) {
				_log.warn("No Audit Message Processor installed.");
			}

			return;
		}

		String eventType = auditMessage.getEventType();

		for (AuditMessageProcessor globalAuditMessageProcessor :
			_globalAuditMessageProcessors) {

			globalAuditMessageProcessor.process(auditMessage);
		}

		Set<AuditMessageProcessor> auditMessageProcessors =
			_auditMessageProcessors.get(eventType);

		if (auditMessageProcessors != null) {
			for (AuditMessageProcessor auditMessageProcessor :
				auditMessageProcessors) {

				auditMessageProcessor.process(auditMessage);
			}
		}
	}

	@Activate
	@Modified
	protected void activate(BundleContext bundleContext,
							Map<String, Object> properties) {

		_auditConfiguration =
			ConfigurableUtil.createConfigurable(AuditConfiguration.class,
				properties);

		if (_log.isInfoEnabled()) {
			_log.info("Liferay Portal Security Audit enabled : " +
				getAuditEnabled());
		}

		ProxyMessageListener proxyMessageListener = new ProxyMessageListener();

		proxyMessageListener.setManager(this);
		proxyMessageListener.setMessageBus(_messageBus);

		Dictionary<String, Object> proxyMessageListenerProperties
			= new HashMapDictionary<>();

		proxyMessageListenerProperties.put(
			"destination.name", DestinationNames.AUDIT);

		_serviceRegistration = bundleContext.registerService(
			ProxyMessageListener.class, proxyMessageListener,
			proxyMessageListenerProperties);
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}
	}

	@Reference (
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		unbind = "unsetAuditMessageProcessor"
	)
	protected void setAuditMessageProcessor(
		AuditMessageProcessor auditMessageProcessor,
		Map<String, Object> properties) {

		String[] eventTypes = getEventTypes(properties);

		if ((eventTypes.length == 1) && eventTypes[0].equals(StringPool.STAR)) {
			_globalAuditMessageProcessors.add(auditMessageProcessor);

			return;
		}

		for (String eventType : eventTypes) {
			Set<AuditMessageProcessor> auditMessageProcessorsSet =
				_auditMessageProcessors.get(eventType);

			if (auditMessageProcessorsSet == null) {
				auditMessageProcessorsSet = new HashSet<>();

				_auditMessageProcessors.put(
					eventType, auditMessageProcessorsSet);
			}

			auditMessageProcessorsSet.add(auditMessageProcessor);
		}
	}

	protected void unsetAuditMessageProcessor(
		AuditMessageProcessor auditMessageProcessor,
		Map<String, Object> properties) {

		String[] eventTypes = getEventTypes(properties);

		if ((eventTypes.length == 1) && eventTypes[0].equals(StringPool.STAR)) {
			_globalAuditMessageProcessors.remove(auditMessageProcessor);

			return;
		}

		for (String eventType : eventTypes) {
			Set<AuditMessageProcessor> auditMessageProcessorsSet =
				_auditMessageProcessors.get(eventType);

			if (auditMessageProcessorsSet == null) {
				continue;
			}

			auditMessageProcessorsSet.remove(auditMessageProcessor);
		}
	}

	private boolean getAuditEnabled() {
		return _auditConfiguration.enabled();
	}

	/**
	 * Get eventType from OSGi properties
	 *
	 * @param properties OSGi properties
	 * @return String array of the eventType
	 */
	private String[] getEventTypes(Map<String, Object> properties) {
		String eventTypes = (String)properties.get(AuditConstants.EVENT_TYPES);

		if (Validator.isNull(eventTypes)) {
			throw new IllegalArgumentException(
				"The property \"" + AuditConstants.EVENT_TYPES + "\" is null");
		}

		return StringUtil.split(eventTypes);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		StandardAuditRouter.class);

	private volatile AuditConfiguration _auditConfiguration;
	private final Map<String, Set<AuditMessageProcessor>>
		_auditMessageProcessors = new ConcurrentHashMap<>();
	private final List<AuditMessageProcessor> _globalAuditMessageProcessors =
		new CopyOnWriteArrayList<>();

	@Reference
	private MessageBus _messageBus;

	private ServiceRegistration<ProxyMessageListener> _serviceRegistration;

}