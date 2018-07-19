package com.hoperun.micro.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import com.hoperun.micro.security.common.PortalUtils;
import com.hoperun.micro.security.common.RedisUtils;
import com.hoperun.micro.security.common.StringUtil;


@Order(1)
@WebFilter(filterName = "RequestFilter", urlPatterns = "/*")
public class RequestFilter implements Filter {

	@Autowired
	private RedisUtils RedisUtil;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	// 设置对本地接口不过滤token
	private final String LOCALHOST = "127.0.0.1";
//	private final int TIMEOUT = 7200 * 1000;
	// 白名单列表。
	private List<String> whiteListUrl = new ArrayList<String>();
	private String NO_ACCESS_TOKEN_URL = "/noaccesstoken";
	private String ERROR_ACCESS_TOKEN_URL = "/erroraccesstoken";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.debug("RequestFilter init ... ");
		// 加入白名单
		//whiteListUrl.add("/api/user/login");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		logger.debug("RequestFilter doFilter ... ");

		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
		PortalUtils.setResponseAttribute(httpResponse);

		String requestUri = httpRequest.getRequestURI();// 得到请求的资源
		String accessToken = httpRequest.getHeader("Access-Token");

		HttpServletRequest request = httpRequest;
		String remoteAddr = request.getRemoteAddr();// 得到来访者的IP地址
		String remoteHost = request.getRemoteHost();
		String method = request.getMethod();// 得到请求URL地址时使用的方法

		if (StringUtil.isEmpty(requestUri)) {
			logger.error("requestUri or requestUri is null");
			return;
		}

		// localhost : debug
		// OPITONS : do not need token
		if (logger.isDebugEnabled()) {
			logger.debug("remoteAddr:" + (remoteAddr == null ? "" : remoteAddr) + ", remoteHost:"
					+ (remoteHost == null ? "" : remoteHost) + ", method:" + (method == null ? "" : method));
		}

		if (LOCALHOST.equalsIgnoreCase(remoteAddr) || LOCALHOST.equalsIgnoreCase(remoteHost)
				|| method.equalsIgnoreCase("OPTIONS")) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		boolean isWhiteUrl = false;
		// 增加判断白名单。
		if (logger.isDebugEnabled()) {
			logger.debug("requestUri:" + requestUri);
		}
		for (String whiteUrl : whiteListUrl) {
			if (whiteUrl != null && requestUri.matches("(.*)" + whiteUrl.replaceAll("\\*", "(.\\*)"))) {
				isWhiteUrl = true;
				break;
			}
		}

		// 如果是白名单直接返回，不进行权限过滤。
		if (isWhiteUrl) {
			filterChain.doFilter(servletRequest, servletResponse);
			if (logger.isDebugEnabled()) {
				logger.debug("white url");
			}
			return;
		}

		boolean isLogin = false;
		// 判断用户登录。

		if (StringUtil.isEmpty(accessToken)) {
			if (logger.isDebugEnabled()) {
				logger.debug("accessToken is null or empty.");
			}
			httpRequest.getRequestDispatcher(NO_ACCESS_TOKEN_URL).forward(httpRequest, httpResponse);
			logger.error("no access token");
			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("accessToken:" + accessToken);
		}

		try {
			if (RedisUtil.get(accessToken) != null) {
//				RedisUtil.expire(accessToken, TIMEOUT); // 过期时间需求未定
				String value = RedisUtil.get(accessToken).toString();
				if (value != null && !value.equals("")) {
					isLogin = true;
					if (logger.isDebugEnabled()) {
						logger.debug("access token verify OK");
					}
				}
			}
		} catch (Exception e) {
			logger.error("expire access token error");
			// need login again
			isLogin = false;
		}

		// 判断用户是否登录，然后进行拦截，跳转到登录页面。
		if (!isLogin) {
			// Access-Token verify error
			httpRequest.getRequestDispatcher(ERROR_ACCESS_TOKEN_URL).forward(httpRequest, httpResponse);
			return;
		} else {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}
	}

	@Override
	public void destroy() {
		logger.debug("RequestFilter destroy ... ");
	}
}
