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
 * Servlet pour la requete AJAX de suppression d'un tuple dans la table contenumodule
 *
 */
@WebServlet("/jsonservletmoduledelete")
public class JsonServletModuleDelete extends HttpServlet {
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
		try {
			deleteContenuModule(module,partie);
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
	 * Supprime le tuple recu dans la table contenumodule 
	 * @param module
	 * @param partie
	 * @throws SQLException
	 */
	protected void deleteContenuModule(String module, String partie) throws SQLException{
			PreparedStatement preparedStatement=null;
			preparedStatement =connexionjdbc.prepareStatement("DELETE from contenumodule where (module=? and partie=?)");
			preparedStatement.setString(1, module);
			preparedStatement.setString(2, partie);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		}
	
	
	
}
