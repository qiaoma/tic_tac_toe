package ttt.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ttt.model.User;
import ttt.model.dao.UserDao;

@Component
public class UserValidator implements Validator{

	@Autowired
	private UserDao userDao;
	 
	@Override
	public boolean supports(Class<?> clazz) {
		
		//return true if clazz is User.class or subclass
		return User.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		User user = (User)target;
	
		//check if the username is empty, then error
		//"username" identifies which field cause error
		if(!StringUtils.hasText(user.getUsername())){
			errors.rejectValue("username", "error.username.required");
		}

		if(!StringUtils.hasText(user.getPassword())){
			errors.rejectValue("password", "error.password.required");
		}
	}
	
	public void loginValidate(Object target, Errors errors) {
		
		validate(target, errors);
		
		User user = (User)target;		
		User userInDB = userDao.getUser(user.getUsername());
		
		if(StringUtils.hasText(user.getUsername()) && userInDB == null){
			errors.rejectValue("username", "error.username.notfound");
		}
		
		if(userInDB != null && !user.getPassword().equals(userInDB.getPassword())){			
			errors.rejectValue("password", "error.password.notexists");			
		}		
	}
	
	public void registerValidate(Object target, Errors errors) {
		
		validate(target, errors);
		
		User user = (User)target;		
		User userInDB = userDao.getUser(user.getUsername());
		if(userInDB != null){
			errors.rejectValue("username", "error.username.exists");
		}
		
		if(!StringUtils.hasText(user.getEmail())){
			errors.rejectValue("email", "error.email.required");
		}
		
	}

}
