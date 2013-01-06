<#include "inc/header.ftl">

<h1>文章编辑</h1>

<form action="${ctxPath}/admin/post-edit/${post.id}" method="post" id="editform">
	<input type="hidden" name="id" id="id" value="${post.id}">
	<table width="100%"><tr>
		<td id="post-edit" valign="top">
		
			<label for="title">标题</label><br>
			<input type="text" name="title" id="title" class="shadowfocus" placeholder="请输入标题" value="${post.title!}">
			<div style="height: 20px;"></div>
			
			<label for="wmd-input">内容</label>
			<div id="wmd-editor" class="wmd-panel rbox">
				<div id="wmd-button-bar"></div>
				<textarea id="wmd-input" name="content" placeholder="在这里尽情书写吧...">${post.content!}</textarea>
			</div>
			<div id="wmd-preview" class="wmd-panel"></div>
			<div id="wmd-output" class="wmd-panel"></div>
			
			<div id="btns">
				
				<input type="button" class="btn shadowhover left" value=" 取 消 " onclick="cancel()">
				
				<input type="button" class="btn2 shadowhover2" value=" 保 存 " onclick="save()">
			</div>
			
		</td>
		<td id="post-side" valign="top">
		
			<label>设置标签</label>
			<ul id="tag-list">
			<#list tags as tag>
				<li><input type="checkbox" name="tagid" value="${tag.id}" <#list post.tags as ptag><#if tag.id=ptag.id>checked</#if></#list>>${tag.name}</li>
			</#list>
			</ul>
			<input type="text" id="newtagname" class="shadowfocus" placeholder="新标签" onkeyup="addNewTag()">
			
			<label>置顶</label>
			<select name="top" class="shadowfocus">
				<option value="0" <#if post.top==0>selected</#if>>否</option>
				<option value="1" <#if post.top==1>selected</#if>>是</option>
			</select>
			
			<label>类型</label>
			<select name="type" class="shadowfocus">
				<option value="post" <#if post.type=='post'>selected</#if>>文章</option>
				<option value="page" <#if post.type=='page'>selected</#if>>页面</option>
			</select>
			
			<label>状态</label>
			<select name="status" class="shadowfocus">
				<option value="publish" <#if post.status=='publish'>selected</#if>>公开</option>
				<option value="draft" <#if post.status=='draft'>selected</#if>>草稿</option>
				<option value="private" <#if post.status=='private'>selected</#if>>私有</option>
				<option value="password" <#if post.status=='password'>selected</#if>>加密</option>
			</select>
			
			<label>链接</label>
			<textarea name="url" class="shadowfocus" placeholder="">${post.url}</textarea>
			
		</td>
	</tr></table>
	
</form>

<link rel="stylesheet" type="text/css" href="${adminPath}/js/markdown/wmd.css" />

<script type="text/javascript" src="http://lib.sinaapp.com/js/jquery/1.7.2/jquery.min.js"></script>
<script type="text/javascript" src="${adminPath}/js/markdown/wmd.js"></script>
<script type="text/javascript" src="${adminPath}/js/markdown/showdown.js"></script>
<script type="text/javascript" src="${adminPath}/js/wmd-pack.js"></script>
<script type="text/javascript" src="${adminPath}/js/jquery.upload.js"></script>
<script type="text/javascript">
convertHTMLWhenSubmit = false;
WEB_ROOT = "${ctxPath}";

$(function() {
	var li = $("#tag-list li");
	setCheckBoxClickEvent(li);
	initCheckBox(li);
});

// li的点击事件和内部checkbox关联
function setCheckBoxClickEvent(li) {
	li.click(function(){
		var thiz = $(this);
		var cbx = thiz.find("input[type=checkbox]");
		if (cbx.attr("checked")) {
			cbx.attr("checked", false);
			thiz.removeClass("checked");
		} else {
			cbx.attr("checked", true);
			thiz.addClass("checked");
		}
	});
}

// li与内部checkbox选中状态同步
function initCheckBox(li) {
	li.each(function(i, n) {
		var thiz = $(this);
		var cbx = thiz.find("input[type=checkbox]");
		if (cbx.attr("checked")) {
			thiz.addClass("checked");
		}
	});
}

// 回车添加新标签
function addNewTag() {
	if (event.keyCode != 13) return;
	
	var name = $("#newtagname").val();
	if (name == "") {
		$("#newtagname").focus();
		return;
	}
	
	$.ajax({
		url: "${ctxPath}/admin/tag-edit",
		data: { "name": name },
		type: "POST",
		success: function(tagid) {
			var li = $("<li>").html('<input type="checkbox" name="tagid" value="' + tagid + '">' + name);
			$("#tag-list").append(li);
			setCheckBoxClickEvent(li);
			li.click();
			$("#newtagname").val("");
		}
	});
}

function cancel() {
	location = "${ctxPath}/admin/post-query?type=post"
}

// 提交表单
function save() {
	var f = $("#editform");
	
	var url = $("textarea[name=url]").val();
	if (url.indexOf("/") > -1) {
		$("textarea[name=url]").val(url.substr(1));
	}
	
	$.ajax({
		url: f.attr("action"),
		data: f.serialize(),
		type: "POST",
		beforeSend: function() {
			if ($("#title").val() == "") {
				alert("请输入标题!");
				$("#title").focus();
				return false;
			}
			if ($("textarea[name=content]").val() == "") {
				alert("请输入内容!");
				$("textarea[name=content]").focus();
				return false;
			}
			
			if ($("select[name=type]").val() == "page" && url == "") {
				alert("页面必须指定一个url!");
				$("textarea[name=url]").focus();
				return false;
			}
			
			return true;
		},
		success: function(postid) {
			$("#id").val(postid);
			f.attr("action", "${ctxPath}/admin/post-edit/" + postid);
			alert("保存成功!");
		}
	});
}

</script>
<#include "inc/footer.ftl">