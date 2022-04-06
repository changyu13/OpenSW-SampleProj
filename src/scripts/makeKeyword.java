package scripts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 3주차 실습 코드
 * 
 * kkma 형태소 분석기를 이용하여 index.xml 파일을 생성하세요.
 * 
 * index.xml 파일 형식은 아래와 같습니다.
 * (키워드1):(키워드1에 대한 빈도수)#(키워드2):(키워드2에 대한 빈도수)#(키워드3):(키워드3에 대한 빈도수) ... 
 * e.g., 라면:13#밀가루:4#달걀:1 ...
 * 
 * input : collection.xml
 * output : index.xml 
 */

public class makeKeyword {

	private String input_file;
	private String output_file = "./index.xml";
	
	public makeKeyword(String file) {
		this.input_file = file;
	}
	
	public String split(String s) {
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(s, true);
		String out = "";
		for(int i=0; i< kl.size(); i++) {
			Keyword kwrd = kl.get(i);
			out += (" "+kwrd.getString() + " : " + kwrd.getCnt()+"# ");
		}
		
		return out;
		
	}
	public void convertXml() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		
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
			
			Element doc = docu.createElement("doc");
			docs.appendChild(doc);
			doc.setAttribute("id",Integer.toString(i));
			
			Element title = docu.createElement("title");
			title.appendChild(docu.createTextNode(titleData));
			doc.appendChild(title);
			
			Element body = docu.createElement("body");
			body.appendChild(docu.createTextNode(split(bodyData)));
			doc.appendChild(body);
			
			
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
			
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			
			DOMSource source = new DOMSource(docu);
			StreamResult result = new StreamResult(new FileOutputStream(new File(output_file)));
			
			transformer.transform(source,result);
			System.out.println("3주차 실행완료");
	}

}
