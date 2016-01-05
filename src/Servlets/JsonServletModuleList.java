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
 * Servlet pour la requete AJAX pour mettre à jour l'enseignant d'un module
 *
 */
@WebServlet("/jsonservletmodulelist")
public class JsonServletModuleList extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ConnexionJDBC connexionjdbc =null;   
    
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
				connexionjdbc = new ConnexionJDBC();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
		int length =request.getParameterMap().keySet().size()/2;
		for(int i=0;i<length;i++) {
	        String Str_module = "responsablemodule["+i+"][module]";
	        String Str_enseignant = "responsablemodule["+i+"][enseignant]";
			String module = request.getParameter(Str_module);
			String enseignant = request.getParameter(Str_enseignant);
			try {
				updateEnseignant(module,enseignant);
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
	 * Met a jour l'enseignant d'un module
	 * @param module
	 * @param enseignant
	 * @throws SQLException
	 */
	protected void updateEnseignant(String module,String enseignant) throws SQLException{
			PreparedStatement preparedStatement=null;
			if(enseignant.equals("null")){
				preparedStatement =connexionjdbc.prepareStatement("UPDATE module set responsable =NULL where module=?");
				preparedStatement.setString(1, module);
			}
			else{
				preparedStatement =connexionjdbc.prepareStatement("UPDATE module set responsable =? where module=?");
				preparedStatement.setString(1, enseignant);
				preparedStatement.setString(2, module);
			}
			
			preparedStatement.executeUpdate();
			preparedStatement.close();
		}
	
	
	}
