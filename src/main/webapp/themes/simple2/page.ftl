<#assign title=blog.title>
<#include "inc/header.ftl">

<div class="postbox">
	<div class="postboxheader">
		<div class="postboxheader_title">
			<h2 style="text-algin: center;">
				<a href="${ctxPath}/post/${page.id}" title="阅读 ${page.title}">${page.title}</a>
			</h2>
		</div>
	</div>
	<div class="postbody">${page.contentHtml}</div>
	
</div>
<#include "inc/footer.ftl">