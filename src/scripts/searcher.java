package scripts;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

public class searcher {

	private String input_file;
	private String output_file;
	
	public searcher(String file) {
		this.input_file = file;
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
		
public void CalcSim() throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException {
		//수정됨
		Scanner scan = new Scanner(System.in);
		FileInputStream fileStream = new FileInputStream(this.input_file);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		//System.out.println("읽어온 객체의 type ->" + object.getClass());
		
		HashMap<String,String> hashMap = (HashMap)object;
		Iterator<String> it = hashMap.keySet().iterator();
		
		/*while(it.hasNext()) {
			String key = it.next();
			String value = (String)hashMap.get(key);
			System.out.println(key + "->" + value);
		}*/
		
		HashMap<String,Integer> query_l = new HashMap();
		System.out.print("Input the query >>>");
		String user = scan.nextLine();
		query_l  = split(user);
		System.out.println(query_l);
		Set<String> set = query_l.keySet();
		ArrayList<String> list = new ArrayList();
		ArrayList<Double> w = new ArrayList();
		double[] id_n = new double[5];
		double[] a_n = new double[5];
		double[] b_n = new double[5];
		double[] sim = new double[5];
		Arrays.fill(id_n, 0);
		int q_n =0;
		int judge =0;
			for(String key: set) {
				list.add(key);
				String[] s;
					if(hashMap.containsKey("  "+key+" ")) {
						String str = hashMap.get("  "+key+" ");
						w.add(q_n, query_l.get(key)*(1.0));
					}
					else {
						w.add(q_n,0.0);
					}
					q_n++;
		}
		for(int i=0;i<5;i++) {
			//System.out.println(w[i]);
			for(int j=0;j<list.size();j++) {
				if(hashMap.containsKey("  "+list.get(j)+" ")){
					String s[] = hashMap.get("  "+list.get(j)+" ").split(" ");
					a_n[i] += w.get(j)*w.get(j);
					b_n[i] += Double.parseDouble(s[i*2+1])*Double.parseDouble(s[i*2+1]);
				}
				else {
					System.out.println(i+"번째 : "+list.get(j));
					b_n[i] =0;
					sim[i] =0;
				}
			}
		}
		//Q.id(n)구하기
				for(int i=0;i<5;i++) {
					//System.out.println(w[i]);
					for(int j=0;j<list.size();j++) {
						if(hashMap.containsKey("  "+list.get(j)+" ")){
							String s[] = hashMap.get("  "+list.get(j)+" ").split(" ");
							id_n[i] += w.get(j)*Double.parseDouble(s[i*2+1]);
							System.out.println("가중치 : "+s[i*2+1]);
						}
						else {
							id_n[i] += 0;
						}
					}
				}
		for(int i=0; i<5; i++) {
			if(b_n[i]!=0) {
				sim[i] = (id_n[i])/(Math.sqrt(a_n[i])*Math.sqrt(b_n[i]));
			}
		}
		
		for(int i=0; i<5; i++)
		{
			System.out.println(sim[i]);
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse("./index.xml");
		
		Element root = document.getDocumentElement();
		NodeList codelist = root.getChildNodes();
		
		String s[] = new String[codelist.getLength()];
		for(int i=0;i<5;i++)
		{
			Node item = codelist.item(i);
			Node titleNode=null, bodyNode=null;
			if(item.getNodeType() == Node.ELEMENT_NODE) { 
				titleNode = item.getFirstChild();
			} else {
				System.out.println("공백 입니다.");
			}
			s[i] = titleNode.getTextContent();
		}
		
		for(int i=0; i<sim.length;i++)
		{
			for(int j =i+1;j<sim.length;j++) {
				if(sim[i] <=sim[j]) {
					double temp = sim[i];
					sim[i] = sim[j];
					sim[j] = temp;
					String temp2 = s[i];
					s[i] = s[j];
					s[j] = temp2;
				}
			}
		}
		
		
		for(int i=0;i<5;i++) {
			if(sim[i]>0.0)
			{
				judge++;
			}
		}
		
		if(judge==0) {
			System.out.println("검색된 문서가 없습니다.");
		}
		else if(judge<3) {
			for(int i=0;i<judge;i++)
			{
				Node item = codelist.item(i);
				Node titleNode=null, bodyNode=null;
				if(item.getNodeType() == Node.ELEMENT_NODE) { 
					titleNode = item.getFirstChild();
				} else {
					System.out.println("공백 입니다.");
				}
				System.out.println(s[i]+" : "+sim[i]);
			}
		}
		else {
			if((sim[0]==sim[1])&&(sim[1]==sim[2])) {
					for(int i=2;i>=0;i--)
					{
						Node item = codelist.item(i);
						Node titleNode=null, bodyNode=null;
						if(item.getNodeType() == Node.ELEMENT_NODE) { 
							titleNode = item.getFirstChild();
						} else {
							System.out.println("공백 입니다.");
						}
						System.out.println(s[i]+" : "+sim[i]);
				}
			}
			else {
				for(int i=0;i<3;i++)
				{
					Node item = codelist.item(i);
					Node titleNode=null, bodyNode=null;
					if(item.getNodeType() == Node.ELEMENT_NODE) { 
						titleNode = item.getFirstChild();
					} else {
						System.out.println("공백 입니다.");
					}
					System.out.println(s[i]+" : "+sim[i]);
			}
			}
		}
	}
		
		
		
}
