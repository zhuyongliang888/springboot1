package com.hoperun.micro.security.common;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class HttpsUtils {

	public final static String HTTPGET = "GET";
	public final static String HTTPPUT = "PUT";
	public final static String HTTPPOST = "POST";
	public final static String HTTPDELETE = "DELETE";
	public final static String HTTPACCEPT = "Accept";
	public final static String CONTENT_LENGTH = "Content-Length";
	public final static String CHARSET_UTF8 = "UTF-8";
	private static CloseableHttpClient httpClient;

	/**
	 * Authentication,the client needs: 1.Import your own certificate for server
	 * verification; 2.Import the CA certificate of the server, and use the CA
	 * certificate to verify the certificate sent by the server; 3.Set the domain
	 * name to not verify (Non-commercial IoT platform, no use domain name access.)
	 */
//	public void initSSLConfig() throws Exception {
//		// 1 Import your own certificate
//		String selfcertpath = Constant.SERTPATH + Constant.SELFCERTPATH;
//		String trustcapath = Constant.SERTPATH + Constant.TRUSTCAPATH;
//		System.out.println("cert path: " + selfcertpath);
//		KeyStore selfCert = KeyStore.getInstance("pkcs12");
//		selfCert.load(new FileInputStream(selfcertpath), Constant.SELFCERTPWD.toCharArray());
//		KeyManagerFactory kmf = KeyManagerFactory.getInstance("sunx509");
//		kmf.init(selfCert, Constant.SELFCERTPWD.toCharArray());
//
//		// 2 Import the CA certificate of the server,
//		KeyStore caCert = KeyStore.getInstance("jks");
//		caCert.load(new FileInputStream(trustcapath), Constant.TRUSTCAPWD.toCharArray());
//		TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509");
//		tmf.init(caCert);
//		SSLContext sc = SSLContext.getInstance("TLS");
//		sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
//
//		// 3 Set the domain name to not verify
//		// (Non-commercial IoT platform, no use domain name access generally.)
//		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sc, new DefaultHostnameVerifier());
//
//		httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
//	}

	/**
	 * init httpClient
	 */
	public void initConfig() {
		httpClient = HttpClients.custom().build();
	}

	/**
	 * post
	 * 
	 * @param url
	 * @param headerMap
	 * @param content
	 * @return
	 */
	public HttpResponse doPostJson(String url, Map<String, String> headerMap, String content) {
		HttpPost request = new HttpPost(url);
		addRequestHeader(request, headerMap);
		request.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));
		return executeHttpRequest(request);
	}

	/**
	 * post(File)
	 * 
	 * @param url
	 * @param headerMap
	 * @param file
	 * @return
	 */
	public StreamClosedHttpResponse doPostMultipartFile(String url, Map<String, String> headerMap, File file) {
		HttpPost request = new HttpPost(url);
		addRequestHeader(request, headerMap);
		FileBody fileBody = new FileBody(file);
		// Content-Type:multipart/form-data;
		// boundary=----WebKitFormBoundarypJTQXMOZ3dLEzJ4b
		HttpEntity reqEntity = (HttpEntity) MultipartEntityBuilder.create().addPart("file", fileBody).build();
		request.setEntity(reqEntity);
		return (StreamClosedHttpResponse) executeHttpRequest(request);
	}

	/**
	 * post
	 * 
	 * @param url
	 * @param headerMap
	 * @param content
	 * @return
	 */
	public StreamClosedHttpResponse doPostJsonGetStatusLine(String url, Map<String, String> headerMap, String content) {
		HttpPost request = new HttpPost(url);
		addRequestHeader(request, headerMap);
		request.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));
		HttpResponse response = executeHttpRequest(request);
		return (StreamClosedHttpResponse) response;
	}

	/**
	 * post
	 * 
	 * @param url
	 * @param content
	 * @return
	 */
	public StreamClosedHttpResponse doPostJsonGetStatusLine(String url, String content) {
		HttpPost request = new HttpPost(url);
		request.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));
		HttpResponse response = executeHttpRequest(request);
		return (StreamClosedHttpResponse) response;
	}

	/**
	 * post
	 * 
	 * @param url
	 * @param formParams
	 * @return
	 * @throws Exception
	 */
	public StreamClosedHttpResponse doPostFormUrlEncodedGetStatusLine(String url, Map<String, String> formParams)
			throws Exception {
		HttpPost request = new HttpPost(url);
		request.setEntity(new UrlEncodedFormEntity(paramsConverter(formParams)));
		HttpResponse response = executeHttpRequest(request);
		return (StreamClosedHttpResponse) response;
	}

	/**
	 * put
	 * 
	 * @param url
	 * @param headerMap
	 * @param content
	 * @return
	 */
	public HttpResponse doPutJson(String url, Map<String, String> headerMap, String content) {
		HttpPut request = new HttpPut(url);
		addRequestHeader(request, headerMap);
		request.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));
		return executeHttpRequest(request);
	}

	/**
	 * put
	 * 
	 * @param url
	 * @param headerMap
	 * @return
	 */
	public HttpResponse doPut(String url, Map<String, String> headerMap) {
		HttpPut request = new HttpPut(url);
		addRequestHeader(request, headerMap);
		return executeHttpRequest(request);
	}

	/**
	 * put
	 * 
	 * @param url
	 * @param headerMap
	 * @param content
	 * @return
	 */
	public StreamClosedHttpResponse doPutJsonGetStatusLine(String url, Map<String, String> headerMap, String content) {
		HttpResponse response = doPutJson(url, headerMap, content);
		return (StreamClosedHttpResponse) response;
	}

	/**
	 * put
	 * 
	 * @param url
	 * @param headerMap
	 * @return
	 */
	public StreamClosedHttpResponse doPutGetStatusLine(String url, Map<String, String> headerMap) {
		HttpResponse response = doPut(url, headerMap);
		return (StreamClosedHttpResponse) response;
	}

	/**
	 * get
	 * 
	 * @param url
	 * @param queryParams
	 * @param headerMap
	 * @return
	 * @throws Exception
	 */
	public HttpResponse doGetWithParas(String url, Map<String, String> queryParams, Map<String, String> headerMap)
			throws Exception {
		HttpGet request = new HttpGet();
		addRequestHeader(request, headerMap);
		URIBuilder builder;
		try {
			builder = new URIBuilder(url);
		} catch (URISyntaxException e) {
			throw new Exception(e);
		}
		if (queryParams != null && !queryParams.isEmpty()) {
			builder.setParameters(paramsConverter(queryParams));
		}
		request.setURI(builder.build());
		return executeHttpRequest(request);
	}

	/**
	 * get
	 * 
	 * @param url
	 * @param queryParams
	 * @param headerMap
	 * @return
	 * @throws Exception
	 */
	public StreamClosedHttpResponse doGetWithParasGetStatusLine(String url, Map<String, String> queryParams,
			Map<String, String> headerMap) throws Exception {
		HttpResponse response = doGetWithParas(url, queryParams, headerMap);
		return (StreamClosedHttpResponse) response;
	}

	/**
	 * delete
	 * 
	 * @param url
	 * @param headerMap
	 * @return
	 */
	public HttpResponse doDelete(String url, Map<String, String> headerMap) {
		HttpDelete request = new HttpDelete(url);
		addRequestHeader(request, headerMap);
		return executeHttpRequest(request);
	}

	/**
	 * delete
	 * 
	 * @param url
	 * @param headerMap
	 * @return
	 */
	public StreamClosedHttpResponse doDeleteGetStatusLine(String url, Map<String, String> headerMap) {
		HttpResponse response = doDelete(url, headerMap);
		return (StreamClosedHttpResponse) response;
	}

	/**
	 * list storage parameters
	 * 
	 * @param params
	 * @return
	 */
	private List<NameValuePair> paramsConverter(Map<String, String> params) {
		List<NameValuePair> nvps = new LinkedList<NameValuePair>();
		Set<Map.Entry<String, String>> paramsSet = params.entrySet();
		for (Map.Entry<String, String> paramEntry : paramsSet) {
			nvps.add(new BasicNameValuePair(paramEntry.getKey(), paramEntry.getValue()));
		}
		return nvps;
	}

	/**
	 * request add header
	 * 
	 * @param request
	 * @param headerMap
	 */
	private static void addRequestHeader(HttpUriRequest request, Map<String, String> headerMap) {
		if (headerMap == null) {
			return;
		}
		for (String headerName : headerMap.keySet()) {
			if (CONTENT_LENGTH.equalsIgnoreCase(headerName)) {
				continue;
			}
			String headerValue = headerMap.get(headerName);
			request.addHeader(headerName, headerValue);
		}
	}

	/**
	 * send http
	 * 
	 * @param request
	 * @return
	 */
	private HttpResponse executeHttpRequest(HttpUriRequest request) {
		HttpResponse response = null;
		try {
			response = httpClient.execute(request);
		} catch (Exception e) {
			System.out.println("executeHttpRequest failed.");
			System.out.println(e);
		} finally {
			try {
				response = new StreamClosedHttpResponse(response);
			} catch (IOException e) {
				System.out.println("IOException: " + e.getMessage());
			}
		}
		return response;
	}

	/**
	 * get http resopnse
	 * 
	 * @param response
	 * @return
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	public String getHttpResponseBody(HttpResponse response) throws UnsupportedOperationException, IOException {
		if (response == null) {
			return null;
		}
		String body = null;
		if (response instanceof StreamClosedHttpResponse) {
			body = ((StreamClosedHttpResponse) response).getContent();
		} else {
			HttpEntity entity = response.getEntity();
			if (entity != null && entity.isStreaming()) {
				String encoding = entity.getContentEncoding() != null ? entity.getContentEncoding().getValue() : null;
				body = StreamUtils.inputStream2String(entity.getContent(), encoding);
			}
		}
		return body;
	}
}
