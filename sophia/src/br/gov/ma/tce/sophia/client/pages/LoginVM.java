package br.gov.ma.tce.sophia.client.pages;

import java.util.Collection;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.PersistenceException;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Window;

import br.gov.ma.tce.gestores.server.beans.estado.Estado;
import br.gov.ma.tce.gestores.server.beans.estado.EstadoFacade;
import br.gov.ma.tce.gestores.server.beans.municipio.Municipio;
import br.gov.ma.tce.gestores.server.beans.municipio.MunicipioFacade;
import br.gov.ma.tce.seguranca.interno.server.beans.usuario.Usuario;
import br.gov.ma.tce.seguranca.interno.server.beans.usuario.UsuarioFacadeBean;
import br.gov.ma.tce.sophia.client.utils.EmailSOPHIA;
import br.gov.ma.tce.sophia.client.utils.LongOperation;
import br.gov.ma.tce.sophia.ejb.beans.evento.Evento;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.PessoaFacadeBean;
import br.gov.ma.tce.sophia.ejb.util.CnpjCpf;
import br.gov.ma.tce.sophia.ejb.util.HashUtil;

public class LoginVM {
	private Pessoa pessoa;
	private Usuario usuarioSeg;
	private br.gov.ma.tce.gestores.server.beans.usuario.Usuario usuarioJuris;

	private EmailSOPHIA emailSOPHIA;

	private String login, senha, id;
	private String matriculaServidor, cpfServidor, cpfJurisdicionado;
	private String cpf, email;
	private boolean loginValido = false;
	private EmailValidator emailValidator;

	private Collection<Estado> estados;
	private Collection<Municipio> municipios;

	private PessoaFacadeBean pessoaFacade;
	private UsuarioFacadeBean usuarioSegFacade;
	private br.gov.ma.tce.gestores.server.beans.usuario.UsuarioFacade usuarioJurisFacade;
	private EstadoFacade estadoFacade;
	private MunicipioFacade municipioFacade;

	@Wire("#modalCadastro")
	private Window modalCadastro;

	@Wire("#modalRecuperarSenha")
	private Window modalRecuperarSenha;

	@Wire("#modalLogin")
	private Window modalLogin;

	@Wire("#modalLogin #boxServidor")
	private Groupbox boxServidor;

	@Wire("#modalLogin #boxJuridiscionado")
	private Groupbox boxJuridiscionado;

	@Wire("#modalCadastro #comboTipoPessoa")
	private Combobox comboTipoPessoa;

	@Wire("#modalCadastro #comboEstados")
	private Combobox comboEstados;

	@Wire("#modalCadastro #comboMunicipios")
	private Combobox comboMunicipios;

	@Wire("#modalConfirmaCadastro")
	private Window modalConfirmaCadastro;

