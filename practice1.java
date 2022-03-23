package practice2;

import java.io.File;
import java.io.FileOutputStream;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class practice1 {

	public static File[] makeFileList(String path) {
		File dir = new File(path);
		return dir.listFiles();
	}
	public void combineHtml() throws IOException, ParserConfigurationException, TransformerException {
		
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		Document docu = docBuilder.newDocument();
		
		Element docs = docu.createElement("docs");
		docu.appendChild(docs);
		
		File[] files = makeFileList("C:\\Users\\user\\Downloads\\2주차 실습 html\\2주차 실습 html");
		
		for(int i=0; i<5; i++) {
			org.jsoup.nodes.Document html = Jsoup.parse(files[i],"UTF-8");
			String titleData = html.title();
			String bodyData = html.body().text();
			
			Element doc = docu.createElement("doc");
			docs.appendChild(doc);
			doc.setAttribute("id",Integer.toString(i));
			
			Element title = docu.createElement("title");
			title.appendChild(docu.createTextNode(titleData));
			doc.appendChild(title);
			
			Element body = docu.createElement("body");
			body.appendChild(docu.createTextNode(bodyData));
			doc.appendChild(body);
			
			
		}
		
		// TODO Auto-generated method stub
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		DOMSource source = new DOMSource(docu);
		StreamResult result = new StreamResult(new FileOutputStream(new File("D:\\collection.xml")));
		
		transformer.transform(source,result);
	}

}
