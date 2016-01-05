package Servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet du menu des responsables d'années
 */
@WebServlet("/menuRA")
public class MenuRAServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageName="/menuRA.jsp";
		String redirectionPageName="/index.jsp";
		HttpSession session = request.getSession();
		boolean superlogged =(boolean) (session.getAttribute("superlogged") != null?session.getAttribute("superlogged"):false);
		boolean logged =(boolean) (session.getAttribute("logged") != null?session.getAttribute("logged"):false);
		
		if(superlogged || !logged){
			pageName =redirectionPageName;
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

}
