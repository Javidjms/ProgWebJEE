package Servlets;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Connexion.ConnexionJDBC;

/**
 * 
 *Servlet de la page de vidage de la table module
 */
@WebServlet("/modelclearing")
public class ModelClearingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doProcess(request,response);
		}

		
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doProcess(request,response);
		}
		
		
		protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String pageName="/menuDDE";
			String redirectionPageName="/index.jsp";
			HttpSession session = request.getSession();
			boolean superlogged =(boolean) (session.getAttribute("superlogged") != null?session.getAttribute("superlogged"):false);
			if(!superlogged){
				pageName =redirectionPageName;
			}
			else{
				try {
					ClearingData();
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}
			RequestDispatcher rd = getServletContext().getRequestDispatcher(pageName);
			request.setAttribute("message", "Vidage de la base effectuée avec succès");
			try {
				rd.forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}

		/**
		 * Vide la table Module
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		private void ClearingData() throws ClassNotFoundException, SQLException {
			ConnexionJDBC connexionjdbc = new ConnexionJDBC();
			connexionjdbc.getStatement().executeUpdate( "TRUNCATE Module ;" );
			 if ( connexionjdbc != null ) {
				 connexionjdbc.close();
			 }
		}
	}
