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

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 2주차 실습 코드
 * 
 * 주어진 5개의 html 문서를 전처리하여 하나의 xml 파일을 생성하세요. 
 * 
 * input : data 폴더의 html 파일들
 * output : collection.xml 
 */

public class makeCollection {
	
	private String data_path;
	private String output_file = "./collection.xml";
	
	public makeCollection(String path) {
		this.data_path = path;
	}
	public File[] makeFileList(String path) {
		File dir = new File(path);
		return dir.listFiles();
	}
	
	public void makeXml() throws ParserConfigurationException, IOException, TransformerException{
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		Document docu = docBuilder.newDocument();
		
		Element docs = docu.createElement("docs");
		docu.appendChild(docs);
		
		File[] files = makeFileList(this.data_path);
		
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
TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		DOMSource source = new DOMSource(docu);
		StreamResult result = new StreamResult(new FileOutputStream(new File(output_file)));
		
		transformer.transform(source,result);
		System.out.println("2주차 실행완료");
	}
}
