

import java.io.*;
import org.json.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;


public class TocHw3 {

	/**
	 * @param args
	 */
	private static int matchnum = 0;
	private static List<Long> cost = new ArrayList<Long>();
	private static JSONArray jsonRealPrice;
	private static String expression;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int i = 0, y = 0;
		expression = ".*"+args[1]+".*"+args[2];
		readfile(args[0]);
			while(jsonRealPrice.optJSONObject(i) != null){
				search(i, Integer.parseInt(args[3]));
				i++;
			}
		average();
		//System.out.println(re(expression, "臺中市北屯區昌平路二段151~200號"));
		//setlist();
	}

	public static void readfile(String filename){
		try{
			jsonRealPrice = new JSONArray(new JSONTokener(new FileReader(new File(filename))));
		}catch(Exception e){
			System.out.println("Can't find the filename \""+filename+"\".");
			System.exit(0);
		}

	}

	public static void search(int i, int yearlimit){
		String a;
		int year = 0, j;
		try{
			a = jsonRealPrice.getJSONObject(i).getString("address");
			if(re(expression, a) == true){
				j = 0;
				while(jsonRealPrice.getJSONObject(i).getJSONArray("details").optJSONObject(j) != null){
					year = findyear(i, j);
					if(year > 0)
						break;
					j++;
				}
				if(year >= yearlimit){
					setlist(jsonRealPrice.getJSONObject(i).getJSONObject("fields").getLong("交易總價(含車位)"));
					System.out.println(jsonRealPrice.getJSONObject(i).getString("address")+","+jsonRealPrice.getJSONObject(i).getJSONObject("fields").getLong("交易總價(含車位)"));
				}
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public static int findyear(int obnum, int arnum){
		int year = 0;
		try{
			year = jsonRealPrice.getJSONObject(obnum).getJSONArray("details").getJSONObject(arnum).getJSONObject("完成年月").getInt("year");
			return year;
		}catch(JSONException e){
			return -1;
		}
	}

	public static boolean re(String target, String source){

		Pattern pattern = Pattern.compile(target);
		Matcher matcher = pattern.matcher(source);
		if(matcher.find() == true){
			return true;
		}
		else
			return false;
	}

	public static void setlist(long num){
		cost.add(new Long(num));
		matchnum++;
	}

	public static void average(){
		Iterator<Long> it = cost.iterator();
		long total = 0;
		while(it.hasNext()){
			total += it.next();
		}
		total /= matchnum;
		System.out.println("Average:"+total);
	}

}
