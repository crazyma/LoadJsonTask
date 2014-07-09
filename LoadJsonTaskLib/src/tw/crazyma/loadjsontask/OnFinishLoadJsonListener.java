package tw.crazyma.loadjsontask;

import org.json.JSONArray;
import org.json.JSONObject;

public interface OnFinishLoadJsonListener {
	public void onFinish(JSONObject resultJsonObj);
	public void onFinish(JSONArray resultJsonAry);
	public void onFinish(String result);
	public void onFinish(Object resultObject);
}
