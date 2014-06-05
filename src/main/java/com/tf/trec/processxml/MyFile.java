package com.tf.trec.processxml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;


public class MyFile {

	public static String read(String filePath){
		File file = new File(filePath);
		StringBuffer fileString = new StringBuffer();
		InputStreamReader read;
		try {
			read = new InputStreamReader(new FileInputStream(file),"GBK");
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while((lineTxt = bufferedReader.readLine()) != null){
				fileString.append(lineTxt);
			}
			read.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileString.toString();
	}
	
	public static List<String> readStopWords(String filePath){
		File file = new File(filePath);
		List<String> stopWords = new LinkedList<String>();
		InputStreamReader read;
		try {
			read = new InputStreamReader(new FileInputStream(file),"GBK");
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while((lineTxt = bufferedReader.readLine()) != null){
				stopWords.add(lineTxt);
			}
			read.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stopWords;
	}

	
	public static boolean write(String desPath,String content){
		File desFile = new File(desPath);
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(desFile);
			fileWriter.write(content);
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public static String filterString(String source){
		String regLR = "&[a-zA-Z]{1,10};";
		String regJH = "&#[0-9]{4};";
		source = source.replaceAll(regLR, " ");
		source = source.replaceAll(regJH, " ");
		return source;
	}
	
	public static String removeStopWords(String sourcePath,String stopFile,String desPath){
		String sourceContent = MyFile.read(sourcePath);
		List<String> stopWords = MyFile.readStopWords(stopFile);
		
		for(String tmp:stopWords){
			System.out.println("-"+tmp);
			sourceContent=sourceContent.replaceAll("(?i)"+" "+tmp+" ", " ");
		}
		
		MyFile.write(desPath, sourceContent);
		return sourceContent;
	}
}
