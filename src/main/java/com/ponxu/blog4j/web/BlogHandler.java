package com.ponxu.blog4j.web;

import com.ponxu.blog4j.Config;
import com.ponxu.blog4j.cache.Cache;
import com.ponxu.blog4j.model.PageInfo;
import com.ponxu.blog4j.service.IPostService;
import com.ponxu.blog4j.service.ISettingService;
import com.ponxu.blog4j.service.ITagService;
import com.ponxu.run.lang.BeanUtils;
import com.ponxu.run.lang.StringUtils;
import com.ponxu.run.web.RequestHandler;
import com.ponxu.run.web.render.AbstractRender;
import com.ponxu.run.web.render.FreeMarkerRender;
import com.ponxu.run.web.render.JSPRender;
import com.sina.sae.util.SaeUserInfo;

/**
 * 具有分页/缓存能力
 * 
 * @author xwz
 */
public abstract class BlogHandler extends RequestHandler {
	protected IPostService postService;
	protected ITagService tagService;
	protected ISettingService settingService;

	protected PageInfo pageInfo;

	/** 是否需要分页 */
	protected boolean needPage() {
		return false;
	}

	/** 缓存键, null不缓存 */
	protected String cacheKey() {
		return null;
	}

	@Override
	public boolean beforeHandle() {
		// 安全验证
		if (!oauth()) {
			redirect("/admin/login");
			return false; // 不用处理了
		}

		// 先设置分页(缓存可能需要)
		if (needPage()) {
			int page = getIntParam("page", 1);
			pageInfo = new PageInfo(page, Config.pageSize);
		}

		// 读取缓存
		Object cache = Cache.get(cacheKey());
		if (cache != null) {
			response().addHeader("IS_CACHE", "true");
			renderString(cache.toString());
			return false; // 不用继续处理了
		}

		return true;
	}

	@Override
	public void afterHandle() {
		// DAO.close();
	}

	@Override
	public void beforeRender(AbstractRender render) {
		// JSP/模板才把常用信息放入
		if (BeanUtils.isNot(render.getClass(), JSPRender.class,
				FreeMarkerRender.class))
			return;

		// 路径信息
		String path = getContextPath();
		context.getRoot().put("ctxPath", path);
		context.getRoot().put("themePath", path + "/themes/" + Config.theme);
		context.getRoot().put("adminPath", path + "/themes/admin");

		// 设置信息
		context.getRoot().put("blog", settingService.asObject());

		// 标签信息
		context.getRoot().put("tags", tagService.queryAll());

		// 分页信息
		if (needPage()) {
			context.getRoot().put("pageInfo", pageInfo);
		}

		// 所有页面
		context.getRoot().put(
				"pages",
				postService.queryPublishPageByKeywords(null,
						new PageInfo(1, 10)));

		// 最新文章
		context.getRoot().put("latestPosts",
				postService.queryPublishLatestPost(10));

		// 操作方法
		// if (BeanUtils.is(render.getClass(), FreeMarkerRender.class)) {
		// try {
		// IPostService posts = (IPostService) BeanFactory.get("postService");
		// // context.getRoot().put("thread", useClass("java.lang.Thread"));
		// // context.getRoot().put("system", useClass("java.lang.System"));
		// context.getRoot().put("postService", useObjectModel(posts));
		// } catch (TemplateModelException e) {
		// e.printStackTrace();
		// }
		// }

		// 处理时长
		context.getRoot().put("howLong", context.howLong());
	}

	@Override
	public void afterRender(Object renderResult) {
		// 缓存
		if (renderResult != null) {
			Cache.put(cacheKey(), renderResult.toString());
		}
	}

	// // 拿到静态Class的Model
	// public TemplateModel useClass(String className)
	// throws TemplateModelException {
	// BeansWrapper wrapper = (BeansWrapper) freeMarkerConfig()
	// .getObjectWrapper();
	// TemplateHashModel staticModels = wrapper.getStaticModels();
	// return staticModels.get(className);
	// }
	//
	// // 拿到目标对象的model
	// public TemplateModel useObjectModel(Object target)
	// throws TemplateModelException {
	// ObjectWrapper wrapper = freeMarkerConfig().getObjectWrapper();
	// TemplateModel model = wrapper.wrap(target);
	// return model;
	// }
	//
	// // 拿到目标对象某个方法的Model
	// public TemplateModel useObjectMethod(Object target, String methodName)
	// throws TemplateModelException {
	// TemplateHashModel model = (TemplateHashModel) useObjectModel(target);
	// return model.get(methodName);
	// }
	//
	// private Configuration freeMarkerConfig() {
	// return FreeMarkerRender.getInstance().getCfg();
	// }

	@Override
	protected String getContextPath() {
		String path = request().getScheme() + "://" + request().getServerName();
		if (request().getServerPort() != 80) {
			path += ":" + request().getServerPort();
		}

		String ctx = "";
		if (!Config.isSAE) {
			ctx = context.getRequest().getContextPath();
		}

		return path + ctx;
	}

	@Override
	protected String cookiePath() {
		if (Config.isSAE)
			return "/" + SaeUserInfo.getAppName();
		else
			return super.cookiePath();
	}

	// ------------------------------------------------------------
	/** 安全验证 true: 验证通过 */
	protected boolean oauth() {
		return true;
	}

	protected boolean checkLoginFromCookie() {
		// 客户端存储md5的用户名和密码
		String loginname = getCookie("loginname");
		String loginpassword = getCookie("loginpassword");
		return checkLogin(loginname, loginpassword);
	}

	protected boolean checkLogin(String md5loginname, String md5password) {
		String loginname = StringUtils.md5(settingService.get("loginname"));
		String loginpassword = settingService.get("loginpassword");

		return StringUtils.equalsIgnoreCase(loginname, md5loginname)
				&& StringUtils.equalsIgnoreCase(loginpassword, md5password);
	}

	// ------------------------------------------------------------
	public void setPostService(IPostService postService) {
		this.postService = postService;
	}

	public void setTagService(ITagService tagService) {
		this.tagService = tagService;
	}

	public void setSettingService(ISettingService settingService) {
		this.settingService = settingService;
	}

}