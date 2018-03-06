package com.apec.framework.common.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * @author xx
 */
public class HttpRequestUtil {
	private static final String DEFAULT_ENCODING = "UTF-8";

	public static String get(String url, HashMap<String, Object> params, String encoding) {
		if (null == encoding || "".equals(encoding)) {
			encoding = DEFAULT_ENCODING;
		}
		// 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
		StringBuilder getURL = new StringBuilder(url);
		String result = "";
		try {
			if (null != params && params.size() > 0) {
				getURL.append("?");
				for (Entry<String, Object> param : params.entrySet()) {
					getURL.append(param.getKey()).append("=").append(param.getValue().toString()).append("&");
				}
				getURL.deleteCharAt(getURL.length() - 1);
			}
			URL getUrl = new URL(getURL.toString());
			// 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
			// 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
			HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);

			// 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
			// 服务器
			connection.connect();
			// 取得输入流，并使用Reader读取
			// 设置编码,否则中文乱码
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
			String lines;
			while ((lines = reader.readLine()) != null) {
				result += lines;
			}
			reader.close();
			// 断开连接
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String post(String url, HashMap<String, Object> params, String encoding) throws Exception{
		if (null == encoding || "".equals(encoding)) {
			encoding = DEFAULT_ENCODING;
		}
		String result = "";
		URL postUrl = new URL(url);
		// 打开连接
		HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();

		// 设置是否向connection输出，因为这个是post请求，参数要放在
		// http正文内，因此需要设为true
		connection.setDoOutput(true);
		// Read from the connection. Default is true.
		connection.setDoInput(true);
		// Set the post method. Default is GET
		connection.setRequestMethod("POST");
		// Post 请求不能使用缓存
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		connection.setConnectTimeout(60000);
		connection.setReadTimeout(60000);

		// 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
		// 要注意的是connection.getOutputStream会隐含的进行connect。
		connection.connect();

		if (null != params && params.size() > 0) {
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			StringBuilder content = new StringBuilder("");
			for (Entry<String, Object> param : params.entrySet()) {
				if (null == param.getValue()) {
					content.append(param.getKey()).append("=").append(URLEncoder.encode("", encoding)).append("&");
				} else {
					content.append(param.getKey()).append("=").append(URLEncoder.encode(param.getValue().toString(), encoding)).append("&");
				}
			}
			content.deleteCharAt(content.length() - 1);
			// DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
			out.writeBytes(content.toString());
			out.flush();
			try {
				out.close();
			} catch (IOException e) {
				throw new IOException();
			}
		}
		// 设置编码,否则中文乱码
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
		String line;
		while ((line = reader.readLine()) != null) {
			result += line;
		}
		try {
			reader.close();
		} catch (IOException e) {
			throw new IOException();
		}
		connection.disconnect();

		return result;
	}
}
