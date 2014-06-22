package ttt.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

//import ttt.service.WhosOnlineService;

import ttt.model.WhosOnlineService;

@Controller
public class WhosOnlineController {

	@Autowired
	WhosOnlineService wos;
	
	@RequestMapping("/whoslogin.html")
	public String login(@RequestParam(value = "username", required = false) String username){
		if(username != null){
			wos.addUsername(username);
		}
		
		return "loginout";
	}
	
	@RequestMapping("/whoslogout.html")
	public String logout(@RequestParam(value = "username", required = false) String username){
		
		if(username != null){
			wos.removeUsername(username);
		}
		
		return "loginout";
	}
	
	@RequestMapping("/whosonline1.html")
	public String wos1(ModelMap models){
		models.put("usernames", wos.getUsernames());
		
		return "whosonline1";
	}
	
	@RequestMapping("/whosonline2.html")
	public String wos2(ModelMap models){
		
		return "whosonline2";
	}
	
	@RequestMapping("/wos.json")
	public String wosJson(ModelMap models){
		models.put("usernames", wos.getUsernames());
		//{"hello":"Hello","usernames":["qq","ss"]}
		//models.put("hello", "Hello"); 
		return "jsonView";
	}
	
	@RequestMapping("/wos2.json")
	@ResponseBody
	public DeferredResult<String>  wosJson2(){
		
		DeferredResult<String> result = new DeferredResult<>();
		wos.addResult(result);
		
		return result;
	}
}
