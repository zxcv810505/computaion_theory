
import org.json.*;

import java.io.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Proj {

	
	public static String[] attribute_name = {"所在地縣市", "交易月份", "完成月份", "有無車位", "有無隔間", "有無管理組織", "客廳數目", "衛浴數目"};
	public static HashMap<String, String> attribute = new HashMap<String, String>();
	public static HashMap<String, Integer> map = new HashMap<String, Integer>();
	
	private static JSONArray jsonRealPrice;
	/**
	 * @param args
	 */
	public static void main(String[] args){
		// TODO Auto-generated method stub
		if(args.length != 3){
			System.out.println("The total number of Parameters is wrong!!");
			System.exit(0);
		}
		int i = 0;
		readfile(args[0]);
		try{
			while(jsonRealPrice.optJSONObject(i) != null){
				combinition(Integer.valueOf(args[2]), jsonRealPrice.getJSONObject(i));
				i++;
			}
			
		}
		catch(JSONException e){}
		Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, Integer> mEntry = (Map.Entry<String, Integer>)it.next();
		}
		ArrayList<Map.Entry<String, Integer>> data_list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
		Collections.sort(data_list, new Comparator<Map.Entry<String, Integer>>(){
				public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2){
					return entry2.getValue()-entry1.getValue();
		}
		});
		Iterator<Map.Entry<String, Integer>> abc = data_list.iterator();
		i = 0;
		try{
		File file = new File("output.txt");
		file.delete();
		FileWriter fileWriter = new FileWriter("output.txt", true);
		Map.Entry<String, Integer> t;
		String tem = null;
		while(i < Integer.valueOf(args[1])){
			t = abc.next();
			tem = t.getKey()+";"+t.getValue(); 
			fileWriter.append(tem+"\r\n");
			i++;
		}
		fileWriter.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
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
	
	public static void combinition(int num, JSONObject a){
		int i, j, k, l;
		String result = null;
		set(a);
		
		if(num == 2)
			for(i = 0; i < 7 ; i++){
				
				for(j = i +1 ; j < 8; j++){
					
					result = attribute_name[i]+":"+attribute.get(attribute_name[i])+","+attribute_name[j]+":"+attribute.get(attribute_name[j]);
					if(map.get(result) == null)
						map.put(result, 1);
					else
						map.put(result, map.get(result)+1);
				}
			}
		
		else if(num == 3)
			for(i = 0; i < 6 ; i++){
				
				for(j = i +1 ; j < 7; j++){
					
					for(k = j + 1; k < 8; k++){
						
						result = attribute_name[i]+":"+attribute.get(attribute_name[i])+","+attribute_name[j]+":"+attribute.get(attribute_name[j])+","+attribute_name[k]+":"+attribute.get(attribute_name[k]);
						if(map.get(result) == null)
							map.put(result, 1);
						else
							map.put(result, map.get(result)+1);
					}
				}
			}
		
		else if(num == 4)
			for(i = 0; i < 5 ; i++){
				
				for(j = i +1 ; j < 6; j++){
					
					for(k = j + 1; k < 7; k++){
						
						for(l = k + 1; l < 8; l++){
							
							result = attribute_name[i]+":"+attribute.get(attribute_name[i])+","+attribute_name[j]+":"+attribute.get(attribute_name[j])+","+attribute_name[k]+":"+attribute.get(attribute_name[k])+","+attribute_name[l]+":"+attribute.get(attribute_name[l]);
							if(map.get(result) == null)
								map.put(result, 1);
							else
								map.put(result, map.get(result)+1);
						}
					}
				}
			}
	}
	
	public static void set(JSONObject a) {
		clearattribute();
		try{
		String[] aa = null;
		int i;
		String tem = a.getString("address");
		Pattern pattern = Pattern.compile("市");
		Matcher matcher = pattern.matcher(tem);
		if(matcher.find() == false){
			pattern = Pattern.compile("縣");
			matcher = pattern.matcher(tem);
			if(matcher.find() == true){
				aa = pattern.split(tem);
				aa[0] = aa[0] + "縣";
			}
		}
		else{
			aa = pattern.split(tem);
			aa[0] = aa[0] + "市";
		}
		if(aa != null)
			attribute.put(attribute_name[0], aa[0]);
		
		attribute.put(attribute_name[1], Integer.toString(a.getJSONObject("fields").getJSONObject("交易年月").getInt("month")));
		
		int month = 0, j = 0;
		while(a.getJSONArray("details").isNull(j) == false){
			month = findmonth(a.getJSONArray("details"), j);
			if(month != 0)
				break;
			j++;
		}
		if(month>=0)
			attribute.put(attribute_name[2], Integer.toString(month));
		else
			attribute.put(attribute_name[2], null);
		
		
		if(a.getJSONObject("fields").getJSONObject("交易筆棟數").getInt("車位") > 0)
			attribute.put(attribute_name[3], "有");
		else
			attribute.put(attribute_name[3], "無");
		
		
		try{
			attribute.put(attribute_name[5], a.getJSONObject("fields").getString("有無管理組織"));
		}catch(JSONException e){}
		
		
		attribute.put(attribute_name[4], a.getJSONObject("fields").getJSONObject("建物現況格局").getString("隔間"));
		
		
		attribute.put(attribute_name[6], Integer.toString(a.getJSONObject("fields").getJSONObject("建物現況格局").getInt("廳")));
		
		attribute.put(attribute_name[7], Integer.toString(a.getJSONObject("fields").getJSONObject("建物現況格局").getInt("衛")));
		}catch(JSONException e){}
		
	}
	
	public static void clearattribute(){
		for(int i = 0; i < 8; i ++){
			attribute.remove(attribute_name[i]);
			attribute.put(attribute_name[i], null);
		}
	}
	
	public static int findmonth(JSONArray a, int arnum){
		int month = 0;
		try{
			month = a.getJSONObject(arnum).getJSONObject("完成年月").getInt("month");
			return month;
		}catch(JSONException e){
			return -1;
		}
	}

}
