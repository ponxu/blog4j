<div id="side" class="right">
	<div class="widget">
		<b>标签</b>
		<ul>
			<#list tags as tag>
			<li><a href="${ctxPath}/tag/${tag.id}">${tag.name}</a></li>
			</#list>
		</ul>
	</div>
	
	<div class="widget">
		<b>最新文章</b>
		<ul>
			<#list latestPosts as lpost>
			<li><a href="${ctxPath}/post/${lpost.id}">${lpost.title}</a></li>
			</#list>
		</ul>
	</div>
</div>