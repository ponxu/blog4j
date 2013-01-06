package com.ponxu.blog4j.dao;

import java.sql.Connection;
import java.util.List;

import com.ponxu.blog4j.Config;
import com.ponxu.run.db.DBUtils;
import com.ponxu.run.db.wrap.RowWrapper;
import com.ponxu.run.log.Log;
import com.ponxu.run.log.LogFactory;

/**
 * 数据操作
 * 
 * @author xwz
 */
public class DAO {
	private static final Log LOG = LogFactory.getLog();
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	static {
		DBUtils.loadDriver(DRIVER);
	}

	public static <T> List<T> queryPage(RowWrapper<T> wrapper, String sql,
			int page, int pageSize, Object... values) {
		if (page > -1 && pageSize > -1) {
			page = page == 0 ? 1 : page;
			int offset = (page - 1) * pageSize;
			sql += " limit " + offset + "," + pageSize;
		}
		LOG.debug(sql);
		return DBUtils.query(sConn(), wrapper, sql, values);
	}

	public static <T> List<T> queryAll(RowWrapper<T> wrapper, String sql,
			Object... values) {
		return queryPage(wrapper, sql, -1, -1, values);
	}

	public static <T> T queryUni(RowWrapper<T> wrapper, String sql,
			Object... values) {
		List<T> list = queryPage(wrapper, sql, 0, 1, values);
		return list == null || list.size() == 0 ? null : list.get(0);
	}

	public static int execute(String sql, Object... values) {
		LOG.debug(sql);
		return DBUtils.execute(mConn(), sql, values);
	}

	public static void begin() {
		DBUtils.beginTranscation(mConn());
	}

	public static void commit() {
		DBUtils.commit(mConn());
	}

	// ---------------------------------------------------------------
	private static final String murl = mysqlUrl(Config.dbHost, Config.dbPort,
			Config.dbName);
	private static final String surl = mysqlUrl(Config.dbHost2, Config.dbPort2,
			Config.dbName2);

	public static ThreadLocal<Connection> mConnThread = new ThreadLocal<Connection>();
	public static ThreadLocal<Connection> sConnThread = new ThreadLocal<Connection>();

	private static String mysqlUrl(String host, int port, String dbName) {
		return "jdbc:mysql://" + host + ":" + port + "/" + dbName;
	}

	/** 主数据库 */
	public static Connection mConn() {
		Connection conn = mConnThread.get();
		if (conn == null) {
			conn = DBUtils.getConnection(murl, Config.dbUser, //
					Config.dbPassword);
			mConnThread.set(conn);
		}
		return conn;
	}

	/** 从数据库 */
	public static Connection sConn() {
		Connection conn = sConnThread.get();
		if (conn == null) {
			conn = DBUtils.getConnection(surl, Config.dbUser2,
					Config.dbPassword2);
			sConnThread.set(conn);
		}
		return conn;
	}

	/** 清除连接 */
	public static void close() {
		Connection conn = mConnThread.get();
		if (conn != null) {
			DBUtils.close(conn);
			mConnThread.set(null);
		}

		conn = sConnThread.get();
		if (conn != null) {
			DBUtils.close(conn);
			sConnThread.set(null);
		}
	}
}
