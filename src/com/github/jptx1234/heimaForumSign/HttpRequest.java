package com.github.jptx1234.heimaForumSign;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class HttpRequest {

	String host = "";
	public String cookie = "";
	public HttpRequest(String host,String cookie){
		this.host = host;
		this.cookie = cookie;
	}
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @return URL 所代表远程资源的响应结果
     */

    public String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        String line;
        try {
            URL realUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("HOST", host);
            connection.setRequestProperty("Proxy-Connection", "keep-alive");
            connection.setRequestProperty("Referer", url);
            connection.setRequestProperty("user-agent",
            		"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
//            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();
            try {
            	List<String> cookiesList=map.get("Set-Cookie");
            	String newcookie="";
            	for (String cook : cookiesList) {
            		newcookie+=cook.split(";")[0]+"; ";
				}
            	if (newcookie.length() != 0) {
					cookie = newcookie;
				}
			} catch (Exception e) {
			}
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            	in = new BufferedReader(new InputStreamReader(
            			new GZIPInputStream(connection.getInputStream()),"UTF-8"));
            	while ((line = in.readLine()) != null) {
            		result += line;
            	}
			}
        } catch (IOException e) {
        	line = "与服务器连接出错:\r\n"+e.toString();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public String sendPost(String url, String param) {
        DataOutputStream out = null;
        BufferedReader in = null;
        HttpURLConnection connection = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            connection = (HttpURLConnection) realUrl.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("HOST", host);
            connection.setRequestProperty("Proxy-Connection", "keep-alive");
//            connection.setRequestProperty("Origin", "http://bbs.itheima.com");
            connection.setRequestProperty("Referer", url);
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            connection.setRequestProperty("user-agent",
            		"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
            out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(param);
            out.flush();
            Map<String, List<String>> map = connection.getHeaderFields();
            try {
            	List<String> cookiesList=map.get("Set-Cookie");
            	String newcookie="";
            	for (String cook : cookiesList) {
            		newcookie+=cook.split(";")[0]+"; ";
//            		System.out.println(cook);
            	}
            	if (newcookie.length() != 0) {
					cookie = encodeCookie(newcookie);
				}
            } catch (Exception e) {
            }
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            	in = new BufferedReader(new InputStreamReader(
            			new GZIPInputStream(connection.getInputStream()),"UTF-8"));
            	String line = null;
            	while ((line = in.readLine()) != null) {
            		result += line;
            	}
			}
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    private String encodeCookie(String newCookie){
    	HashMap<String, String> cookieMap = new HashMap<>();
    	putCookie(cookie, cookieMap);
    	putCookie(newCookie, cookieMap);
    	StringBuilder cookieSB = new StringBuilder();
    	cookieMap.forEach((K,V)->{
    		cookieSB.append(K);
    		cookieSB.append("=");
    		cookieSB.append(V);
    		cookieSB.append("; ");
    	});
    	return cookieSB.toString();
    }
    
    private void putCookie(String cookieString, HashMap<String, String> cookieMap){
    	String[] oldCookieStrings = cookie.split("; ");
    	for (String cookieKVString : oldCookieStrings) {
    		String[] cookieKV = cookieKVString.split("=", 2);
    		try {
    			cookieMap.put(cookieKV[0], cookieKV[1]);
    		} catch (Exception e) {
    		}
    	}
    	
    }
}