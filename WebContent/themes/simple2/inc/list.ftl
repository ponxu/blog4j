<#list posts as post>
<div class="postbox">
	<div class="postboxheader">
		<div class="postboxheader_title">
			<h2>
				<a href="${ctxPath}/post/${post.id}" title="阅读 ${post.title}"><#if post.top=1>[置顶]</#if>${post.title}</a>
			</h2>
			<p>
				发布于：${post.addtime?string('yyyy-MM-dd HH:mm')}
			</p>
		</div>
	</div>
	<div class="postbody">${post.contentHtml}</div>
	<div class="postlink">
		<a class="linkmore" href="${ctxPath}/post/${post.id}" title="${post.title}">+阅读全文</a>
	</div>
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
</#list>
<#include "navbar.ftl">