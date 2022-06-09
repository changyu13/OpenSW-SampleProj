package API;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class API {

	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		

		Scanner scan= new Scanner(System.in);
		
		System.out.print("검색어를 입력하세요:");
		String user = scan.nextLine();
		String text = URLEncoder.encode(user,"UTF-8");
		
	    String clientId = "ce7ReoabEP7vVF4JyG_H";
		String clientSecret = "RmTkiaLeCk";
		
		String apiURL = "https://openapi.naver.com/v1/search/movie?query="+text;
		
		URL url = new URL(apiURL);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		
		con.setRequestMethod("GET");
		con.setRequestProperty("X-Naver-Client-Id",clientId);
		con.setRequestProperty("X-Naver-Client-Secret",clientSecret);
		
		
		//API 응답-수신
		int responseCode = con.getResponseCode();
		BufferedReader br;
		if(responseCode==200) {
			br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		}
		else {
			br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		}
		
		String inputLine;
		StringBuffer response = new StringBuffer();
		while((inputLine=br.readLine())!=null) {
			response.append(inputLine);
		}
		br.close();
		
		String str = response.toString();
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(str);
		JSONArray infoArray = (JSONArray) jsonObject.get("items");
		
		for(int i=0; i<infoArray.size();i++) {
			System.out.println("=item_"+i+"============================");
			JSONObject itemObject = (JSONObject)infoArray.get(i);
			System.out.println("title:\t"+itemObject.get("title"));
			System.out.println("subtitle:\t"+itemObject.get("subtitle"));
			System.out.println("director:\t"+itemObject.get("director"));
			System.out.println("actor:\t"+itemObject.get("actor"));
			System.out.println("userRating:\t"+itemObject.get("userRating")+"\n");
		}
	}

}
