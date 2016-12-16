package com.github.jptx1234.heimaForumSign;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

	public static Timer timer = new Timer();
	
	
	
	public static void main(String[] args) {
		sign();
		setTime();
	}
	
	static void setTime(){
		Random random = new Random();
		ZonedDateTime zonedDateTime = ZonedDateTime.of(ZonedDateTime.now().plusDays(1).toLocalDate(),LocalTime.of(2+random.nextInt(4), random.nextInt(60), random.nextInt(60)),ZoneId.systemDefault());
		Date date = Date.from(zonedDateTime.toInstant());
		System.out.println("next sign time: "+date);
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				sign();
				setTime();
			}
		}, date);
	}
	
	
	static void sign(){
		System.out.println("Sign now :"+LocalDateTime.now().toString());
		File cookieFile = new File("cookie.txt");
		BufferedReader br = null;
		FileReader fr = null;
		String cookie = "";
		try {
			fr = new FileReader(cookieFile);
			br = new BufferedReader(fr);
			cookie = br.readLine();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		HttpRequest httpRequest = new HttpRequest("bbs.itheima.com", cookie);
		String post = "";
		while (post.length() == 0) {
			post = httpRequest.sendPost("http://bbs.itheima.com/plugin.php?id=dsu_paulsign:sign&operation=qiandao&infloat=1&sign_as=1&inajax=1", "formhash=de422598&qdxq=kx&qdmode=2&todaysay=&fastreply=0");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
		}
		System.out.println(post);
		cookie = httpRequest.cookie;
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(cookieFile);
			bw = new BufferedWriter(fw);
			bw.write(cookie);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
