package tw.crazyma.loadjsontask;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class LoadJsonTask extends AsyncTask<Void, Void, JSONObject> {
	final private String tag = "crazyma";
	
	private String urlStr;	
	private int connectionTimeout,socketTimeout;
	private OnFinishLoadJsonListener onFinishListener;
	private OnParseJsonListener onParseJsonListener;
	
	public LoadJsonTask(){
		init();
	}
	
	public LoadJsonTask(OnFinishLoadJsonListener onFinishListener){
		this.onFinishListener = onFinishListener;
		init();
	}
	
	public LoadJsonTask(String urlStr,OnFinishLoadJsonListener onFinishListener){
		this.urlStr = urlStr;
		this.onFinishListener = onFinishListener;
		init();
	}
	
	protected void init(){
		connectionTimeout = socketTimeout = -1;
	}
	
	@Override
	protected JSONObject doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Log.d(tag,"LoadJsonTask | onInBackground");
		JSONObject jsonObj = null;
		if(urlStr != null)
			jsonObj = onDownload();		
		else
			Log.e(tag,"Url is null");
		
		return jsonObj;
	}

	@Override
	protected void onPostExecute(JSONObject jsonObj) {
		// TODO Auto-generated method stub
		super.onPostExecute(jsonObj);
		Log.d(tag,"LoadJsonTask | onPostExecute");
		if(jsonObj != null){
			if(onFinishListener != null)
				if(onParseJsonListener != null && onParseJsonListener.onParse(jsonObj) != null){
					Object obj = onParseJsonListener.onParse(jsonObj);
					if(obj != null){
						if(obj instanceof JSONObject){
							JSONObject jObj = (JSONObject) obj;
							onFinishListener.onFinish(jObj);
						}else if(obj instanceof JSONArray){
							JSONArray jAry = (JSONArray) obj;
							onFinishListener.onFinish(jAry);
						}else if(obj instanceof String){
							String str = (String) obj;
							onFinishListener.onFinish(str);
						}else{
							Log.e(tag,"Reture Value from OnParsejsonListener id undefined");
						}
					}else
						Log.e(tag,"ParseJsonListener Result is Null");
				}else{
					onFinishListener.onFinish(jsonObj);
				}
			else
				Log.w(tag,"OnLoadJsonFinishListener is Null");
		}else{
			Log.e(tag,"onPostExecute Result Parameter is Null");
		}
	}
	
	protected JSONObject onDownload(){
		HttpGet request = new HttpGet(urlStr);
		JSONObject jsonObj = null;

		try {
			HttpResponse response = new DefaultHttpClient().execute(request);
			HttpParams httpParameters = new BasicHttpParams();
			if(connectionTimeout != -1)
				HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeout);
			
			if(socketTimeout != -1)
				HttpConnectionParams.setSoTimeout(httpParameters, socketTimeout);
			
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				jsonObj = new JSONObject(EntityUtils.toString(response.getEntity()));
				Log.i(tag,jsonObj.toString());
			}else{
				Log.e(tag,"Http Connection Error. Status Code : " + response.getStatusLine().getStatusCode());
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.toString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.toString();
		}
		return jsonObj;
	}
	
	public OnFinishLoadJsonListener getOnFinishListener() {
		return onFinishListener;
	}

	public void setOnFinishLoadJsonListener(OnFinishLoadJsonListener onFinishListener) {
		this.onFinishListener = onFinishListener;
	}
	
	public OnParseJsonListener getOnParseJsonListener() {
		return onParseJsonListener;
	}

	public void setOnParseJsonListener(OnParseJsonListener onParseJsonListener) {
		this.onParseJsonListener = onParseJsonListener;
	}

	public String getUrlStr() {
		return urlStr;
	}

	public void setUrlStr(String urlStr) {
		this.urlStr = urlStr;
	}

}
