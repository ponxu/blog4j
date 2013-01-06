package com.ponxu.blog4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.ponxu.run.lang.BeanUtils;
import com.ponxu.run.lang.FileUtils;
import com.ponxu.run.lang.StringUtils;
import com.sina.sae.util.SaeUserInfo;

/**
 * 全局配置
 * 
 * @author xwz
 */
public class Config {
	private static final String DEFAULT_PROP_FILE = "blog4j.properties";

	/** 是否 SAE */
	public static boolean isSAE = false;
	/** 是否 Cloud Foundry */
	public static boolean isCloudFoundry = false;
	/** 是否 BAE */
	public static boolean isBAE = false;

	public static String theme = "simple";
	public static int pageSize = 15;
	public static boolean cache = false;
	public static int sublength = 500;
	// 主数据库
	public static String dbUser;
	public static String dbPassword;
	public static String dbName;
	public static String dbHost;
	public static int dbPort;

	// 从数据库
	public static String dbUser2;
	public static String dbPassword2;
	public static String dbName2;
	public static String dbHost2;
	public static int dbPort2;

	// CloudFoundry mongodb
	public static String mgUser;
	public static String mgPassword;
	public static String mgName;
	public static String mgDb;
	public static String mgHost;
	public static int mgPort;

	static {
		// 环境检测
		if (checkSAE()) {
			isSAE = true;
		} else if (checkCloudFoundry()) {
			isCloudFoundry = true;
		}

		// 加载配置
		loadCommon();
		if (isSAE) {
			loadSAE();
		} else if (isCloudFoundry) {
			loadCloudFoundry();
		}
	}

	/** 判断是否SAE */
	private static boolean checkSAE() {
		try {
			Class.forName("com.sina.sae.util.SaeUserInfo");
			// "appname": mean not sae or local
			return !"appname".equals(SaeUserInfo.getAppName());
		} catch (ClassNotFoundException e) {
			// I do not think it's SAE
		}
		return false;
	}

	/** 判断是否CloudFoundry */
	private static boolean checkCloudFoundry() {
		return StringUtils.isNotEmpty(System.getenv("VCAP_SERVICES"));
	}

	/** 通用配置 */
	private static void loadCommon() {
		try {
			Properties prop = new Properties();
			InputStream in = FileUtils.fromClassPath(DEFAULT_PROP_FILE);
			prop.load(in);

			// 基础设置
			theme = prop.getProperty("theme", "simple");
			pageSize = BeanUtils.cast(prop.getProperty("page_size", "15"),
					int.class);
			cache = BeanUtils.cast(prop.getProperty("cache", "false"),
					Boolean.class);
			sublength = BeanUtils.cast(prop.getProperty("sublength", "500"),
					int.class);

			// 本地JDBC配置
			dbUser = dbUser2 = prop.getProperty("db_user");
			dbPassword = dbPassword2 = prop.getProperty("db_password");
			dbName = dbName2 = prop.getProperty("db_name");
			dbHost = dbHost2 = prop.getProperty("db_host", "127.0.1");
			dbPort = dbPort2 = BeanUtils.cast(
					prop.getProperty("db_port", "3307"), int.class);

			in.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	/** CloudFoundry 特殊配置 */
	private static void loadCloudFoundry() {
		try {
			String jsonStr = System.getenv("VCAP_SERVICES");
			JSONObject json = new JSONObject(jsonStr);

			// mysql
			JSONObject mysqlCfg = json.getJSONArray("mysql-5.1")
					.getJSONObject(0).getJSONObject("credentials");
			dbUser = dbUser2 = mysqlCfg.getString("username");
			dbPassword = dbPassword2 = mysqlCfg.getString("password");
			dbName = dbName2 = mysqlCfg.getString("name");
			dbHost = dbHost2 = mysqlCfg.getString("host");
			dbPort = dbPort2 = mysqlCfg.getInt("port");

			// mongodb用于文件存储
			JSONObject mgCfg = json.getJSONArray("mongodb-2.0")
					.getJSONObject(0).getJSONObject("credentials");
			mgUser = mgCfg.getString("username");
			mgPassword = mgCfg.getString("password");
			mgName = mgCfg.getString("name");
			mgDb = mgCfg.getString("db");
			mgHost = mgCfg.getString("host");
			mgPort = mgCfg.getInt("port");

		} catch (JSONException e) {
			System.err.println(e.getMessage());
		}
	}

	/** sae特殊配置 */
	private static void loadSAE() {
		dbUser = dbUser2 = SaeUserInfo.getAccessKey();
		dbPassword = dbPassword2 = SaeUserInfo.getSecretKey();
		dbName = dbName2 = "app_" + SaeUserInfo.getAppName();
		dbPort = dbPort2 = 3307;
		dbHost = "w.rdc.sae.sina.com.cn";
		dbHost2 = "r.rdc.sae.sina.com.cn";
	}

	/** 检查是否BAE环境, 若是加载特殊设置 (NM, 百度这个配置居然放在Header里面????!! 奇葩啊!!!!) */
	public static void checkAndLoadBAE(HttpServletRequest request) {
		if (!isBAE) {
			String host = request.getHeader("BAE_ENV_ADDR_SQL_IP");
			String port = request.getHeader("BAE_ENV_ADDR_SQL_PORT");
			String username = request.getHeader("BAE_ENV_AK");
			String password = request.getHeader("BAE_ENV_SK");

			if (StringUtils.isEmpty(host) || StringUtils.isEmpty(port)
					|| StringUtils.isEmpty(username)
					|| StringUtils.isEmpty(password))
				return;

			synchronized (Config.class) {
				if (!isBAE) {
					dbUser = dbUser2 = username;
					dbPassword = dbPassword2 = password;
					// dbName = dbName2 = ""; // 在blog4j.proerpties中配置
					dbHost = dbHost2 = host;
					dbPort = dbPort2 = BeanUtils.cast(port, int.class);
					isBAE = true;
				}
			}
		}
	}

	public static String show() {
		return "Config [isSAE=" + isSAE + ", isCloudFoundry=" + isCloudFoundry
				+ ", theme=" + theme + ", pageSize=" + pageSize + ", cache="
				+ cache + ", sublength=" + sublength + ", dbUser=" + dbUser
				+ ", dbPassword=" + dbPassword + ", dbName=" + dbName
				+ ", dbHost=" + dbHost + ", dbPort=" + dbPort + ", dbUser2="
				+ dbUser2 + ", dbPassword2=" + dbPassword2 + ", dbName2="
				+ dbName2 + ", dbHost2=" + dbHost2 + ", dbPort2=" + dbPort2
				+ "]";
	}
}