package com.ote.engine.tools;

import java.util.Date;

public class Chrono {
	private static Date start;
	private static Date end;

	public static void start(){
		start=new Date();
	}
	
	public static void stop(){
		end=new Date();
	}
	
	public static void print(){
		long time=(end.getTime()-start.getTime())/1000;
		String seconds = Integer.toString((int)(time % 60));  
		String minutes = Integer.toString((int)((time % 3600) / 60));  
		String hours = Integer.toString((int)(time / 3600));

		System.out.println("FIN:"+hours+"h:"+minutes+"m:"+seconds+"s");
	}
}
