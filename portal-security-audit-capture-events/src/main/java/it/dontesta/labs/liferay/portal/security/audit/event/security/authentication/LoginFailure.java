package it.dontesta.labs.liferay.portal.security.audit.event.security.authentication;

import com.liferay.portal.kernel.audit.AuditException;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.audit.AuditRouter;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.AuthFailure;
import com.liferay.portal.kernel.service.UserLocalService;

import it.dontesta.labs.liferay.portal.security.audit.router.constants.EventTypes;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Antonio Musarra
 */
@Component(
	immediate = true, property = {"key=auth.failure"},
	service = AuthFailure.class
)
public class LoginFailure implements AuthFailure {

	@Override
	public void onFailureByEmailAddress(
		long companyId, String emailAddress, Map<String, String[]> headerMap,
		Map<String, String[]> parameterMap) {

		try {
			User user = _userLocalService.getUserByEmailAddress(
				companyId, emailAddress);

			AuditMessage auditMessage = _buildAuditMessage(
				user, headerMap, "Authentication failed with email address");

			_auditRouter.route(auditMessage);
		}
		catch (AuditException ae) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to route audit message {" +
					ae.getMessage() +
					"}", ae);
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e.getMessage(), e);
			}
		}
	}

	@Override
	public void onFailureByScreenName(
		long companyId, String screenName, Map<String, String[]> headerMap,
		Map<String, String[]> parameterMap) {

		try {
			User user = _userLocalService.getUserByScreenName(
				companyId, screenName);

			AuditMessage auditMessage = _buildAuditMessage(
				user, headerMap, "Authentication failed with screen name");

			_auditRouter.route(auditMessage);
		}
		catch (AuditException ae) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to route audit message {" +
						ae.getMessage() +
						"}", ae);
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e.getMessage(), e);
			}
		}
	}

	@Override
	public void onFailureByUserId(
		long companyId, long userId, Map<String, String[]> headerMap,
		Map<String, String[]> parameterMap) {

		try {
			User user = _userLocalService.getUserById(companyId, userId);

			AuditMessage auditMessage = _buildAuditMessage(
				user, headerMap, "Authentication failed with user ID");

			_auditRouter.route(auditMessage);
		}
		catch (AuditException ae) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to route audit message {" +
						ae.getMessage() +
						"}", ae);
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e.getMessage(), e);
			}
		}
	}

	/**
	 * Utility for build audit message
	 *
	 * @param user
	 * @param headerMap
	 * @param reason
	 * @return
	 */
	private AuditMessage _buildAuditMessage(
		User user, Map<String, String[]> headerMap, String reason) {

		JSONObject additionalInfoJSONObject = _jsonFactory.createJSONObject();

		additionalInfoJSONObject.put(
			"headers", _jsonFactory.serialize(headerMap));
		additionalInfoJSONObject.put("reason", reason);

		AuditMessage auditMessage = new AuditMessage(
			EventTypes.LOGIN_FAILURE, user.getCompanyId(), user.getUserId(),
			user.getFullName(), User.class.getName(),
			String.valueOf(user.getPrimaryKey()), null,
			additionalInfoJSONObject);

		return auditMessage;
	}

	private static final Log _log = LogFactoryUtil.getLog(LoginFailure.class);

	@Reference
	private AuditRouter _auditRouter;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private UserLocalService _userLocalService;

}