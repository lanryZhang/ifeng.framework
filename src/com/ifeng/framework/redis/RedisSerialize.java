package com.ifeng.framework.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.ifeng.framework.util.LogUtil;


public class RedisSerialize {
	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				LogUtil.info("Unable to close " + closeable);
			}
		}
	}

	public static <T> byte[] serialize(T value) {
		if (value == null) {
			throw new NullPointerException("Can't serialize null");
		}
		byte[] rv = null;

		ByteArrayOutputStream bos = null;
		ObjectOutputStream os = null;
		try {
			bos = new ByteArrayOutputStream();
			
			os = new ObjectOutputStream(bos);
			os.writeObject(value);
			rv = bos.toByteArray();
		} catch (Exception e) {
			throw new IllegalArgumentException("Non-serializable object", e);
		} finally {
			close(os);
			close(bos);
		}
		return rv;
	}

	@SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] in) {

		ByteArrayInputStream bis = null;
		ObjectInputStream is = null;
		try {
			if (in != null) {
				bis = new ByteArrayInputStream(in);
				is = new ObjectInputStream(bis);
				T t = (T) is.readObject();
				return t;
			}
		} catch (Exception e) {
			LogUtil.error(e);
		} finally {
			close(is);
			close(bis);
		}
		return null;
	}
	
	
}
