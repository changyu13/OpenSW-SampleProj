package scripts;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class indexer {
	
	private String input_file;
	private String output_file;
	
	public indexer(String file) {
		this.input_file = file;
	}
	public void makePost() throws IOException, ParserConfigurationException, SAXException {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(this.input_file);
		
		Element root = document.getDocumentElement();
		NodeList codelist = root.getChildNodes();
		String[] s1;
		String sum="";
		int cnt =0;
		ArrayList <HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		HashMap n1 = new HashMap();
		HashMap n2 = new HashMap();
		HashMap n3 = new HashMap();
		HashMap n4 = new HashMap();
		HashMap n5 = new HashMap();
		list.add(n1);
		list.add(n2);
		list.add(n3);
		list.add(n4);
		list.add(n5);
		HashMap<String,Integer> doc_num = new HashMap<>();
		for(int i=0; i<5;i++) {
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
			bodyData = bodyData.trim();
			sum += bodyData;
			
		}
		s1 = sum.split("#");
		String[][] s2 = new String[s1.length][];
		for(String s : s1) {
			s2[cnt] = s.split(":"); 
			cnt++;
		}

		HashMap h = new HashMap();
		for(int j=0;j<s2.length;j++) {
			h.put(s2[j][0],s2[j][1]);
		}
		h.remove("  파스타 ");
		
		//System.out.println("h = "+h);
		for(int i=0; i<5;i++) {
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
			bodyData = bodyData.trim();
			bodyData = bodyData;
			int count =0; 
			
			String[] doc_s = bodyData.split("#");
			String[][] doc_s2 = new String[doc_s.length][];
			for(String s : doc_s) {
				doc_s2[count] = s.split(":"); 
				count++;
			}
			//doc_s2[0][0] = "  "+doc_s2[0][0];
			
			for(int j=0;j<doc_s.length;j++) {
				list.get(i).put(doc_s2[j][0],doc_s2[j][1]);
			}
		}
		System.out.println("리스트 사이즈 : "+list);
		Iterator <String> keys = h.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			int k=0;
			for(int m=0; m< 5; m++){
					if(list.get(m).containsKey(key))
					{
						k++;
						doc_num.put(key,k);
						//System.out.println(m+"번째 리스트 "+list.get(m));
						//System.out.println(key+" "+m+"번쨰"+" "+k+"개");
					}
				}
		}
	
		System.out.println("doc_num = "+doc_num);
		String[][] w = new String[h.size()][2];
		Iterator <String> w_keys = doc_num.keySet().iterator();
		for(int j=0; j<h.size();j++) {
			if(w_keys.hasNext()) {
				w[j][0] = w_keys.next();
				w[j][1] = "";
				for(int k=0; k<5; k++)
				{
					if(list.get(k).get(w[j][0])==null) {
						w[j][1] += Integer.toString(k) +" 0 ";
					}
					else {
						//System.out.println("w[j][0] :" +w[j][0]);
						//String str = w[j][0];
						w[j][1] += Integer.toString(k)+ " " +String.format("%.2f",(Double.parseDouble(list.get(k).get(w[j][0])) * Math.log(5.0/doc_num.get(w[j][0]))))+ " ";
					}
				}
			}
		}
		for(int j=0;j<h.size();j++) {
			w[j][0] = w[j][0].trim();
			w[j][0] = "  "+w[j][0]+" ";
		}
		//doc_s2[0][0] = "  "+doc_s2[0][0];
		/*for(int i : plus) {
			w[i-1][0] = "  "+w[i-1][0];
		}*/

		FileOutputStream fileStream = new FileOutputStream("./index.post");
		ObjectOutputStream objectOuputStream = new ObjectOutputStream(fileStream);
	
		
		HashMap MovieMap = new HashMap();
		for(int k=0;k<h.size();k++) {
			System.out.print(w[k][0] +" -> "+ w[k][1]);
			System.out.println();
			MovieMap.put(w[k][0],w[k][1]);		
		}
		objectOuputStream.writeObject(MovieMap);
		objectOuputStream.close();
		
	}
	private double log(double d) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
