package tw.crazyma.loadjsontasksample2;

import org.json.JSONArray;
import org.json.JSONObject;

import tw.crazyma.loadjsontask.LoadJsonTask;
import tw.crazyma.loadjsontask.OnFinishLoadJsonListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class Main extends Activity {

//	This address might be not working. You can just change into your own Address to get the JSON format datas.
	final String urlStr = "http://crazyma.comli.com/json/test.php";
	private TextView text;
	
	private TextView textView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        textView = (TextView) findViewById(R.id.text_view);
        
        LoadJsonTask task = new LoadJsonTask();
        task.setUrlStr(urlStr);
        task.setOnFinishLoadJsonListener(onFinishListener);
        task.execute();
    }

    private OnFinishLoadJsonListener onFinishListener = new OnFinishLoadJsonListener(){

		@Override
		public void onFinish(JSONObject resultJsonObj) {
			// TODO Auto-generated method stub
			textView.setText(resultJsonObj.toString());
		}

		@Override
		public void onFinish(JSONArray resultJsonAry) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFinish(String result) {
			// TODO Auto-generated method stub
			
		}
    	
    };
    
}
