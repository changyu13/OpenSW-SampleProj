package scripts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Midterm {
	private String input_file;
	private String output_file;
	private String query;
	public Midterm(String file,String q) {
		this.input_file = file;
		this.query =q;
	}
	
	public HashMap<String,Integer> split(String s) {
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(s, true);
		String out = "";
		HashMap<String,Integer> map = new HashMap<>();
		for(int i=0; i< kl.size(); i++) {
			Keyword kwrd = kl.get(i);
			map.put(kwrd.getString(),kwrd.getCnt());
		}
		
		return map;
	}
		
public void showSnippet() throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException {
		//수정됨
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	
	Document docu = docBuilder.newDocument();
	
	Element docs = docu.createElement("docs");
	docu.appendChild(docs);
	
	
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = factory.newDocumentBuilder();
	Document document = builder.parse(this.input_file);
	
	Element root = document.getDocumentElement();
	NodeList codelist = root.getChildNodes();
	
	ArrayList<String> list = new ArrayList();
	int [] count = new int[5];
	String [] name = new String[5]; 
	for(int i=0; i<5; i++) {
		
		Node item = codelist.item(i);
		Node titleNode=null, bodyNode=null;
		if(item.getNodeType() == Node.ELEMENT_NODE) { 
			titleNode = item.getFirstChild();
			bodyNode = item.getLastChild();
		} else {
			System.out.println("공백 입니다.");
		}
		
		String titleData = titleNode.getTextContent();
		String bodyData = bodyNode.getTextContent();
		list.add(bodyData);
		name[i]= titleData;

		
		
	}
	Scanner scan = new Scanner(System.in);
	HashMap<String,Integer> query_l = new HashMap();
	String user = query;
	query_l  = split(user);
	System.out.println(query_l);
	Set<String> set = query_l.keySet();
		for(int i=0; i<5; i++) {
			int count1=0;
			for(String key : set) {
				String s = list.get(i);
				boolean possible = true;
				while(possible==true) {
					String k="";
					for(int j=0; j<30; j++) {
						k += s.charAt(i);
					}
					if(k.contains(key)) {
						count1++;
					}
				}
			}
			count[i]=count1;
		}
		for(int i=0; i<count.length;i++)
		{
			for(int j =i+1;j<count.length;j++) {
				if(count[i] <=count[j]) {
					int temp = count[i];
					count[i] = count[j];
					count[j] = temp;
				}
			}
		}
		int judge =0;
		for(int i=0;i<5;i++) {
			if(count[i]>0)
			{
				judge++;
			}
		}
		for(int i=0; i<judge;i++) {
			
		}
		
		
	}
	
}

