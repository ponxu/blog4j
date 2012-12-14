<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${title!''}</title>
<link href="${themePath}/style.css" type="text/css" rel="stylesheet" />
</head>
<body>
	<div class="header">
		<div class="headernav">
			<ul>
				<li><a href="${ctxPath}/">首页</a></li>
				<#list pages as page>
				<li><a href="${ctxPath}/page/${page.url}">${page.title}</a></li>
				</#list>
			</ul>
		</div>
		
		<div class="headermain">
			<div class="headertitle">
				<h1>${blog.title}</h1>
				<p>${blog.subtitle}</p>
			</div>
			<div class="headershare">
				<ul>
					<li><a class="icon_rss" href="#">rss</a>
					</li>
					<li><a class="icon_mail" href="#">mail</a>
					</li>
					<li><a class="icon_weibo" href="http://weibo.com/xwz789"
						target="_blank">weibo</a>
					</li>
				</ul>
			</div>
		</div>
	</div>
	
	<div class="mainbody">
		<div class="con">