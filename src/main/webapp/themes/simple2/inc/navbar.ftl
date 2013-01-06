<div class="pagenavigation">
	<#if (pageInfo.currentIndex>1)>
	<a href="${ctxPath}${pageInfo.preUrl}" class="left">上一页</a>
	</#if>
	
	<#assign forbegin=pageInfo.currentIndex-5>
	<#assign forend=pageInfo.currentIndex+5>
	
	<#list forbegin..forend as i>
		<#if (i>=1) && (i<=pageInfo.pageCount)>
		<a href="${ctxPath}${pageInfo.rightParamUrl}${i}" <#if pageInfo.currentIndex==i>class="current"</#if>>${i}</a>
		</#if>
	</#list>
	
	<#if (pageInfo.currentIndex<pageInfo.pageCount)>
	<a href="${ctxPath}${pageInfo.nextUrl}" class="right">下一页</a>
	</#if>
	
</div>