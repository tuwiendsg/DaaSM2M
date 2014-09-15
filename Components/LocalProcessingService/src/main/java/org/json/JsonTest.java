package org.json;

public class JsonTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JSONTokener jtoken = new JSONTokener(" {\"id\":\"1270\",\"latitude\":\"10.77586\",\"longitude\":\"106.634\"}");
		
		JSONObject jsn = (JSONObject) jtoken.nextValue();
		System.out.println( jsn.get("id")+" "+jsn.get("longitude")+" "+jsn.getString("latitude"));
	}

}
