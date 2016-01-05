package Servlets;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Connexion.ConnexionJDBC;

/**
 *Servlet de la page de la liste des modules
 */
@WebServlet("/modulelist")
public class ModuleListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String promo;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageName="/modulelist.jsp";
		String redirectionPageName="/index.jsp";
		HttpSession session = request.getSession();
		boolean superlogged =(boolean) (session.getAttribute("superlogged") != null?session.getAttribute("superlogged"):false);
		boolean logged =(boolean) (session.getAttribute("logged") != null?session.getAttribute("logged"):false);
		
		if(superlogged || !logged){
			pageName =redirectionPageName;
		}
		else{
			promo =(String) session.getAttribute("promo");
			try {
				ConnexionJDBC connexionjdbc = new ConnexionJDBC();
				getModuleList(request,connexionjdbc);
				getEnseignantList(request,connexionjdbc);
				connexionjdbc.close();
				
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		RequestDispatcher rd = getServletContext().getRequestDispatcher(pageName);
		try {
			rd.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Retourne la liste des modules
	 * @param request
	 * @param connexion
	 * @throws SQLException
	 */
	private void getModuleList(HttpServletRequest request, ConnexionJDBC connexion) throws SQLException{
		PreparedStatement preparedStatement = connexion.prepareStatement("SELECT module,libelle,public,responsable from module where public like ?");
		preparedStatement.setString( 1, "%"+promo+"%" );
		ResultSet resultat =preparedStatement.executeQuery();
		ArrayList<HashMap<String,String>> resultat2= new ArrayList<HashMap<String,String>>();
		while(resultat.next()){
			HashMap<String,String> row = new HashMap<String,String>();
			row.put("id",resultat.getString("module"));
			row.put("libelle",resultat.getString("libelle"));
			row.put("public",resultat.getString("public"));
			row.put("enseignant",resultat.getString("responsable"));
			resultat2.add(row);
		}
		request.setAttribute("modulelist", resultat2);
		preparedStatement.close();
	}
	
	/**
	 * Retourne la liste des enseignants
	 * @param request
	 * @param connexion
	 * @throws SQLException
	 */
	private void getEnseignantList(HttpServletRequest request, ConnexionJDBC connexion) throws SQLException{
		Statement statement = connexion.getStatement();
		ResultSet resultat =statement.executeQuery("SELECT login from enseignant");
		ArrayList<String> resultat2= new ArrayList<String>();
		resultat2.add("null");
		while(resultat.next()){
			resultat2.add(resultat.getString("login"));
		}
		request.setAttribute("enseignantlist", resultat2);
		statement.close();
	}

}
