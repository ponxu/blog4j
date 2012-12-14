<#include "inc/header.ftl">

<h1>标签列表</h1>

<table width="400" class="listt">
	<thead>
		<tr>
			<td width="50">ID</td>
			<td>名字</td>
			<td width="50">排序</td>
			<td width="50">文章</td>
			<td width="50"></td>
		</tr>
	</thead>
	<tbody>
	<#list tags as tag>
	<tr id="tr${tag.id}">
		<td>
			${tag.id}
			<input type="hidden" name="id" id="id${tag.id}" value="${tag.id}" >
		</td>
		<td>
			<input type="text" name="name" id="name${tag.id}" class="shadowfocus" value="${tag.name}" >
		</td>
		<td>
			<input type="text" name="sort" id="sort${tag.id}" class="shadowfocus" style="width: 30px;" value="${tag.sort}" >
		</td>
		<td>${tag.postCount}</td>
		<td>
			<input type="button" class="btn2 shadowhover2" value="修改" onclick="save(${tag.id})">
			<input type="button" class="btn shadowhover" value="删除" onclick="del(${tag.id})">
		</td>
	</tr>
	</#list>
	</tbody>
</table>

<script type="text/javascript" src="http://lib.sinaapp.com/js/jquery/1.7.2/jquery.min.js"></script>
<script type="text/javascript">
function save(tagid) {
	$.ajax({
		url: "${ctxPath}/admin/tag-edit",
		data: { id: $("#id" + tagid).val(), name: $("#name" + tagid).val(), sort: $("#sort" + tagid).val() },
		type: "POST",
		success: function(tagid) {
			if (tagid > 0 && confirm("保存成功! 刷新此页吗?")) {
				location = "${ctxPath}/admin/tag-edit";
			}
		}
	});
}

function del(tagid) {
	if (!confirm("确定删除吗?")) 
		return;
	
	$.ajax({
		url: "${ctxPath}/admin/tag-del/" + tagid,
		type: "GET",
		success: function(ret) {
			if (ret > 0) {
				$("#tr" + tagid).remove();
			}
		}
	});
}
</script>
<#include "inc/footer.ftl">