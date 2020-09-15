package com.example.demo.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StreamManage extends Thread{
	private InputStream inputStream;
    private String type;
    private String result;
    boolean end = false;

    //StreamManage的构造参数是InputStream类实例 => 实际上是Process实例的返回流
    public StreamManage(InputStream inputStream,String type){
        this.inputStream = inputStream;
        this.type = type;
        this.end = false;
    }
    public void run(){
    	log.info("start thread to read cmd ");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream,Charset.forName("GBK"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null;
        StringBuffer resultBuffer = new StringBuffer();
        try{
            while((line = bufferedReader.readLine())!=null && !end){
                if (type.equals("Error")) {
                    log.error(line);
                }else{
                	log.debug(line);
                }
                resultBuffer.append(line);
    			resultBuffer.append("\r\n");
            }
            log.info("result:[{}]", resultBuffer.toString());
    		result = resultBuffer.toString();
        }catch (Throwable e){
            log.error("cmd fail.",e);
        }finally{
        	end = true;
        }
    }
	public String getResult() {
		return result;
	}
	public boolean isEnd() {
		return end;
	}
	public void setEnd(boolean end) {
		this.end = end;
	}	
}
