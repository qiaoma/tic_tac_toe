package ttt.model;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WhosOnlineService {
	
	List<String> usernames;
	List<DeferredResult<String>> results;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public WhosOnlineService(){
		usernames = new ArrayList<>();
		results = new ArrayList<>();
		//objectMapper = new ObjectMapper();
	}
	
	public void addResult(DeferredResult<String> result){
		results.add(result);
	}

	public List<String> getUsernames() {
		return usernames;
	}

	public void setUsernames(List<String> usernames) {
		this.usernames = usernames;
	}
	
	public void addUsername(String username){
		
		if(!usernames.contains(username)){
			usernames.add(username);
			setResults();
		}
	}
	
	public void removeUsername(String username){
		usernames.remove(username);
		setResults();
	}
	
	private void setResults(){
		
		StringWriter sw = new StringWriter();
		try {
			objectMapper.writeValue(sw, usernames);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String json = sw.toString();
		
		//set results for all the results in results
		for(DeferredResult<String> result : results){
			
			result.setResult(json);
		}
		
		results.clear();
		
	}
}
