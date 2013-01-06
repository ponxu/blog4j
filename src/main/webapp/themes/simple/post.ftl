<#assign title=post.title + " | " + blog.title>
<#include "inc/header.ftl">
<div class="post">
	<h1>${post.title}</h1>
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
</div>

<#if relativePosts?has_content>
<div class="relative-post">
	<b>您可能也喜欢：</b>
	<ul>
		<#list relativePosts as rpost>
		<li><a href="${ctxPath}/post/${rpost.id}">${rpost.title}</a></li>
		</#list>
	</ul>
</div>
</#if>


${blog.commentcode!}

<#include "inc/prettify.ftl">
<#include "inc/footer.ftl">