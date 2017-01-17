package com.pingan.yzt;

import android.content.res.Resources;
import android.nfc.Tag;
import android.util.JsonToken;
import android.util.Log;
import android.widget.Toast;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.pingan.yzt.AndroidDes3Util;
import com.pingan.yztsso.Callback;
import com.pingan.yztsso.SSO;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class echoes a string called from JavaScript.
 */
public class YiZhangTong extends CordovaPlugin {

    private static final String TAG = "YiZhangTong";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(args, callbackContext);
            return true;
        }
        return false;
    }

    private void coolMethod(JSONArray args, final CallbackContext callbackContext) throws  JSONException{
        if (args != null && args.length() > 0) {
            Log.d(TAG,args.get(0)+","+args.get(1)+","+args.get(2));
            String appId = args.getString(0);
            String uname = args.getString(1);
            String pwd = args.getString(2);
            String isTest = args.getString(3);
            if(isTest.equals("1")){
                SSO.setTestApi(false);
            }else{
                SSO.setTestApi(true);
            }
            
            SSO.isDebug();

            SSO.init(this.cordova.getActivity(),appId);
            SSO.login("", uname, pwd, "N", new Callback() {
                @Override
                public void onSuccess(String s) {
                    Map<String,Object> map = new HashMap<String, Object>();
                    Log.d(TAG,String.format("org data:%s",s));
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String des3Assert = jsonObject.getString("des3Assert");
                        if (des3Assert == null || des3Assert.equals("")){
                            map.put("result","1001");
                            map.put("retmsg","接口调用失败。");
                            callbackContext.success(new JSONObject(map).toString());
                            return;
                        }
                        String userXmlInfo = AndroidDes3Util.decode(des3Assert);
                        Log.d(TAG,String.format("trans data: %s",userXmlInfo));

                        map = deal(userXmlInfo);
                        if (map == null || !(map.get("result")+"").equals("0000")){
                            map.put("result","1000");
                            map.put("retmsg","登录失败。");
                            callbackContext.success(new JSONObject(map).toString());
                            return;
                        }

                        Map<String,Object> rntUserInfo = new HashMap<String, Object>();
                        rntUserInfo.put("result","0000");
                        rntUserInfo.put("data",(Map<String,String>)map.get("atts"));
                        JSONObject obj=new JSONObject(rntUserInfo);
                        Log.d(TAG,String.format("return json: %s",obj.toString()));
                        callbackContext.success(obj.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        map.put("result","1002");
                        map.put("retmsg","系统异常");
                        callbackContext.success(new JSONObject(map).toString());
                    }

                }

                @Override
                public void onFailed(String s, Throwable throwable) {
                    Map<String,Object> map = new HashMap<String, Object>();
                    Log.d(TAG,throwable.toString());
                    map.put("result","1001");
                    map.put("retmsg","系统异常");
                    callbackContext.success(new JSONObject(map).toString());
                    // callbackContext.error("{'result':'1001','retmsg','"+throwable.toString()+"'}");
                }
            });

        } else {
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("result","1001");
            map.put("retmsg","系统异常");
            callbackContext.error(new JSONObject(map).toString());
        }
    }


    public Map<String,Object> deal(String xmlData){
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));
            Element element = document.getDocumentElement();
            NodeList nodeList = element.getChildNodes();

            for(int i=0;i<nodeList.getLength();i++){
                Node node = nodeList.item(i);
                if (! (node instanceof Element)){
                    continue;
                }

                map.put(node.getNodeName(),node.getTextContent());
                Log.d(TAG,String.format("second child: %s",node.getNodeName()+","+node.getTextContent()));

                if (node.getNodeName().equals("attributes")){
                    NodeList atts = node.getChildNodes();
                    Map<String,String> attMap = new HashMap<String, String>();
                    for(int j=0;j<atts.getLength();j++){
                        Node attNode = atts.item(j);
                        if (!(attNode instanceof Element)){
                            continue;
                        }

                        //cnName,idType,idNo,sex,mobileNo
                        attMap.put(attNode.getAttributes().getNamedItem("key").getNodeValue(),
                                attNode.getAttributes().getNamedItem("value").getNodeValue());

                    }
                    map.put("atts",attMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}
