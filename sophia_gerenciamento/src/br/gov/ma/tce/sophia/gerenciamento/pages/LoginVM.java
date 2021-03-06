package br.gov.ma.tce.sophia.gerenciamento.pages;

import javax.naming.InitialContext;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;

import br.gov.ma.tce.sophia.ejb.beans.usuario.Usuario;
import br.gov.ma.tce.sophia.ejb.beans.usuario.UsuarioFacadeBean;
import br.gov.ma.tce.sophia.ejb.util.HashUtil;

public class LoginVM {
	private Usuario usuario;

	private UsuarioFacadeBean usuarioFacade;

	private String login;
	private String senha;

	public LoginVM() {
		try {
			InitialContext ctx = new InitialContext();
			usuarioFacade = (UsuarioFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/UsuarioFacadeBean!br.gov.ma.tce.sophia.ejb.beans.usuario.UsuarioFacadeBean");
		} catch (Exception e) {
			Clients.showNotification("Erro de conexão interno. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
			e.printStackTrace();
		}
	}

	@Init
	public void init() {
		try {
			usuario = usuarioFacade.findByPrimaryKey(1);
			usuario = (Usuario) Sessions.getCurrent().getAttribute("usuario");
			
			//usuario = usuarioFacade.findByCpf("05146253331");
			//Sessions.getCurrent().setAttribute("usuario", usuario);
			
			if (usuario != null) {
				Executions.sendRedirect("/index.zul");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Executions.sendRedirect("/error.zul");
		}
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}

	@Command
	@NotifyChange(".")
	public void logar() {
		try {			
			if ((login == null || login.length() == 0) || (senha == null || senha.length() == 0)) {
				throw new Exception("Insira login e senha para entrar no sistema.");
			}

			try {
				usuario = usuarioFacade.findByCpf(login);				
			} catch (Exception e) {
				e.printStackTrace();
				Executions.sendRedirect("/error.zul");
			}
			
			if (usuario == null) {
				throw new Exception("Credenciais inválidas.");
			} else if (!HashUtil.rehashSenha(senha, usuario.getPessoa().getSenha())) {
				throw new Exception("Credenciais inválidas.");
			} else {
				Sessions.getCurrent().setAttribute("usuario", usuario);
				Executions.sendRedirect("/index.zul");
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}

	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
