package ttt.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import ttt.model.Game;
import ttt.model.User;

@Test(groups = "UserDaoTest")
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    UserDao userDao;
    
    @Test
    public void getUser()
    {
        assert userDao.getUser( 1 ).getUsername().equalsIgnoreCase( "cysun" );
    }
    
    @Test
    public void getAIFinishGame()
    {
        int countFinishGame = 0;
        int countWin = 0;
        int countLoss = 0;
    	
    	User user = userDao.getUser( "cysun" );
        
        List<Game> games = userDao.getCompletedGamesAgainstAI( user );
        
        for(Game game: games){
        	if (game.getOutcome().equals("win")){       		
        		countWin++;
        		countFinishGame++;
        	}
        			
        	if (game.getOutcome().equals("loss")){
        		countLoss++;
        		countFinishGame++;
        	}
        }
        
        assert countWin == 1;
        assert countLoss == 1;
        assert countFinishGame == 2;
    }
 /*   
    @Test
    public void getCompletedGames()
    {
        int countFinishGame = 0;
        int countWin = 0;
        int countLoss = 0;
    	
    	User user = userDao.getUser( "jo" );
        
        List<Game> games = userDao.getCompletedGames( user );
        
        for(Game game: games){
        	if (game.getOutcome().equals("win")){       		
        		countWin++;
        		countFinishGame++;
        	}
        			
        	if (game.getOutcome().equals("loss")){
        		countLoss++;
        		countFinishGame++;
        	}
        	System.out.println(game.getGameType()+" "+game.getOutcome());
        }
      
    }
  */ 
    @Test
    public void getAISavedGame()
    {
    	User user = userDao.getUser( "cysun" );
    	
    	List<Game> games = userDao.getSavedGames( user );
    	
    	String position = games.get(0).getPosition();
    	
    	assert games.size() == 1;
    	
    	for (int i = 0; i < position.length(); i++){
    		if(i == 0){
    			assert (""+position.charAt(i)).equals("X");
    		}else if(i == 4){
    			assert (""+position.charAt(i)).equals("O");
    		}else{
    			assert (""+position.charAt(i)).equals("-");
    		}   		
    	}   
    		
    }

}