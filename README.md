介紹
--


客製化 AsyncTask, 專門用來Download Json 並可自行定義初步的解析規則
方便developer使用

Variable
-----------------

####Required
 1. urlStr : 網址
 2. onFinishListener : Task 結束時所呼叫的interface

####Optional
 1. connectionTimeout : connection timeout 的時間(毫秒)
 2. socketTimeout : socket timeout 的時間(毫秒)
 3. onParseJsonListener : 當原始JSON Object下載完成, 可以定義初步的解析規則

Interface
---
####OnFinishLoadJsonListener
######有三個method :

      onFinish(JSONObject resultJsonObj)
      onFinish(JSONArray resultJsonAry)
	  onFinish(String resultStr)

如果沒有設定 `onParseJsonListener`, 則會呼叫`onFinish(JSONObject resultJsonObj)`, 
`resultJsonObj` 則會是從server上收到的完整 `JSONObject`

####OnParseJsonListener
######有一個method :
      onParse(JSONObject jsonObj)

* 引數 `jsonObj` 是從server上收到的完整 `JSONObject`, 使用者可以在 `onParse` 裡定義自己的parsing規則
* 回傳值可以為三種：`JSONObject`, `JSONArray`, `String`, 會分別呼叫對應的 `onFinish` Method
* 非此三種的回傳值, 則不會呼叫任何一個 `onFinish`

簡易使用流程
--
 1. 宣告LoadJsonTask物件
 2. 設定 url 網址
 3. 設定 onFinishLoadJsonListener
 4. 呼叫 loadJsonTaskObject.execute()

--

		LoadJsonTask task = new LoadJsonTask();
		task.setUrlStr(" your url address ");
		task.setOnFinishLoadJsonListener(new OnFinishLoadJsonListener(){
			@override
			public onFinish(JSONObject resultJsonObj){
				//	do you jobs
			}
		});
		task.execute();

Required Permission
--
		<uses-permission android:name="android.permission.INTERNET">
