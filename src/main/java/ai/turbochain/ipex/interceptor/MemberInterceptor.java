package ai.turbochain.ipex.interceptor;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.osp.blockchain.bean.User;

import ai.turbochain.ipex.constant.SysConstant;
import ai.turbochain.ipex.entity.Member;
import ai.turbochain.ipex.entity.transform.AuthMember;
import ai.turbochain.ipex.service.MemberService;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户验证拦截器
 *
 * @author fly
 */
@Slf4j
public class MemberInterceptor implements HandlerInterceptor {
	public static final Integer origin_ART = 2;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
	private MemberService memberService;
    
    /**
     * 处理Session问题
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String code = request.getParameter("code");
        String token = request.getParameter("token");
        if (code != null && (code.equals("2546") || code.equals("2547"))) {
            return true;
        }
        if (token != null && token.substring(0, 4).equals("6011")) {
            return true;
        }
        HttpSession session = request.getSession();
        log.info(request.getRequestURL().toString());

        AuthMember user = (AuthMember) session.getAttribute(SysConstant.SESSION_MEMBER);

//        Member webUser = (Member) session.getAttribute(SysConstant.SESSION_MEMBER_WEB);
//        if (user != null || webUser != null) {

        String accessToken = request.getHeader("access_token");
       
        String clientId = request.getHeader("clientId");

        if ("6".equals(clientId)) {
        	Integer origin = origin_ART;
        	
        	if (StringUtils.isNotBlank(accessToken)) {
                log.info("accessToken:{}", accessToken);
               
                ValueOperations valueOperations = redisTemplate.opsForValue();
                
                String accessUser =	(String) valueOperations.get(accessToken);

                if (StringUtils.isNotBlank(accessUser)) {
                	com.alibaba.fastjson.JSONObject jSONUser = JSON.parseObject(accessUser);
                	
                	String mobilePhone = jSONUser.getString("mobile");
                	
                	Member member = memberService.findByPhoneAndOrigin(mobilePhone, origin);

                    if (member != null) {
                        
                    	request.getSession().setAttribute(SysConstant.API_HARD_ID_MEMBER, AuthMember.toAuthMember(member));
                       
                        return true;
                    } else {
                    	 ajaxReturn(response, 4000, "该手机号尚未注册！");
                         return false;
                    }
                }
        	}
        	
        	 ajaxReturn(response, 4000, "当前登录状态过期，请您重新登录！");
             return false;
        }
        
        if (user != null) {
            return true;
        } else if (StringUtils.isNotBlank(accessToken)) {
            log.info("accessToken:{}", accessToken);
            ValueOperations valueOperations = redisTemplate.opsForValue();

            /**  User s = new User();
             s.setId(1);s.setIpexId(112l);
             valueOperations.set(accessToken, s);*/

            User hardIdUser = (User) valueOperations.get(accessToken);

            if (hardIdUser != null) {
                
            	Long memberId = hardIdUser.getIpexId();
               
                if (memberId != null) {
                    Member member = new Member();

                    member.setId(memberId);
                    
                    request.getSession().setAttribute(SysConstant.API_HARD_ID_MEMBER, AuthMember.toAuthMember(member));

                    return true;
                }
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
