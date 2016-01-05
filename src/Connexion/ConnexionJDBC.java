package Connexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe permettant de se connecter à la Base de donnée
 *
 */
public class ConnexionJDBC {
	private Connection connexion = null;
	public ConnexionJDBC() throws ClassNotFoundException, SQLException {
		Class.forName( "com.mysql.jdbc.Driver" );
		String url = "jdbc:mysql://localhost:3306/PLANNINGS";
		String utilisateur = "planningDb";
		String motDePasse = "PlanningRobot";
		connexion = null;
		connexion = DriverManager.getConnection( url, utilisateur,motDePasse ); 
	}
	
	/**
	 * Retourne la connexion
	 * @return connexion
	 */
	public Connection getConnexion(){
		return connexion;
	}
	/**
	 * Retourne le statement
	 * @return statement
	 * @throws SQLException
	 */
	public Statement getStatement() throws SQLException{
		Statement statement = connexion.createStatement();
		return statement;
	}
	
	/**
	 * Fermerture de la connexion
	 */
	public void close(){
		if ( connexion != null )
			try {
			/* Fermeture de la connexion */
			connexion.close();
			} catch ( SQLException ignore ) {
			/* Si une erreur survient lors de la fermeture, il
			suffit de l'ignorer. */
			}
	}
	
	/**
	 * Etablis une requete préparée
	 * @param string requete préparée
	 * @return statement
	 * @throws SQLException
	 */
	public PreparedStatement prepareStatement(String string) throws SQLException {
		PreparedStatement statement = connexion.prepareStatement(string);
		return statement;
	}
	
}
