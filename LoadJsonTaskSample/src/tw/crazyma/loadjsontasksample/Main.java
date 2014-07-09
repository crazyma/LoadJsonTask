package tw.crazyma.loadjsontasksample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tw.crazyma.loadjsontask.LoadJsonTask;
import tw.crazyma.loadjsontask.OnFinishLoadJsonListener;
import tw.crazyma.loadjsontask.OnParseJSONArrayListener;
import tw.crazyma.loadjsontask.OnParseJSONObjectListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class Main extends Activity {
	
	//	This address might be not working. You can just change into your own Address to get the JSON format datas.
	final String urlStr = "http://crazyma.comli.com/json/array_test.php";
//	final String urlStr = "http://crazyma.comli.com/json/test.php";
	private TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		text = (TextView) findViewById(R.id.text);
		
		LoadJsonTask task = new LoadJsonTask();
		task.setUrlStr(urlStr);
//		task.setOnParseJSONObjectListener(onParseJSONObjectListener);	//	optional
//		task.setOnParseJSONArrayListener(onParseJSONArrayListener);	//optional
		task.setOnFinishLoadJsonListener(onFinishListener);
		
		task.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private OnParseJSONObjectListener onParseJSONObjectListener = new OnParseJSONObjectListener(){

		@Override
		public Object onParse(JSONObject jsonObj) {
			// TODO Auto-generated method stub
			try {
				JSONObject parsedJsonObj = jsonObj.getJSONObject("data");
				return parsedJsonObj;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	};
	
	private OnParseJSONArrayListener onParseJSONArrayListener = new OnParseJSONArrayListener(){

		@Override
		public Object onParse(JSONArray jsonAry) {
			// TODO Auto-generated method stub
			String str = null;
			try {
				str = (String)jsonAry.get(0);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return str;
		}
		
	};
	
	private OnFinishLoadJsonListener onFinishListener = new OnFinishLoadJsonListener(){

		@Override
		public void onFinish(JSONObject resultJsonObj) {
			// TODO Auto-generated method stub
			text.setText(resultJsonObj.toString());
		}

		@Override
		public void onFinish(JSONArray resultJsonAry) {
			// TODO Auto-generated method stub
			text.setText(resultJsonAry.toString());
		}

		@Override
		public void onFinish(String result) {
			// TODO Auto-generated method stub
			text.setText(result);
		}
		
	};

}
