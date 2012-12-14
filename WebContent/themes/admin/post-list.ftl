<#include "inc/header.ftl">

<h1>${listTitle!}</h1>

<table width="100%" class="listt">
	<thead>
		<tr>
			<td width="50">ID</td>
			<td>标题</td>
			<td width="150">时间</td>
			<td width="80">状态</td>
			<td width="50"></td>
		</tr>
	</thead>
	<tbody>
	<#list posts as post>
	<tr>
		<td>${post.id}</td>
		<td>
			<a href="${ctxPath}/admin/post-edit/${post.id}">
				<#if post.top=1>[置顶]</#if>${post.title}
			</a>
		</td>
		<td>${post.addtime?string("yyyy-MM-dd HH:mm")}</td>
		<td>${post.status}</td>
		<td>
			<input type="button" class="btn2 shadowhover2" value="修改" onclick="edit(${post.id})">
			<input type="button" class="btn shadowhover" value="删除" onclick="del(${post.id})">
		</td>
	</tr>
	</#list>
	</tbody>
</table>

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

<script type="text/javascript" src="http://lib.sinaapp.com/js/jquery/1.7.2/jquery.min.js"></script>
<script type="text/javascript">
function edit(postid) {
	location = "${ctxPath}/admin/post-edit/" + postid;
}

function del(postid) {
	if (!confirm("确定删除吗?")) 
		return;
	
	$.ajax({
		url: "${ctxPath}/admin/post-del/" + postid,
		type: "GET",
		success: function(ret) {
			if (ret > 0) {
				location.reload();
			}
		}
	});
}
</script>
<#include "inc/footer.ftl">