package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Connexion.ConnexionJDBC;

/**
 * Servlet pour la requete AJAX d'ajout d'un tuple dans la table contenumodule
 *
 */
@WebServlet("/jsonservletmoduleadd")
public class JsonServletModuleAdd extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
private ConnexionJDBC connexionjdbc =null;   
    
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
				connexionjdbc = new ConnexionJDBC();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}

		String module = request.getParameter("module");
		String partie = request.getParameter("partie");
		String type = request.getParameter("type");
		String nbHeures = request.getParameter("nbHeures");
		String enseignant = request.getParameter("enseignant");
		try {
			updateContenuModule(module,partie,type,nbHeures,enseignant);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connexionjdbc.close();
		try {
	        response.setContentType("application/json");
	        PrintWriter out = response.getWriter();
	        out.println("{");
	        out.println("\"Sample\": \"Empty JSON\"");
	        out.println("}");
	        out.close();
		} catch (IOException e) {
	        e.printStackTrace();
	    }

	  }
	
	/**
	 * Met a jour la table contenumodule avec les valeurs recu
	 * @param module
	 * @param partie
	 * @param type
	 * @param nbHeures
	 * @param enseignant
	 * @throws SQLException
	 */
	protected void updateContenuModule(String module,String partie,String type,String nbHeures,String enseignant) throws SQLException{
			PreparedStatement preparedStatement=null;
			if(enseignant.equals("null")){
				preparedStatement =connexionjdbc.prepareStatement("INSERT INTO contenumodule (module,partie,type,nbHeures,hed,enseignant) VALUES (?,?,?,?,?,NULL)");
				preparedStatement.setString(1, module);
				preparedStatement.setString(2, partie);
				preparedStatement.setString(3, type);
				preparedStatement.setString(4, nbHeures);
				preparedStatement.setString(5, nbHeures);
				
				
			}
			else{
				preparedStatement =connexionjdbc.prepareStatement("INSERT INTO contenumodule (module,partie,type,nbHeures,hed,enseignant) VALUES (?,?,?,?,?,?)   ");
				preparedStatement.setString(1, module);
				preparedStatement.setString(2, partie);
				preparedStatement.setString(3, type);
				preparedStatement.setString(4, nbHeures);
				preparedStatement.setString(5, nbHeures);
				preparedStatement.setString(6, enseignant);
			}
			
			preparedStatement.executeUpdate();
			preparedStatement.close();
		}
	
	
	
}
