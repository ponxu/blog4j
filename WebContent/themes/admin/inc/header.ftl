<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>编辑 - Blog4j</title>
<link rel="stylesheet" type="text/css" href="${adminPath}/style/admin.css" />
</head>
<body>
	<div id="header-wrapper">
		<div id="header">
			<div id="admin-title" class="left">
				<span class="h1"><a href="${ctxPath}/">${blog.title!}</a> | </span>
				<span class="small">${blog.subtitle!}</span>
			</div>
			<div id="admin-func" class="right">
				<a href="${ctxPath}/admin/post-edit/0">写一写</a>
				
				<a href="${ctxPath}/admin/post-query?type=post">文章</a>
				<a href="${ctxPath}/admin/post-query?status=draft">草稿</a>
				<a href="${ctxPath}/admin/post-query?type=page">页面</a>
				
				<a href="${ctxPath}/admin/tag-edit">标签</a>
				<a href="${ctxPath}/admin/setting">设置</a>
				<a href="${ctxPath}/admin/logout">退出</a>
			</div>
			<div class="clear"></div>
		</div>
	</div>
	<div id="content-wrapper">