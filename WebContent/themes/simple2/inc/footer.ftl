		</div> <!-- end con -->
		<#include "side.ftl">
	</div> <!-- end mainbody -->
	<div class="footer">
		<p>
			© 2012 ${blog.title}
			| Powered By <a href="http://www.ponxu.com">Blog4j</a>
			| <a href="${ctxPath}/admin">Admin</a>
			<#if blog.analyticscode?? && blog.analyticscode!=''>
			| ${blog.analyticscode}
			</#if>
		</p>
	</div>
</body>
</html>