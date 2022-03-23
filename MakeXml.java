package practice2;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class MakeXml {
	public String split(String s) {
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(s, true);
		String out = "";
		for(int i=0; i< kl.size(); i++) {
			Keyword kwrd = kl.get(i);
			out += (kwrd.getString() + " : " + kwrd.getCnt()+"# ");
		}
		
		return out;
		
	}
}
