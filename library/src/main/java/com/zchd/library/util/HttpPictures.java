package com.zchd.library.util;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @gaojing
 * 多图片上传类
 * */
public class HttpPictures {
	private static final int TIME_OUT = 25000; // 超时时间
	private static final String CHARSET = "UTF-8"; // 设置编码
	public static String postWithFiles(String url,
			Map<String, String> textParams, List<String> filePaths, List<String> keys) {
		// StringBuilder resultSb = null;
		String string = null;
		try {
			final String BOUNDARY = UUID.randomUUID().toString();
			final String PREFIX = "--";
			final String LINE_END = "\r\n";

			final String MULTIPART_FROM_DATA = "multipart/form-data";
			// final String CHARSET = "UTF-8";

			URL uri = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

			// 缓存大小
			// conn.setChunkedStreamingMode(1024 * 64);
			// 超时
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");

			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Charset", CHARSET);
			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
					+ ";boundary=" + BOUNDARY);

			// 拼接文本类型的参数
			StringBuilder textSb = new StringBuilder();
			if (textParams != null) {
				for (Map.Entry<String, String> entry : textParams.entrySet()) {
					String str=entry.getKey();
					textSb.append(PREFIX).append(BOUNDARY).append(LINE_END);
					textSb.append("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"" + LINE_END);
					textSb.append("Content-Type: text/plain; charset="
							+ CHARSET + LINE_END);
					textSb.append("Content-Transfer-Encoding: 8bit" + LINE_END);
					textSb.append(LINE_END);
					textSb.append(entry.getValue());
					textSb.append(LINE_END);
				}
				System.out.println(textSb.toString());
			}

			DataOutputStream outStream = new DataOutputStream(
					conn.getOutputStream());
			outStream.write(textSb.toString().getBytes());
			// 参数POST方式
			// outStream.write("userId=1&cityId=26".getBytes());

			// 发送文件数据
			boolean markey=true;
			if (filePaths != null) {
				for (int i = 0; i < filePaths.size(); i++) {
					String file = filePaths.get(i);
					
					File f = new File(file);
					StringBuilder fileSb = new StringBuilder();
					fileSb.append(PREFIX).append(BOUNDARY).append(LINE_END);			
					fileSb.append("Content-Disposition: form-data; name=\""+keys.get(i)+"\"; filename=\"" + f.getName() + "\""
								+ LINE_END);
					//设置编码
					fileSb.append("Content-Type: application/octet-stream; charset="
							+ CHARSET + LINE_END);
					fileSb.append(LINE_END);
					outStream.write(fileSb.toString().getBytes());

					InputStream is = new FileInputStream(f);
					byte[] buffer = new byte[4096];
					int len;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
					is.close();
					outStream.write(LINE_END.getBytes());
				}
			}

			// 请求结束标志
			outStream.write((PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes());
			outStream.flush();

			// 得到响应码
			int res = conn.getResponseCode();

			// br.close();
			if (res == 200) {
				string = IsToStr.convertStreamToString(conn.getInputStream());
			} else {
				string = "{\"flag\":\"0\",\"msg\":\"连接异常\"}";
			}
			outStream.close();
			conn.disconnect();
			return string;
			// return resultSb == null ? null : resultSb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return string == null ? null : string;
	}
}