	public LoginVM() {
		try {
			InitialContext ctx = new InitialContext();
			pessoaFacade = (PessoaFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/PessoaFacadeBean!br.gov.ma.tce.sophia.ejb.beans.pessoa.PessoaFacadeBean");
			usuarioSegFacade = (UsuarioFacadeBean) ctx.lookup(
					"java:global/sophia_ear/seguranca_interno_server/UsuarioFacadeBean!br.gov.ma.tce.seguranca.interno.server.beans.usuario.UsuarioFacadeBean");
			usuarioJurisFacade = (br.gov.ma.tce.gestores.server.beans.usuario.UsuarioFacade) ctx.lookup(
					"java:global/sophia_ear/gestores_server/gertrudes_UsuarioFacadeBean!br.gov.ma.tce.gestores.server.beans.usuario.UsuarioFacade");
			estadoFacade = (EstadoFacade) ctx.lookup(
					"java:global/sophia_ear/gestores_server/gertrudes_EstadoFacadeBean!br.gov.ma.tce.gestores.server.beans.estado.EstadoFacade");
			municipioFacade = (MunicipioFacade) ctx.lookup(
					"java:global/sophia_ear/gestores_server/gertrudes_MunicipioFacadeBean!br.gov.ma.tce.gestores.server.beans.municipio.MunicipioFacade");
		} catch (NamingException e) {
			Clients.showNotification("Erro de conexão interno. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
			e.printStackTrace();
		}
	}

	@Init
	public void init() {
		pessoa = (Pessoa) Sessions.getCurrent().getAttribute("pessoa");
		if (pessoa != null) {
			Executions.sendRedirect("/index.zul");
		} else {
			try {
				pessoa = pessoaFacade.findByPrimaryKey(1);
				pessoa = new Pessoa();
				estados = estadoFacade.findAll();
				emailSOPHIA = new EmailSOPHIA();
				emailValidator = new EmailValidator();
			} catch (Exception e) {
				Executions.sendRedirect("/error.zul");
			}
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
				pessoa = pessoaFacade.findByCpf(login);
			} catch (Exception e) {
				e.printStackTrace();
				Executions.sendRedirect("/error.zul");
			}

			if (pessoa == null) {
				throw new Exception("Credenciais inválidas.");
			} else if (!HashUtil.rehashSenha(senha, pessoa.getSenha())) {
				throw new Exception("Credenciais inválidas.");
			} else {
				Sessions.getCurrent().setAttribute("pessoa", pessoa);
				Evento evento = (Evento) Sessions.getCurrent().getAttribute("evento");
				if (evento != null) {
					/*
					 * Se o usuário não estava logado e clica em pré inscrição no index.zul é
					 * atribuido valor true, caso não seja true, ele está vindo da página
					 * inscricao/inscricao.zul
					 */
					boolean vemDoIndex = (boolean) Sessions.getCurrent().getAttribute("vemDoIndex");
					Sessions.getCurrent().removeAttribute("vemDoIndex");
					if (vemDoIndex == true) {
						Executions.sendRedirect("/index.zul");
					} else {
						String caminho = "/inscricao/inscricao.zul?id=" + evento.getEventoId();
						Executions.sendRedirect(caminho);
					}
				} else {
					Executions.sendRedirect("/index.zul");
				}

			}

		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalRecuperaSenha(@BindingParam("visible") boolean visible) {
		try {
			modalRecuperarSenha.setVisible(visible);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void recuperaSenha() {
		try {
			if (this.cpf == null || !testaQuantNumeros(this.cpf, 11)) {
				this.setCpf(null);
			}

			if (this.cpf == null || (this.email == null || this.email.equals(""))) {
				throw new Exception("Todos os campos obrigatórios precisam ser preenchidos corretamente.");
			} else if (!emailValidator.isValid(email, null)) {
				throw new Exception("Insira um endereço de e-mail válido.");
			} else {
				try {
					pessoa = pessoaFacade.findByCpfAndEmail(this.cpf.replaceAll("\\D+", ""), this.email);
					if (pessoa == null) {
						throw new Exception("Não foi encontrado cadastro válido com os dados inseridos.");
					} else {
						Pessoa destinatario = new Pessoa();
						destinatario.setNome(pessoa.getNome());
						destinatario.setCpf(pessoa.getCpf());
						destinatario.setEmail(pessoa.getEmail());
						String senha = HashUtil.getSenhaRandom();
						pessoa.setSenha(HashUtil.hashSenha(senha));
						if (pessoaFacade.update(pessoa) != null) {
							new LongOperation() {
								@Override
								protected void execute() throws InterruptedException {
									emailSOPHIA.emailRecuperaSenha(destinatario, senha, " ");
								}
							}.start();
							this.cpf = this.email = null;
							pessoa = new Pessoa();
							modalRecuperarSenha.setVisible(false);
							Clients.showNotification("Uma nova senha foi enviada para seu email!",
									Clients.NOTIFICATION_TYPE_INFO, null, null, 4000, true);
						} else {
							throw new Exception(
									"Ocorreu um erro inesperado ao fazer a recuperação de senha. Tente novamente mais tarde.");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					Executions.sendRedirect("/error.zul");
				}
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalCadastro(@BindingParam("visible") boolean visible) {
		try {
			pessoa = new Pessoa();
			if (visible) {
				modalCadastro.setVisible(true);
			} else {
				comboEstados.setSelectedItem(null);
				comboMunicipios.setSelectedItem(null);
				loginValido = false;
				modalCadastro.setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalLoginDeUsuario(@BindingParam("visible") boolean visible) {
		try {
			matriculaServidor = cpfServidor = cpfJurisdicionado = null;
			if (visible) {
				usuarioSeg = null;
				usuarioJuris = null;
				loginValido = false;
				pessoa.setServidorMatricula(null);
				pessoa.setNome(null);
				pessoa.setCpf(null);
				pessoa.setEmail(null);
				if (pessoa.getTipoPessoa().equals("1")) {
					loginValido = true;
				} else {
					if (pessoa.getTipoPessoa().equals("2")) {
						boxServidor.setVisible(true);
						boxJuridiscionado.setVisible(false);
					} else if (pessoa.getTipoPessoa().equals("3")) {
						boxServidor.setVisible(false);
						boxJuridiscionado.setVisible(true);
					}
					modalLogin.setVisible(true);
				}
			} else {
				if (!loginValido) {
					pessoa.setServidorMatricula(null);
					pessoa.setNome(null);
					pessoa.setCpf(null);
					pessoa.setEmail(null);
					pessoa.setRepeteEmail(null);
					pessoa.setTipoPessoa(null);
					comboTipoPessoa.setSelectedItem(null);
				}
				modalLogin.setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void validarServidor() {
		try {
			String cpfInserido = null;
			if (cpfServidor != null) {
				cpfInserido = cpfServidor.replaceAll("\\D+", "");
			}

			if ((matriculaServidor == null || matriculaServidor.length() == 0)
					|| (cpfInserido == null || cpfInserido.length() == 0)) {
				throw new Exception("Todos os campos precisam ser preenchidos.");
			}

			String cpfEncontrado = null;
			usuarioSeg = usuarioSegFacade.findByMatricula(Integer.parseInt(matriculaServidor));
			if (usuarioSeg != null) {
				cpfEncontrado = usuarioSeg.getServidor().getCpf();
				while (cpfEncontrado.length() < 11) {
					cpfEncontrado = "0" + cpfEncontrado;
				}
			}
			if (usuarioSeg == null) {
				throw new Exception("Dados não encontrados nos registros de Servidores.");
			} else if (!cpfEncontrado.equals(cpfInserido)) {
				usuarioSeg = null;
				throw new Exception("Dados não encontrados nos registros de Servidores.");
			} else {
				try {
					Pessoa p = pessoaFacade.findByMatricula(matriculaServidor);
					if (p != null) {
						throw new Exception("Matrícula de servidor já cadastrada para outro participante.");
					} else {
						loginValido = true;
						pessoa.setServidorMatricula(matriculaServidor);
						if (usuarioSeg.getNome().toUpperCase() != null) {
							pessoa.setNome(usuarioSeg.getNome().toUpperCase());
						}
						if (usuarioSeg.getServidor().getCpf() != null) {
							pessoa.setCpf(usuarioSeg.getServidor().getCpf());
							pessoa.setCpf(pessoa.getCpfStr());
						}
						if (usuarioSeg.getServidor().getEmail() != null) {
							pessoa.setEmail(usuarioSeg.getServidor().getEmail().toLowerCase());
							pessoa.setRepeteEmail(usuarioSeg.getServidor().getEmail().toLowerCase());
						}
						Clients.showNotification(
								"Validação de servidor realizada com sucesso! Os campos foram liberados para finalizar seu cadastro.",
								Clients.NOTIFICATION_TYPE_INFO, null, null, 2000, true);
					}
				} catch (PersistenceException e) {
					e.printStackTrace();
					Executions.sendRedirect("/error.zul");
				} catch (Exception e) {
					Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
				}
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void validarJurisdicionado() {
		try {
			String cpfInserido = null;
			if (cpfJurisdicionado != null) {
				cpfInserido = cpfJurisdicionado.replaceAll("\\D+", "");
			}

			if (cpfInserido == null || cpfInserido.length() == 0) {
				throw new Exception("Todos os campos precisam ser preenchidos.");
			}

			usuarioJuris = usuarioJurisFacade.findByLogin(cpfInserido);
			if (usuarioJuris == null) {
				throw new Exception("CPF não encontrado nos registros de Jurisdicionados.");
			} else if (usuarioJuris.getUsuarioInterno().equals("S")) {
				throw new Exception("Acesso não permitido a usuário interno. Cadastre-se como SOCIEDADE.");
			} else {
				try {
					Pessoa p = pessoaFacade.findByCpf(cpfInserido);
					if (p != null) {
						throw new Exception("CPF de jurisdicionado já cadastrado para outro usuário.");
					} else {
						loginValido = true;
						pessoa.setCpf(cpfInserido);
						pessoa.setCpf(pessoa.getCpfStr());
						Clients.showNotification(
								"Validação de juridiscionado realizada com sucesso! Os campos foram liberados para finalizar seu cadastro.",
								Clients.NOTIFICATION_TYPE_INFO, null, null, 2000, true);
					}
				} catch (PersistenceException e) {
					e.printStackTrace();
					Executions.sendRedirect("/error.zul");
				} catch (Exception e) {
					Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
				}
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarListaPorEstado() {
		try {
			if (pessoa.getEstado() != null) {
				municipios = municipioFacade.findMunicipiosByEstado(pessoa.getEstado().getEstadoId());
				pessoa.setMunicipio(null);
				comboMunicipios.setSelectedItem(null);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaCadastro(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				consertaDados();
				if (pessoa.getTipoPessoa() == null) {
					throw new Exception("Selecione o tipo de usuário e complete seu cadastro.");
				} else if ((pessoa.getNome() == null || pessoa.getNome().equals("")) || pessoa.getCpf() == null
						|| pessoa.getSexo() == null
						|| (pessoa.getLogradouro() == null || pessoa.getLogradouro().equals(""))
						|| (pessoa.getNumero() == null || pessoa.getNumero().equals("")) || pessoa.getEstado() == null
						|| pessoa.getMunicipio() == null
						|| (pessoa.getBairro() == null || pessoa.getBairro().equals("")) || pessoa.getCep() == null
						|| pessoa.getDtNascimento() == null
						|| (pessoa.getEmail() == null || pessoa.getEmail().equals(""))
						|| (pessoa.getRepeteEmail() == null || pessoa.getRepeteEmail().equals(""))
						|| (pessoa.getSenha() == null || pessoa.getSenha().equals(""))
						|| (pessoa.getRepeteSenha() == null || pessoa.getRepeteSenha().equals(""))
						|| pessoa.getTipoPessoa() == null) {
					throw new Exception("Todos os campos obrigatórios precisam ser preenchidos corretamente.");
				} else if (!CnpjCpf.isValidCPF(pessoa.getCpf().replaceAll("\\D+", ""))) {
					throw new Exception("O CPF informado é inválido");
				} else if (pessoa.getTelCelular() == null && pessoa.getTelFixo() == null
						&& pessoa.getTelComercial() == null) {
					throw new Exception("Preencha pelo menos um telefone para contato.");
				} else if (!emailValidator.isValid(pessoa.getEmail(), null)) {
					throw new Exception("Insira um endereço de e-mail válido.");
				} else if (pessoaFacade.findByCpf(pessoa.getCpf().replaceAll("\\D+", "")) != null) {
					throw new Exception("CPF já cadastrado.");
				} else if (pessoaFacade.findByEmail(pessoa.getEmail()) != null) {
					throw new Exception("E-mail já cadastrado.");
				} else if (pessoa.getSenha().length() < 4) {
					throw new Exception("A senha deve ter no mínimo 4 caracteres.");
				} else if (!pessoa.getEmail().equals(pessoa.getRepeteEmail())) {
					throw new Exception("E-mails não correspondem.");
				} else if (!pessoa.getSenha().equals(pessoa.getRepeteSenha())) {
					throw new Exception("Senhas não correspondem.");
				} else {
					modalConfirmaCadastro.setVisible(true);
				}
			} else {
				modalConfirmaCadastro.setVisible(false);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void cadastro() {
		try {
			pessoa.setDtCadastro(new Date());
			pessoa.setSenha(HashUtil.hashSenha(pessoa.getSenha()));
			if (pessoaFacade.include(pessoa) != null) {
				Pessoa destinatario = new Pessoa();
				destinatario.setNome(pessoa.getNome());
				destinatario.setEmail(pessoa.getEmail());
				new LongOperation() {
					@Override
					protected void execute() throws InterruptedException {
						emailSOPHIA.emailCadastro(destinatario, " ");
					}
				}.start();
				modalConfirmaCadastro.setVisible(false);
				modalCadastro.setVisible(false);
				Clients.showNotification("Cadastro realizado com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
						3000, true);
			}
		} catch (Exception e) {
			Clients.showNotification("Ocorreu um erro inesperado ao fazer o cadastro. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	/**
	 * Testa quantidade de números em uma string
	 * 
	 * @param input
	 * @param quant
	 * @return se a string contém o número de números em quant
	 */
	private Boolean testaQuantNumeros(String input, int quant) {
		if (input != null) {
			int count = 0;
			for (int i = 0; i < input.length(); i++) {
				if (Character.isDigit(input.charAt(i))) {
					count++;
				}
			}
			return count == quant;
		} else {
			return false;
		}
	}

	private void consertaDados() {
		if (!testaQuantNumeros(pessoa.getCpf(), 11)) {
			pessoa.setCpf(null);
		}

		if (!testaQuantNumeros(pessoa.getTelFixo(), 10)) {
			pessoa.setTelFixo(null);
		}

		if (!testaQuantNumeros(pessoa.getTelComercial(), 10)) {
			pessoa.setTelComercial(null);
		}

		if (!testaQuantNumeros(pessoa.getTelCelular(), 11)) {
			pessoa.setTelCelular(null);
		}

		if (!testaQuantNumeros(pessoa.getCep(), 8)) {
			pessoa.setCep(null);
		}

		if (pessoa.getComplemento() != null && pessoa.getComplemento().trim().equals("")) {
			pessoa.setComplemento(null);
		}
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMatriculaServidor() {
		return matriculaServidor;
	}

	public void setMatriculaServidor(String matriculaServidor) {
		this.matriculaServidor = matriculaServidor;
	}

	public String getCpfServidor() {
		return cpfServidor;
	}

	public void setCpfServidor(String cpfServidor) {
		this.cpfServidor = cpfServidor;
	}

	public String getCpfJurisdicionado() {
		return cpfJurisdicionado;
	}

	public void setCpfJurisdicionado(String cpfJurisdicionado) {
		this.cpfJurisdicionado = cpfJurisdicionado;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Usuario getUsuarioSeg() {
		return usuarioSeg;
	}

	public void setUsuarioSeg(Usuario usuarioSeg) {
		this.usuarioSeg = usuarioSeg;
	}

	public br.gov.ma.tce.gestores.server.beans.usuario.Usuario getUsuarioJuris() {
		return usuarioJuris;
	}

	public void setUsuarioJuris(br.gov.ma.tce.gestores.server.beans.usuario.Usuario usuarioJuris) {
		this.usuarioJuris = usuarioJuris;
	}

	public Collection<Estado> getEstados() {
		// Remove dados desnecessários presentes no banco da lista de estados
		if (estados != null) {
			estados.remove(estadoFacade.findByPrimaryKey(0));
			estados.remove(estadoFacade.findByPrimaryKey(99));
		}
		return estados;
	}

	public void setEstados(Collection<Estado> estados) {
		this.estados = estados;
	}

	public Collection<Municipio> getMunicipios() {
		return municipios;
	}

	public void setMunicipios(Collection<Municipio> municipios) {
		this.municipios = municipios;
	}

	public boolean isLoginValido() {
		return loginValido;
	}

	public void setLoginValido(boolean loginValido) {
		this.loginValido = loginValido;
	}
}
