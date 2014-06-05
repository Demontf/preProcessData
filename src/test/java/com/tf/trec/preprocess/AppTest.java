package com.tf.trec.preprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import com.tf.trec.processxml.MyFile;
import com.tf.trec.processxml.MyXml;
/**
 * Unit test for simple App.
 */
public class AppTest 
{
//	@Test
    public void testMyXml(){
    	String filePath = "H:\\test.nxml";
    	MyXml myxml = new MyXml(filePath);
    	if(myxml.readXml()){
    		myxml.SaveToXml("H:\\");
    	}
    	
	}
//	@Test 
	public void testRemoveTag() throws IOException{
		String source = "<p><bold>Technical primer for reader:</bold>This paper consists of"+
    "visualization of global datasets as well as the tropical marine"+
    "microbial &lt;bibliography article. &lt;/list-item&gt;described here.</p>"+
    "<list list-type='order'>"+
     "<list-item>"+
        "<p>If not already&gt; done, install Google Earth free"+
        "version from";
		File file = new File("H:\\1.txt");
		InputStreamReader read = new InputStreamReader(new FileInputStream(file),"GBK");
		BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        StringBuffer test = new StringBuffer();
        while((lineTxt = bufferedReader.readLine()) != null){
            test.append(lineTxt);
        }
		MyXml myxml = new MyXml("H:\\test.nxml");
		System.out.println("complete: "+myxml.removeTags(test.toString()));
//		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("h:\\Result.txt"), true));
		System.out.println(myxml.removeTags(test.toString()).substring(0,2000));
		new FileWriter(new File("h:\\Result.txt")).write(myxml.removeTags(test.toString()));
		
		read.close();
	}
	
	//@Test
	public void test1(){
		MyXml myxml = new MyXml("H:\\test.xml");
		myxml.test();
	}
	
//	@Test
	public void testMyFile(){
		String sourcePath="H:\\1.txt";
		String desPath="H:\\1res.txt";
		String content = MyFile.read(sourcePath);
		System.out.println("content :"+content);
		MyFile.write(desPath, content);
	}
//	@Test
	public void testfiler(){
		String ss="&lg;dasdas &ssssaa;&dasadaeee213214,* s &#2345;";
		String word="MN , mn ,Mn";
		System.out.println("---"+word.replaceAll("(?i)mn", "##"));
		System.out.println(MyFile.filterString(ss));
	}
	@Test
	public void testRemoveStopwords(){
		String sourcePath="H:\\19172194.xml";
		String desPath="H:\\19172194res.txt";
		String stopFile = "H:\\stopwords.txt";
		
		MyFile.removeStopWords(sourcePath, stopFile, desPath);
		
	}
}
