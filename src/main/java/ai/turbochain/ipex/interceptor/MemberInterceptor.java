package ai.turbochain.ipex.interceptor;


import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.osp.blockchain.bean.User;

import ai.turbochain.ipex.constant.SysConstant;
import ai.turbochain.ipex.entity.Member;
import ai.turbochain.ipex.entity.transform.AuthMember;
import ai.turbochain.ipex.event.MemberEvent;
import ai.turbochain.ipex.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 * 	用户验证拦截器
 * @author fly
 *
 */
@Slf4j
public class MemberInterceptor implements HandlerInterceptor {

	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 	处理Session问题
	 */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        log.info(request.getRequestURL().toString());
        
        AuthMember user = (AuthMember) session.getAttribute(SysConstant.SESSION_MEMBER);
        
//        Member webUser = (Member) session.getAttribute(SysConstant.SESSION_MEMBER_WEB);
//        if (user != null || webUser != null) {
      
        String accessToken = request.getHeader("access_token");
        accessToken = "11.67d0bc3246ae1c10cb10e0f72715cc3ae524b372.3600.1576142879";
        if (1==1) {
        //	return true;
        }

        if (user != null) {
            return true;
        } else if (StringUtils.isNotBlank(accessToken)) {
        	log.info("accessToken:{}",accessToken);
            ValueOperations valueOperations = redisTemplate.opsForValue();
           
            Object code = valueOperations.get(accessToken);
            User hardIdUser = (User) request.getAttribute("user");
        	
            if (code!=null&&hardIdUser!=null) {
            	return true;
        	}
            
            ajaxReturn(response, 4000, "当前登录状态过期，请您重新登录！");
        	return false;
        } else {
//        	// API 验证机制    后期添加
//            String token = request.getHeader("api-auth-token");
//            log.info("token:{}",token);
//            
//            //解决service为null无法注入问题
//            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
//            MemberService memberService = (MemberService) factory.getBean("memberService");
//            MemberEvent memberEvent = (MemberEvent) factory.getBean("memberEvent");
//            Member member = memberService.loginWithToken(token, request.getRemoteAddr(), "");
//            if (member != null) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.add(Calendar.HOUR_OF_DAY, 24 * 7);
//                member.setTokenExpireTime(calendar.getTime());
//                memberService.save(member);
//                memberEvent.onLoginSuccess(member, request.getRemoteAddr());
//                session.setAttribute(SysConstant.SESSION_MEMBER, AuthMember.toAuthMember(member));
//                return true;
//            } else {
//                ajaxReturn(response, 4000, "当前登录状态过期，请您重新登录！");
//                return false;
//            }
        	ajaxReturn(response, 4000, "当前登录状态过期，请您重新登录！");
        	return false;
        }
    }


    public void ajaxReturn(HttpServletResponse response, int code, String msg) throws IOException, JSONException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("message", msg);
        out.print(json.toString());
        out.flush();
        out.close();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }
}
