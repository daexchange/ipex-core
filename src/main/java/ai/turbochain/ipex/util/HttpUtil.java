package ai.turbochain.ipex.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

	public static String sentJsonPost(String url, Map header, String json) throws Exception {
		HttpPost post = new HttpPost(url);

		if (header != null) {
			Iterator it = header.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				post.addHeader((String) entry.getKey(), entry.getValue().toString());
			}
		}
		post.addHeader("Content-Type", "application/json");
		post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpResponse response = httpclient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity(), "utf-8");
				return result;
			} else {
				throw new Exception(EntityUtils.toString(response.getEntity(), "utf-8"));
			}
		} finally {
			httpclient.close();
		}
	}
	
	public static String sentPost(String url, Map header, Map param) throws Exception {
		HttpPost post = new HttpPost(url);

		if (header != null) {
			Iterator it = header.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				post.addHeader((String) entry.getKey(), entry.getValue().toString());
			}
		}
		// 添加参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		Iterator it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();

			String paramName = (String) entry.getKey();
			Object value = (Object) entry.getValue();
			String vsValue = null;
			if (value == null) {
				vsValue = "";
			} else {
				vsValue = value.toString();
			}
			params.add(new BasicNameValuePair(paramName, vsValue));
		}

		post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpResponse response = httpclient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity(), "utf-8");
				return result;
			} else {
				throw new Exception(EntityUtils.toString(response.getEntity(), "utf-8"));
			}
		} finally {
			httpclient.close();
		}
	}

	@SuppressWarnings("finally")
	public static String sendGet(String url) throws Exception {
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			throw new Exception("调用" + url + "接口失败");
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			return result;
		}
	}
}
