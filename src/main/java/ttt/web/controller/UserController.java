package ttt.web.controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import ttt.model.Authority;
import ttt.model.User;
import ttt.model.dao.UserDao;
import ttt.web.validator.UserValidator;

@Controller
@SessionAttributes("user")
public class UserController {

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private UserValidator userValidator;
    
    @RequestMapping(value="login", method=RequestMethod.GET)
    public String login(ModelMap models ){
      
    	models.put( "user", new User() );
    	
        return "login";
    }
    
    @RequestMapping(value="login", method=RequestMethod.POST)
    public String login(@ModelAttribute User user, ModelMap models, HttpSession session, BindingResult result){
    	
    	userValidator.loginValidate(user, result);
    	if(result.hasErrors()){
    		return "login";
    	}    	
    	
    	String username = user.getUsername();
    	models.put("user", user);
    			
    	User userDB = userDao.getUser(username);
    	int userDB_Id = userDB.getId();
    	user.setId(userDB_Id);
    		
    	session.setAttribute("user", user);
    	//return "redirect:gameBoard.html";
    	return "playerOption";
    }
    
    @RequestMapping(value="register", method=RequestMethod.GET)
    public String register(ModelMap models){
    	models.put("user", new User());
    	return "register";
    }
    
    @RequestMapping(value="register", method=RequestMethod.POST)
    public String register(@ModelAttribute User user, SessionStatus sessionStatus, BindingResult result){
    	
    	userValidator.registerValidate(user, result);
    	if(result.hasErrors()){
    		return "register";
    	}    
    	String username = user.getUsername();
    	Authority authority = new Authority();
    	authority.setUsername(username);
    	authority.setAuthority("ROLE_USER");
	    userDao.saveUser(user);
	    userDao.saveAuthority(authority);
	    sessionStatus.setComplete();
	    return "confirm"; 
    }
}

