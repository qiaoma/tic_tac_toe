package ttt.model.dao;

import java.util.List;

import ttt.model.Authority;
import ttt.model.Game;
import ttt.model.User;

public interface UserDao {

    User getUser( Integer id );
    
    User getUser( String username );
    
    User saveUser(User user);
    
    Authority saveAuthority(Authority authority);
    
    Game getGame(Integer id);
    
    List<User> getUsers();
    
    List<Game> getCompletedGamesAgainstAI( User user );
    
    List<Game> getCompletedGamesAgainstPlayer( User user );
    
    List<Game> getCompletedGames( User user );
    
    List<Game> getGames( User user );
    
    List<Game> getSavedGames( User user );
    
    Game saveGame(Game game);

}
