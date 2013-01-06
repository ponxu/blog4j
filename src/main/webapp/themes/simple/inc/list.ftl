<#list posts as post>
<div class="post">
	<h1><a href="${ctxPath}/post/${post.id}"><#if post.top=1>[置顶]</#if>${post.title}</a></h1>
	<div class="post-info">
		发布于:
		${post.addtime?string('yyyy-MM-dd HH:mm')}
		&nbsp;&nbsp;&nbsp;&nbsp;
		<#if post.tags?has_content>
		标签:
		<#list post.tags as tag>
			<a href="${ctxPath}/tag/${tag.id}" class="taga">${tag.name}</a>&nbsp;
		</#list>
		</#if>
	</div>
	<div class="post-content">${post.contentHtml}</div>
	<div class="post-readon"><a href="${ctxPath}/post/${post.id}">继续阅读...</a></div>
</div>
</#list>

<div class="navbar">
	<#if (pageInfo.currentIndex>1)>
	<a href="${ctxPath}${pageInfo.preUrl}" class="left"><-更新</a>
	</#if>
	
	${pageInfo.currentIndex} of ${pageInfo.pageCount}页 / 共${pageInfo.totalCount}篇
	
	<#if (pageInfo.currentIndex<pageInfo.pageCount)>
	<a href="${ctxPath}${pageInfo.nextUrl}" class="right">更旧-></a>
	</#if>
	
	<div class="clear"></div>
</div>