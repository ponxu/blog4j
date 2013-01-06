package com.ponxu.blog4j.dao;

import java.io.ByteArrayOutputStream;
import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.ponxu.blog4j.Config;
import com.ponxu.run.log.Log;
import com.ponxu.run.log.LogFactory;

/**
 * mongodb操作
 * 
 * @author xwz
 */
public class MongoDAO {
	private static final Log LOG = LogFactory.getLog();
	private static ThreadLocal<MongoClient> clientLocal = new ThreadLocal<MongoClient>();
	private static ThreadLocal<DB> dbLocal = new ThreadLocal<DB>();

	public static void saveFile(String fileName, byte[] data) {
		GridFS fs = new GridFS(db());
		GridFSInputFile in = fs.createFile(data);
		in.setId(fileName);
		in.save();
	}

	public static byte[] getFile(String fileName) {
		try {
			GridFS fs = new GridFS(db());
			GridFSDBFile out = fs.findOne(new BasicDBObject("_id", fileName));
			ByteArrayOutputStream temp = new ByteArrayOutputStream();
			out.writeTo(temp);
			return temp.toByteArray();
		} catch (Exception e) {
			// quiet
		}
		return null;
	}

	public static DB db() {
		DB d = dbLocal.get();
		if (d == null) {
			initThread();
			d = dbLocal.get();
		}
		return d;
	}

	public synchronized static void close() {
		if (clientLocal.get() != null) {
			clientLocal.get().close();
		}

		clientLocal.set(null);
		dbLocal.set(null);
	}

	private static void initThread() {
		try {
			MongoClient client = new MongoClient(Config.mgHost, Config.mgPort);
			DB db = client.getDB(Config.mgDb);
			if (db.authenticate(Config.mgUser, Config.mgPassword.toCharArray())) {
				clientLocal.set(client);
				dbLocal.set(db);
			}
		} catch (UnknownHostException e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
