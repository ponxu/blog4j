<div class="sidebar">
	<div class="sidebarbox">
		<form method="get" id="searchform" action="${ctxPath}/search">
			<div>
				<label class="screen-reader-text" for="s">搜索：</label>
				<input type="text" name="s" id="s" />
				<input type="submit" id="searchsubmit" value="搜索" />
			</div>
		</form>
	</div>
	<#if blog.weibocode?exists && blog.weibocode!=''>
	<div class="sidebarbox">
		<div class="sidebarheader">
			<h2>博主微薄</h2>
		</div>
		<div class="textwidget">
			${blog.weibocode}
		</div>
	</div>
	</#if>
	
	<div class="sidebarbox">
		<div class="sidebarheader">
			<h2>所有标签</h2>
		</div>
		<ul>
			<#list tags as tag>
			<li><a href="${ctxPath}/tag/${tag.id}" title="查看 ${tag.name} 的所有文章">${tag.name}</a></li>
			</#list>
		</ul>
	</div>
	
	<div class="sidebarbox">
		<div class="sidebarheader">
			<h2>近期文章</h2>
		</div>
		<ul>
			<#list latestPosts as lpost>
			<li><a href="${ctxPath}/post/${lpost.id}" title="阅读 ${lpost.title}">${lpost.title}</a></li>
			</#list>
		</ul>
	</div>
</div>