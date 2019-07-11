package com.tzj.collect.common.constant;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5 {
	public static final int INTERATIONS = 1024;

	public static final String key = "uv8t9hyj3f5gkrq4gz8vv9moatpsdt9w";

	public static final String UTF8 = "utf-8";

	/**
	 * MD5 加密
	 * 
	 * @param plainText
	 *            明文
	 * @return 消息摘要
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5(String plainText) {
		if (plainText == null || "".equals(plainText)) {
			return null;
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes("UTF-8"));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return plainText;
	}

	/**
	 * MD5 加密
	 * 
	 * @param plainText
	 *            明文
	 * @return 消息摘要
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5(String plainText, String input_charset) {
		if (plainText == null || "".equals(plainText)) {
			return null;
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes(input_charset));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

		}
		return plainText;
	}

	/**
	 * 进行一定次数的MD5加密
	 * 
	 * @param plainText
	 *            明文
	 * @return 经过一定次数MD5加密后的消息摘要
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5s(String plainText) {
		for (int i = 0; i < INTERATIONS; i++) {
			plainText = md5(plainText);
		}
		return plainText;
	}

	/**
	 * 签名字符串
	 * 
	 * @param text
	 *            需要签名的字符串
	 * @param key
	 *            密钥
	 * @param input_charset
	 *            编码格式
	 * @return 签名结果
	 */
	public static String sign(String text, String key, String input_charset) {
		text = text + key;
		return DigestUtils.md5Hex(getContentBytes(text, input_charset));
	}

	/**
	 * 签名字符串
	 * 
	 * @param text
	 *            需要签名的字符串
	 * @param sign
	 *            签名结果
	 * @param key
	 *            密钥
	 * @param input_charset
	 *            编码格式
	 * @return 签名结果
	 */
	public static boolean verify(String text, String sign, String key, String input_charset) {
		text = text + key;
		String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
		if (mysign.equals(sign)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param content
	 * @param charset
	 * @return
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	private static byte[] getContentBytes(String content, String charset) {
		if (charset == null || "".equals(charset)) {
			return content.getBytes();
		}
		try {
			return content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
		}
	}

	public static void main(String[] args) {
		Long aa=Long.parseLong("1490162232068");
		System.out.println(md5("M1477965191880NzU5OTQxjL682vd1rft5IfRzSS1BSg==fX0AyB1sh/xtRZYd3+461w==x9N7uwxSBRjkfugDn5qpZw==BHQBJ1T+UAfgdD9Mcy7wJQ==frj8n4qQx/1xKlkQ+9E1gw==310101021005000131010102100531010100000031010102100012弄2号bbco4RrfDft6xfJFIqx0Kw==1490162232068"));

		
	}

	public static String sign(TreeMap<String, Object> data, String key, String input_charset) {
		StringBuilder builder = new StringBuilder();
		Set<Map.Entry<String, Object>> set = data.entrySet();
		Iterator<Map.Entry<String, Object>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Object> entry = iterator.next();
			builder.append(entry.getKey());
			builder.append("=");
			builder.append(entry.getValue());
			builder.append("&");
		}
		builder.append("key=");
		return sign(builder.toString(), key, input_charset);

	}
}
