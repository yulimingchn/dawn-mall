var DD = DAWN = {
	checkLogin : function(){
		var _token = $.cookie("D_TOKEN");
		if(!_token){
			return ;
		}
		$.ajax({
			url : "http://sso.dawn.com/service/user/" + _token,
			dataType : "jsonp",
			type : "GET",
			success : function(_data){
				var html =_data.username+"，欢迎来到曙光！<a href=\"http://www.dawn.com/user/logout.html\" class=\"link-logout\">[退出]</a>";
				$("#loginbar").html(html);
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	DD.checkLogin();
});