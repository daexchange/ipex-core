package ai.turbochain.ipex.filter;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志处理
 * 
 * @author fly
 *
 */
public class LogFilter implements Filter {

	public final static Logger logger = LoggerFactory.getLogger(LogFilter.class);

	private String filterName;

	public void init(FilterConfig config) throws ServletException {
		// 获取 Filter的 name，启动Filter
		filterName = config.getFilterName();
		logger.info("启动 Filter: " + filterName);//
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		// 运行前的时间
		long startTime = System.currentTimeMillis();
		// 获取访问的URI
		String requestURI = request.getRequestURI();
		
		// 所有的地址栏参数对比
		requestURI = request.getQueryString() == null ? requestURI 
				: (requestURI + "?" + request.getQueryString());
		try{
			chain.doFilter(request, response);
		} catch (Exception e) {
			long endTime = System.currentTimeMillis();
			// 消耗的总时间
			logger.info(request.getRemoteAddr() + " 访问了 " + requestURI + " 出错,  总用时 " + (endTime - startTime) + " 毫秒。");
		}
		
		
		long endTime = System.currentTimeMillis();
		// 消耗的总时间
//		logger.info(request.getRemoteAddr() + " 访问了 " + requestURI + ", 总用时 " + (endTime - startTime) + " 毫秒。");
		System.out.println(request.getRemoteAddr() + " 访问了 " + requestURI + ", 总用时 " + (endTime - startTime) + " 毫秒。");
		
	}

	/**
	 * 销毁时记录日志
	 */
	public void destroy() {

	}
}
