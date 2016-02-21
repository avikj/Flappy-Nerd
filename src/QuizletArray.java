import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
/*
@author avik
*/
public class QuizletArray {						//TermText is before every q and a
	private static String urlContents = "";
	private static String[][] setData;
	private static String setId = "71089996";
	public static void main(String[] args){
		
	}
	public static void getHtmlData(String asdf){
		 URL url = null;
			try {
				url = new URL(asdf);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 InputStream is = null;
			try {
				is = (InputStream) url.getContent();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 BufferedReader br = new BufferedReader(new InputStreamReader(is));
			 String line = null;
			 StringBuffer sb = new StringBuffer();
			 try {
				while((line = br.readLine()) != null){
				   sb.append(line);
				 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			urlContents = sb.toString();
	}
	public static int countInstances(String src, String text){
		return src.length()- src.replace(text, text.substring(1)).length();
	}
	public static void parseHtml(){
		int arrayIndex = 0;
		while(urlContents.contains("TermText")){
			int startIndex = urlContents.indexOf("TermText");
			//System.out.println(urlContents.indexOf("<", startIndex)+1);
			setData[arrayIndex][0] = urlContents.substring(urlContents.indexOf(">", startIndex)+1, urlContents.indexOf("<", startIndex));
			urlContents = urlContents.substring(urlContents.indexOf(">", startIndex));
			startIndex = urlContents.indexOf("TermText");
			setData[arrayIndex][1] = urlContents.substring(urlContents.indexOf(">", startIndex)+1, urlContents.indexOf("<", startIndex));
			urlContents = urlContents.substring(urlContents.indexOf(">", startIndex));
			arrayIndex++;
		}
	}
	public static void printSetData(){
		for(int i = 0; i <setData.length; i++){
			for(int j = 0; j<setData[i].length; j++){
				System.out.print(setData[i][j]+"\t");
			}
			System.out.println();
		}
	}
	public static String[][] getShit(String asdf){
		try{getHtmlData(asdf);
		setData = new String[countInstances(urlContents, "TermText")/2][2];
		parseHtml();
		printSetData();
		String[][] lel = new String[setData.length][3];
		for(int i = 0; i < setData.length; i++){
			for(int j = 0; j < setData[i].length; j++){
				lel[i][j] = setData[i][j];
			}
			int temp = (int)(setData.length*Math.random());
			while(lel[temp][1]==lel[i][1])
				temp = (int)(setData.length*Math.random());
			lel[i][2] =  setData[temp][1];
		}
		return lel;}catch(Exception e){
			return null;
		}
	}
}
