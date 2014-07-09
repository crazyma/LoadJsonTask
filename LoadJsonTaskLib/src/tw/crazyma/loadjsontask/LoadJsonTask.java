package tw.crazyma.loadjsontask;

import java.io.IOException;
import java.lang.reflect.Type;

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

public class LoadJsonTask extends AsyncTask<Void, Void, Object> {
	final private String tag = "crazyma";
	
	private String urlStr,errorDescription;
	private Exception exception;
	private int connectionTimeout,socketTimeout;
	private OnFinishLoadJsonListener onFinishListener;	
	private OnParseJSONObjectListener onParseJSONObjectListener;
	private OnParseJSONArrayListener onParseJSONArrayListener;
	private OnTaskFailListener onTaskFailListener;
	private Type typeOfT;
	private Class<Type> classOfT;
	
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
	protected Object doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Log.d(tag,"LoadJsonTask | onInBackground");
		Object obj = null;
		if(urlStr != null)
			obj = onDownload();		
		else{
			Log.e(tag,"Url is null");			
			errorDescription = "Url is null";
		}
		
		return obj;
	}

	@Override
	protected void onPostExecute(Object obj) {
		// TODO Auto-generated method stub
		super.onPostExecute(obj);
		Log.d(tag,"LoadJsonTask | onPostExecute");
		if(obj != null){
			if(onFinishListener != null){
				if(obj instanceof JSONObject){
					if(onParseJSONObjectListener != null && onParseJSONObjectListener.onParse((JSONObject)obj) != null){
						Object parsedObj = onParseJSONObjectListener.onParse((JSONObject)obj);
						if(parsedObj instanceof JSONObject){
							JSONObject jObj = (JSONObject) parsedObj;
							onFinishListener.onFinish(jObj);
						}else if(parsedObj instanceof JSONArray){
							JSONArray jAry = (JSONArray) parsedObj;
							onFinishListener.onFinish(jAry);
						}else if(parsedObj instanceof String){
							String str = (String) parsedObj;
							onFinishListener.onFinish(str);
						}else{
							Log.e(tag,"Reture Value from OnParseJSONObjectListener is undefined");
							errorDescription = "Reture Value from OnParseJSONObjectListener is undefined";
						}
					}else{
						onFinishListener.onFinish((JSONObject)obj);
					}
				}else{
					if(onParseJSONArrayListener != null && onParseJSONArrayListener.onParse((JSONArray)obj) != null){
						Object parsedObj = onParseJSONArrayListener.onParse((JSONArray)obj);
						if(parsedObj instanceof JSONObject){
							JSONObject jObj = (JSONObject) parsedObj;
							onFinishListener.onFinish(jObj);
						}else if(parsedObj instanceof JSONArray){
							JSONArray jAry = (JSONArray) parsedObj;
							onFinishListener.onFinish(jAry);
						}else if(parsedObj instanceof String){
							String str = (String) parsedObj;
							onFinishListener.onFinish(str);
						}else{
							Log.e(tag,"Reture Value from OnParseJSONArrayListener is undefined");
							errorDescription = "Reture Value from OnParseJSONArrayListener is undefined";
						}
					}else{
						onFinishListener.onFinish((JSONArray)obj);
					}
				}
			}else
				Log.e(tag,"OnLoadJsonFinishListener is Null | You need to implement OnLoadJsonFinishListener");
				errorDescription = "OnLoadJsonFinishListener is Null | You need to implement OnLoadJsonFinishListener";
		}else{
			Log.e(tag,"onPostExecute Result Parameter is Null | The response is not a JSONObject or a JSONArray");
			errorDescription = "OnLoadJsonFinishListener is Null | You need to implement OnLoadJsonFinishListener";
		}
		
		if(onTaskFailListener != null && !(errorDescription == null && exception == null)){
			onTaskFailListener.onTaskFail(errorDescription, exception);
		}
	}
	
	protected Object onDownload(){
		HttpGet request = new HttpGet(urlStr);
		Object object = null;

		try {
			HttpResponse response = new DefaultHttpClient().execute(request);
			HttpParams httpParameters = new BasicHttpParams();
			if(connectionTimeout != -1)
				HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeout);
			
			if(socketTimeout != -1)
				HttpConnectionParams.setSoTimeout(httpParameters, socketTimeout);
			
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){			
				String responseStr = EntityUtils.toString(response.getEntity());
				if(responseStr.substring(0, 1).equals("{"))
					object = new JSONObject(responseStr);
				else if(responseStr.substring(0, 1).equals("[")){
					object = new JSONArray(responseStr);
				}
			}else{
				Log.e(tag,"Http Connection Error. Status Code : " + response.getStatusLine().getStatusCode());
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.toString();
			exception = e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.toString();
			exception = e;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.toString();
			exception = e;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.toString();
			exception = e;
		}
		return object;
	}
	
	public OnFinishLoadJsonListener getOnFinishListener() {
		return onFinishListener;
	}

	public void setOnFinishLoadJsonListener(OnFinishLoadJsonListener onFinishListener) {
		this.onFinishListener = onFinishListener;
	}
	
	public OnParseJSONObjectListener getOnParseJSONObjectListener() {
		return onParseJSONObjectListener;
	}

	public void setOnParseJSONObjectListener(
			OnParseJSONObjectListener onParseJSONObjectListener) {
		this.onParseJSONObjectListener = onParseJSONObjectListener;
	}

	public OnParseJSONArrayListener getOnParseJSONArrayListener() {
		return onParseJSONArrayListener;
	}

	public void setOnParseJSONArrayListener(
			OnParseJSONArrayListener onParseJSONArrayListener) {
		this.onParseJSONArrayListener = onParseJSONArrayListener;
	}

	public String getUrlStr() {
		return urlStr;
	}

	public void setUrlStr(String urlStr) {
		this.urlStr = urlStr;
	}

	public OnTaskFailListener getOnTaskFailListener() {
		return onTaskFailListener;
	}

	public void setOnTaskFailListener(OnTaskFailListener onTaskFailListener) {
		this.onTaskFailListener = onTaskFailListener;
	}

	public Type getTypeOfT() {
		return typeOfT;
	}

	public void setTypeOfT(Type typeOfT) {
		this.typeOfT = typeOfT;
	}

	public Class<Type> getClassOfT() {
		return classOfT;
	}

	public void setClassOfT(Class<Type> classOfT) {
		this.classOfT = classOfT;
	}
	
	

	
}
