
import java.io.*;
import org.json.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TocHw4 {

	/**
	 * @param args
	 */
	private static JSONArray jsonRealPrice;

	public static void main(String[] args) throws JSONException {
		// TODO Auto-generated method stub

		int i = 0, max = 0;
		String tem = null;
		ArrayList<String> stat = new ArrayList<String>();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		readfile(args[0]);

		while(jsonRealPrice.optJSONObject(i) != null){
			if(getstring(jsonRealPrice.getJSONObject(i).getString("address")).length() > 0)
				tem = getstring(jsonRealPrice.getJSONObject(i).getString("address"))+" "+Integer.toString(jsonRealPrice.getJSONObject(i).getJSONObject("fields").getJSONObject("交易年月").getInt("year"))+" "+Integer.toString(jsonRealPrice.getJSONObject(i).getJSONObject("fields").getJSONObject("交易年月").getInt("month"));
			if(stat.contains(tem) == false)
				stat.add(tem);
			i++;
		}
		
		Scanner s;
		Iterator<String> a = stat.iterator();
		while(a.hasNext()){
			s = new Scanner(a.next());
			tem = s.next();
			if(map.get(tem) == null)
				map.put(tem, 1);
			else
				map.put(tem, map.get(tem)+1);
			if(map.get(tem) > max)
				max = map.get(tem);
		}
		
		Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
		it = map.entrySet().iterator();
		ArrayList<String> list = new ArrayList<String>();
		while(it.hasNext()){
			Map.Entry<String, Integer> mEntry = (Map.Entry<String, Integer>)it.next();
			//System.out.println(mEntry.getKey()+"-----"+Integer.toString(mEntry.getValue()));
			if(mEntry.getValue() == max)
				list.add(mEntry.getKey());
		}
		
		Iterator<String> b = list.iterator();
		while(b.hasNext()){
			findinterval(b.next());
		}
		
	}

	public static void readfile(String filename){
		try{
			jsonRealPrice = new JSONArray(new JSONTokener(new FileReader(new File(filename))));
		}catch(Exception e){
			System.out.println("Can't find the filename \""+filename+"\".");
			System.exit(0);
		}

	}
	
	public static String getstring(String source) throws NullPointerException{
		String[] address = null;
		Pattern pattern = Pattern.compile("路");
		Matcher matcher = pattern.matcher(source);
		if(matcher.find() == false){
			pattern = Pattern.compile("街");
			matcher = pattern.matcher(source);
			if(matcher.find() == false){
				pattern = Pattern.compile("巷");
				matcher = pattern.matcher(source);
				if(matcher.find() == false){
					return "";
				}
				else{
					address = pattern.split(source);
					address[0] = address[0] + "巷";
				}
			}
			else{
				address = pattern.split(source);
				address[0] = address[0] + "街";
			}
		}
		else{
			address = pattern.split(source);
			address[0] = address[0] + "路";
		}
		return address[0];
	}
	
	public static void findinterval(String name) throws JSONException{
		
		
		
		Pattern pattern = Pattern.compile(name);
		Matcher matcher ;
		double max = 0, min = Double.MAX_VALUE, price;
		int i = 0;
		while(jsonRealPrice.optJSONObject(i) != null){
			matcher = pattern.matcher(jsonRealPrice.getJSONObject(i).getString("address"));
			if(matcher.find() == true){
				price = jsonRealPrice.getJSONObject(i).getJSONObject("fields").getDouble("交易總價(含車位)");
				if(price > max)
					max = price;
				if(price < min)
					min = price;
			}
			i++;
		}
		System.out.println(name+", 最高成交價："+max+", 最低成交價："+min);
	}

}
