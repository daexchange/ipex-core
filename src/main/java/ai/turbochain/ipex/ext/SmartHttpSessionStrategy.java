package ai.turbochain.ipex.ext;

import org.apache.commons.lang.StringUtils;
import org.springframework.session.Session;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Session 处理
 * @author fly
 *
 */
public class SmartHttpSessionStrategy implements HttpSessionStrategy {
    private CookieHttpSessionStrategy browser;
    private HeaderHttpSessionStrategy api;
    private String tokenName = "x-auth-token";

    public SmartHttpSessionStrategy(CookieHttpSessionStrategy browser, HeaderHttpSessionStrategy api) {
        this.browser = browser;
        this.api = api;
    }

    /**
     * 	从提供的{@link javax.servlet.http.HttpServletRequest}获取请求的会话ID。
     * 	例如，会话ID可能来自Cookie或请求标头。
     */
    @Override
    public String getRequestedSessionId(HttpServletRequest request) {
        String paramToken = request.getParameter(tokenName);
        if (StringUtils.isNotEmpty(paramToken)) {
            return paramToken;
        }
        return getStrategy(request).getRequestedSessionId(request);
    }

    /**
     * 	此方法在创建新会话时调用，并应通知客户端新会话ID是什么。 
	*	例如，它可能创建一个带有会话ID的新Cookie，或者设置一个带有新会话ID值的HTTP响应头。
	*	注意这里的 session 是 org.springframework.session.Session
     */
    @Override
    public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
    	String x1 = response.getHeader("x-auth-token");
        getStrategy(request).onNewSession(session, request, response);
        String x2 = response.getHeader("x-auth-token");
    }

    /**
     * 	当会话无效时调用此方法，并应通知客户端会话标识不再有效。 
     *	例如，它可能会删除其中包含会话ID的Cookie，或者设置一个带有空值的HTTP响应标头，指示客户端不再提交该会话ID。
     */
    @Override
    public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
        getStrategy(request).onInvalidateSession(request, response);
    }

    private HttpSessionStrategy getStrategy(HttpServletRequest request) {
        String authType = request.getHeader("x-auth-token");
        if (authType == null) {
            return this.browser;
        } else {
            return this.api;
        }
    }
}