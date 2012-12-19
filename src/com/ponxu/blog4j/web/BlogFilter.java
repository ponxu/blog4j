package com.ponxu.blog4j.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ponxu.blog4j.Config;
import com.ponxu.blog4j.dao.DAO;
import com.ponxu.blog4j.dao.MongoDAO;

/**
 * @author xwz
 */
public class BlogFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		// 加载BAE配置, BAE
		Config.checkAndLoadBAE(request);

		try {
			chain.doFilter(request, response);
		} finally {
			DAO.close();
			MongoDAO.close();
		}
		
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

	public void destroy() {
	}

}
