<%@page import="java.sql.Statement"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ponxu.blog4j.Config"%>
<%@ page import="com.ponxu.run.db.wrap.impl.LongRowWrapper"%>
<%@ page import="com.ponxu.blog4j.dao.DAO"%>
<%@	page import="com.ponxu.run.lang.StringUtils"%>
<%@ page import="com.ponxu.run.lang.FileUtils"%>
<%@ page import="java.io.InputStream"%>

<%
	boolean isInstall = true;
	String method = request.getMethod();
	String ctx = request.getContextPath();
	String msg = "";
	
	// 判断是否已经安装
	try {
		DAO.queryUni(LongRowWrapper.getInstance(), "select count(*) from bj_setting");
	} catch (Throwable t) {
		if (t.getMessage().indexOf("doesn't exist") > -1)
			isInstall = false;
	}
	
	// 已安装, 重定向到首页
	if (isInstall) {
		response.sendRedirect(ctx);
		return;
	}

	// 提交安装数据
	if ("POST".equalsIgnoreCase(method)) {
		String title = request.getParameter("title");
		String subtitle = request.getParameter("subtitle");
		String loginname = request.getParameter("loginname");
		String loginpassword = request.getParameter("loginpassword");
		String analyticscode = request.getParameter("analyticscode");
		String commentcode = request.getParameter("commentcode");
		
		if (StringUtils.isNotEmpty(title) 
				&& StringUtils.isNotEmpty(subtitle)
				&& StringUtils.isNotEmpty(loginname)
				&& StringUtils.isNotEmpty(loginpassword)) {
			
			try {
				InputStream in = FileUtils.fromClassPath("blog4j.sql");
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				String line = null;
				while ((line = br.readLine()) != null) {
					if (StringUtils.isNotEmpty(line)) DAO.execute(line);
				}
				br.close();
				
				DAO.execute("insert into bj_setting values('title','标题','" + title + "');");
				DAO.execute("insert into bj_setting values('subtitle','子标题','" + subtitle + "');");
				DAO.execute("insert into bj_setting values('loginname','登录用户名','" + loginname + "');");
				DAO.execute("insert into bj_setting values('loginpassword','登录密码',md5('" + loginpassword + "'));");
				DAO.execute("insert into bj_setting values('analyticscode','统计代码','" + analyticscode + "');");
				DAO.execute("insert into bj_setting values('commentcode','评论代码','" + commentcode + "');");
				
				// 
				DAO.execute("insert into bj_setting values('weibocode','微薄代码','');");
				DAO.execute("insert into bj_setting values('sharecode','分享代码','');");
				
				response.sendRedirect(ctx + "/admin");
				return;
			} catch (Throwable t) {
				t.printStackTrace();
				msg = t.getMessage();
			}
			
		} else {
			msg = "博客标题, 博客副标题, 登录用户名, 登录密码, 必须填写!";
		}
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>编辑 - Blog4j</title>
<link rel="stylesheet" type="text/css" href="<%=ctx%>/themes/admin/style/admin.css" />
</head>
<body>
	<div id="header-wrapper">
		<div id="header">
			<div id="admin-title" class="left">
				<span class="h1"><a href="http://www.ponxu.com">Blog4j安装</a> | </span>
				<span class="small">简单的, 高效的, 易于管理的个人博客!</span>
			</div>
			<div id="admin-func" class="right">
			</div>
			<div class="clear"></div>
		</div>
	</div>
	<div id="content-wrapper">
<center>
	<%=msg %>
	<form action="" method="post" onsubmit="return confirm('确定安装吗?')">
	<br>
	<table width="720" class="listt">
		<tbody>
			<tr>
				<td width="150" align="right">博客标题</td>
				<td><input type="text" name="title" class="shadowfocus" value="一个博客"></td>
			</tr>
			
			<tr>
				<td width="150" align="right">博客副标题</td>
				<td><input type="text" name="subtitle" class="shadowfocus" value="爱记录,爱分享,爱生活!"></td>
			</tr>
			
			<tr>
				<td width="150" align="right">登录用户名</td>
				<td><input type="text" name="loginname" class="shadowfocus" value="admin"></td>
			</tr>
			
			<tr>
				<td width="150" align="right">登录密码</td>
				<td><input type="text" name="loginpassword" class="shadowfocus" value="admin"></td>
			</tr>
			
			<tr>
				<td width="150" align="right">统计代码</td>
				<td><textarea name="analyticscode" class="shadowfocus">站长统计</textarea></td>
			</tr>
			
			<tr>
				<td width="150" align="right">评论代码</td>
				<td><textarea name="commentcode" class="shadowfocus">多说评论</textarea></td>
			</tr>
			
		</tbody>
	</table>
	<br>
	
	<input type="submit" class="btn2 shadowhover2" value="确定安装">
	
	</form>
</center>
	</div> <!-- end content-wrapper -->
	<div id="footer">
	</div>
	
	<style>
		input[type=text], textarea {
			width: 400px;
		}
		textarea {
			height: 100px;
		}
		td {
			padding: 30px;
		}
	</style>
</body>
</html>