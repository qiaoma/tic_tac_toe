package ttt.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ttt.model.Authority;
import ttt.model.Game;
import ttt.model.User;
import ttt.model.dao.UserDao;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    //@PostAuthorize("principal.username == returnObject.username")
    public User getUser( Integer id )
    {
        return entityManager.find( User.class, id );
    }

    //Retrieve a user from the database given the user's username
	@Override
	public User getUser(String username) {
		
		List<User> users = getUsers();
		for(User user: users){
			if(user.getUsername().equals(username)){
				return user;
			}
		}
		return null;
	}
	  
	@Override
	@Transactional
	public User saveUser(User user){
	  	return entityManager.merge(user);
	}
	
	@Override
	public List<User> getUsers() {
		//can order by username
		return entityManager.createQuery( "from User order by id", User.class )
	            .getResultList();
	} 
	
	@Override
	public Game getGame(Integer id) {
		
		return entityManager.find( Game.class, id );
	}

	@Override
	public List<Game> getCompletedGamesAgainstAI(User user) {
				
		return entityManager.createQuery("from Game where gameType = 1 and "
				+ "(outcome = 'win' or outcome = 'loss' or outcome = 'tie') and player1 = :player1", 
				Game.class ).setParameter("player1", user).getResultList();
	}
	
	@Override
	public List<Game> getCompletedGamesAgainstPlayer(User user) {
		
		return entityManager.createQuery("from Game where gameType = 2 and "
				+ "(outcome = 'win' or outcome = 'loss' or outcome = 'tie') and player1 = :player1", 
				Game.class ).setParameter("player1", user).getResultList();
		
	}

	@Override
	public List<Game> getCompletedGames(User user) {
		
		return entityManager.createQuery("from Game where "
				+ "(outcome = 'win' or outcome = 'loss' or outcome = 'tie') and player1 = :player1", 
				Game.class ).setParameter("player1", user).getResultList();
	}
	
	@Override
	public List<Game> getGames(User user) {
	
		return entityManager.createQuery("from Game where player1 = :player1", 
				Game.class ).setParameter("player1", user).getResultList();
	}

	//Retrieve all the saved games by a user
	@Override
	public List<Game> getSavedGames(User user) {
		
		return entityManager.createQuery("from Game where player1 = :player1 and outcome = 'unfinish'", Game.class )
        		.setParameter("player1", user).getResultList();
		
	}

	@Override
	@Transactional
	public Game saveGame(Game game) {
		
		return entityManager.merge(game);
	}

	@Override
	@Transactional
	public Authority saveAuthority(Authority authority) {
		
		return entityManager.merge(authority);
	}

}
