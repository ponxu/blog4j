<#assign title=blog.title>
<#include "inc/header.ftl">
<div class="postbox">
	<div class="postboxheader">
		<div class="postboxheader_title">
			<h2>
				<a href="${ctxPath}/post/${post.id}" title="阅读 ${post.title}">${post.title}</a>
			</h2>
			<p>
				发布于：${post.addtime?string('yyyy-MM-dd HH:mm')}
			</p>
		</div>
	</div>
	<div class="postbody">${post.contentHtml}</div>
	
	<#if blog.sharecode?exists && blog.sharecode!=''>
	<div class="sharelink">${blog.sharecode}</div>
	</#if>
	
	<div class="postfooter">
		<p>
			<strong>标签：</strong>
			<#list post.tags as tag>
			<a href="${ctxPath}/tag/${tag.id}" class="taga">${tag.name}</a><#if tag_has_next>,</#if>
			</#list>
			<#if !post.tags?has_content>无</#if>
		</p>
	</div>
</div>

<#if relativePosts?has_content>
<div class="relative-post postbox">
	<b>您可能也喜欢：</b>
	<ul>
		<#list relativePosts as rpost>
		<li><a href="${ctxPath}/post/${rpost.id}" title="阅读 ${rpost.title}">${rpost.title}</a></li>
		</#list>
	</ul>
</div>
</#if>

${blog.commentcode!}

<#include "inc/footer.ftl">