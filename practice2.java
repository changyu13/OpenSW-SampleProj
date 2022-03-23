package practice2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class practice2 {

	public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException, SAXException {
		// TODO Auto-generated method stub
		
		MakeXml x = new MakeXml();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		Document docu = docBuilder.newDocument();
		
		Element docs = docu.createElement("docs");
		docu.appendChild(docs);
		
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse("D:\\collection.xml");
		
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
			body.appendChild(docu.createTextNode(x.split(bodyData)));
			doc.appendChild(body);
			
			
		}
		
	TransformerFactory transformerFactory = TransformerFactory.newInstance();
			
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			
			DOMSource source = new DOMSource(docu);
			StreamResult result = new StreamResult(new FileOutputStream(new File("D:\\index.xml")));
			
			transformer.transform(source,result);
	}
}
