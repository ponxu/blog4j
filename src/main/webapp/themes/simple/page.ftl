<#assign title=page.title + " | " + blog.title>
<#include "inc/header.ftl">
<div class="post page">
	<h1>${page.title}</h1>
	
	<div class="post-content">${page.contentHtml}</div>
</div>
<#include "inc/footer.ftl">