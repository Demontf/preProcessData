package com.tf.trec.processxml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MyXml {

	private String xmlPath;
	private final static String ABSTRACT_PAHT = "/article/front/article-meta/abstract/p";
	private final static String KEYWORDS_PAHT = "/article/front/article-meta/kwd-group/kwd";
	private final static String TITLE_PAHT = "/article/front/article-meta/title-group/article-title";
	private final static String ARTICLEID_PAHT = "/article/front/article-meta/article-id";
	private final static String CONTENT_PAHT = "/article/body";
	private String theAbstract="";
	private String articleId = "";
	private String thetitle="";
	private String theContent="";
	private List<String> keywords;
	
	public void  test(){
		Document doc = getDocument();
		Element rootElement = doc.getRootElement();
		String string = rootElement.asXML();
		removeTags(string);
	}
	
	public MyXml(String xmlPath){
		this.xmlPath = xmlPath;
		keywords = new LinkedList<String>();
	}
	
	public boolean readXml(){
		Document doc = getDocument();
		if(doc!=null){
			Element rootElement = doc.getRootElement();
			System.out.println("root Element: "+rootElement.getName());
			List<Element> abcList = doc.selectNodes(ABSTRACT_PAHT);
			if(abcList!=null){
				String source = abcList.get(0).getText();
				theAbstract = removeTags(source);
//				System.out.println("list num: "+abcList.size());
//				System.out.println("abstract: "+abcList.get(0).getName());
				System.out.println("abstract: "+abcList.get(0).getText());
			}
			
			List<Element> idList = doc.selectNodes(ARTICLEID_PAHT);
			if(idList!=null){
				for(Element element:idList){
					if(element.attributeValue("pub-id-type").equals("pmid")){
						articleId=element.getText();
					}
				}
				System.out.println("articleID :"+articleId);
			}
			
			List<Element> titleList = doc.selectNodes(TITLE_PAHT);
			if(titleList!=null){
				thetitle = titleList.get(0).getText();
				System.out.println("the title:"+thetitle);
			}
			
			List<Element> kwdList = doc.selectNodes(KEYWORDS_PAHT);
			if(kwdList!=null){
				for(Element e :kwdList){
					String kwd = e.getText();
					keywords.add(kwd);
				}
				for(String s:keywords){
					//System.out.println("keyword: "+s);
				}
			}
			
			List<Element> contlist = doc.selectNodes(CONTENT_PAHT);
			if(contlist!=null){
				//System.out.println("content source"+ contlist.get(0).asXML());
				String source = contlist.get(0).asXML();
				theContent = removeTags(source);
				//System.out.println("content complete"+ theContent);
			}
			return true;
		}else{
			return false;
		}
		
	}
	//防止加载DTD 获取doc
    public Document getDocument() 
    { 
    	SAXReader saxReader = new SAXReader(false);
        NullEntityResolver resolver = new NullEntityResolver();
        saxReader.setEntityResolver(resolver);
        Document doc = null;
        try {
        	saxReader.setEncoding("GBK");
			doc = saxReader.read(xmlPath);
		} catch (DocumentException e) {
			e.printStackTrace();
			System.out.println("cant find the xml");
		} 
        return doc;
    }
    
    public String removeTags(String source){
    	String regXMLStart = "<[a-zA-Z]+[1-9]?[^><]*>";
    	String regXMLEnd = "</[a-zA-Z]+[1-9]?>";
    	String regXMLLR = "&[a-zA-Z]{1,10};";//&lt &gt
    	String regJH = "&#[0-9]{4};";//&#1234;
    	
    	List<String> regList = new LinkedList<String>();
    	regList.add(regXMLLR);
    	regList.add(regXMLStart);
    	regList.add(regXMLEnd);
    	regList.add(regJH);
    	String complete=source;
    	
    	for(String tmp:regList){
        	complete = complete.replaceAll(tmp, " ");
    	}
    	
    	return complete;
    }
    
   public void SaveToXml(String savePath){
	   Document article = DocumentHelper.createDocument();
	   Element root = article.addElement("article"); 
	   Element title = root.addElement("title");
	   title.setText(thetitle);
	   Element abc = root.addElement("abstract");
	   abc.addText(theAbstract);
	   Element keygroup = root.addElement("kwd-group");
	   for(int i=0;i<keywords.size();i++){
		  Element kwd = keygroup.addElement("kwd");
		  kwd.addText(keywords.get(i)); 
	   }
	   Element content = root.addElement("content");
	   content.addText(theContent);
	   
	   XMLWriter xmlWriter = null;
	   OutputFormat format = OutputFormat.createPrettyPrint();
	   format.setEncoding("utf-8");
	   FileWriter out = null;
	   try {
		out = new FileWriter(new File(savePath+articleId+".xml"));
		xmlWriter = new XMLWriter(out,format);
		xmlWriter.write(article);
	} catch (UnsupportedEncodingException ex)  
	{  
	    System.out.println("xml encoding error: " + ex);  
	}  
	catch (IOException ex)  
	{  
	        System.out.println("xml writing error: " + ex);  
	}  
	finally  
	{  
	    if (xmlWriter != null)  
	    {  
	        try  
	        {  
	            xmlWriter.close();  
	        }  
	        catch (IOException ex)  
	        {  
	            System.out.println("error occured when closing XMLWriter: " + ex);  
	        }   
	    }  
	              
	    if (out != null)  
	    {  
	        try  
	        {  
	            out.close();  
	        }  
	        catch (IOException ex)  
	        {  
	            System.out.println("error occured when closing StringWriter: " + ex);            }   
	    }  
	}
   }
    
   public class NullEntityResolver implements EntityResolver {
        String emptyDtd = "";

        ByteArrayInputStream byteIs = new ByteArrayInputStream(emptyDtd.getBytes());

        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            return new InputSource(byteIs);
        }
    }
}