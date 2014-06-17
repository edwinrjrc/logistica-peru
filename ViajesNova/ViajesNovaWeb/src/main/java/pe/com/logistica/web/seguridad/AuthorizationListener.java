/**
 * 
 */
package pe.com.logistica.web.seguridad;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

import pe.com.logistica.bean.negocio.Usuario;

/**
 * @author Edwin
 * 
 */
public class AuthorizationListener implements PhaseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2355404062399271061L;

	/**
	 * 
	 */
	public AuthorizationListener() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
	 */
	@Override
	public void afterPhase(PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();
		String currentPage = facesContext.getViewRoot().getViewId();

		boolean isLoginPage = (currentPage.lastIndexOf("index.xhtml") > -1);
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		if (session == null) {
			NavigationHandler nh = facesContext.getApplication()
					.getNavigationHandler();
			nh.handleNavigation(facesContext, null, "irInicioSistema");
		}

		else {
			Usuario currentUser = (Usuario)session.getAttribute("usuarioSession");

			if (!isLoginPage && (currentUser == null)) {
				NavigationHandler nh = facesContext.getApplication()
						.getNavigationHandler();
				nh.handleNavigation(facesContext, null, "irInicioSistema");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
	 */
	@Override
	public void beforePhase(PhaseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.event.PhaseListener#getPhaseId()
	 */
	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
