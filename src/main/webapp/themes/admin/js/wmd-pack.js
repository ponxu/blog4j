WEB_ROOT = "";

function upload() {
	// 上传方法
	$.upload({
		// 上传地址
		url : WEB_ROOT + '/admin/upload',
		// 文件域名字
		fileName : 'filedata',
		// 其他表单数据
		params : {
			name : 'pxblog\r\npxlog\r\n'
		},
		// 上传完成后, 返回json, text
		dataType : 'text',
		// 上传之前回调,return true表示可继续上传
		onSend : function() {
			return true;
		},
		// 上传之后回调
		onComplate : function(data) {
			$(".wmd-prompt-dialog form input[type=text]").val(data);
		}
	});
}