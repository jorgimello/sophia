package br.gov.ma.tce.sophia.gerenciamento.pages;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import br.gov.ma.tce.gestores.server.beans.estado.Estado;
import br.gov.ma.tce.gestores.server.beans.estado.EstadoFacade;
import br.gov.ma.tce.gestores.server.beans.municipio.Municipio;
import br.gov.ma.tce.gestores.server.beans.municipio.MunicipioFacade;
import br.gov.ma.tce.seguranca.interno.server.beans.usuario.UsuarioFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.atividade.AtividadeFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.capacitacaoservidor.CapacitacaoServidor;
import br.gov.ma.tce.sophia.ejb.beans.capacitacaoservidor.CapacitacaoServidorFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.diasatividade.DiasAtividade;
import br.gov.ma.tce.sophia.ejb.beans.diasatividade.DiasAtividadeFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.evento.Evento;
import br.gov.ma.tce.sophia.ejb.beans.evento.EventoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.frequencia.Frequencia;
import br.gov.ma.tce.sophia.ejb.beans.frequencia.FrequenciaFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscritoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresse;
import br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresseFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.PessoaFacadeBean;
import br.gov.ma.tce.sophia.ejb.util.Filtro;
import br.gov.ma.tce.sophia.ejb.vo.RelatorioMensalCapacitacoesVO;
import br.gov.ma.tce.sophia.gerenciamento.utils.DestinatarioUtil;
import br.gov.ma.tce.sophia.gerenciamento.utils.EmailSOPHIA;
import br.gov.ma.tce.sophia.gerenciamento.utils.LongOperation;
import br.gov.ma.tce.sophia.gerenciamento.utils.Report;

public class ParticipantesVM {
	private Evento eventoSelecionado;
	private Atividade atividadeSelecionada;
	private Pessoa participanteSelecionado;
	private ParticipanteInteresse participanteInteresse;
	private ParticipanteInscrito participanteInscrito;
	private CapacitacaoServidor capacitacaoServidor;
	private Filtro filtroParticipante;

	private EmailSOPHIA emailSOPHIA;

	private File lista;
	private boolean alterarEstadoVisible = false;
	private boolean status = false;
	private String filtroSelecionado, textoMensagem, tipoPessoaSelecionado, mesRelatorio, anoRelatorio,
			filtroTipoParticipante;
	private EmailValidator emailValidator;
	private int vagasSociedade = 0, vagasServidor = 0, vagasJurisdicionado = 0;
	private static Integer vagasTotal = 0, vagasDisponiveis = 0, inscricoesAprovadas = 0, destinatariosSelecionados = 0;
	private static final String textoRodape1 = "%d participante(s) encontrado(s)";
	private static final String textoRodape2 = "%d participante(s) encontrado(s) - Inscrições aprovadas: %d - Vagas disponíveis: %d - Total de vagas: %d";
	private static final String textoRodape3 = "%d participante(s) encontrado(s)";
	private static final String textoRodape4 = "%d participante(s) encontrado(s) - Destinatários selecionados: %d";

	private Collection<Evento> eventos;
	private Collection<Atividade> atividades;
	private Collection<Pessoa> participantes;
	private Collection<ParticipanteInteresse> participantesInteressados;
	private Collection<ParticipanteInscrito> participantesInscritos;
	private Collection<ParticipanteInscrito> participantesComAlteracao;
	private Collection<ParticipanteInscrito> participantesSemAlteracao;
	private Collection<CapacitacaoServidor> capacitacoes;
	private Collection<DestinatarioUtil> destinatarios;
	private Collection<Estado> estados;
	private Collection<Municipio> municipios;

	private EventoFacadeBean eventoFacade;
	private AtividadeFacadeBean atividadeFacade;
	private PessoaFacadeBean pessoaFacade;
	private ParticipanteInteresseFacadeBean participanteInteresseFacade;
	private ParticipanteInscritoFacadeBean participanteInscritoFacade;
	private DiasAtividadeFacadeBean diasAtividadeFacade;
	private FrequenciaFacadeBean frequenciaFacade;
	private CapacitacaoServidorFacadeBean capacitacaoServidorFacade;
	private EstadoFacade estadoFacade;
	private MunicipioFacade municipioFacade;
	private UsuarioFacadeBean usuarioSegFacade;

	@Wire("#comboCategorias")
	private Combobox comboCategorias;

	@Wire("#comboEventos")
	private Combobox comboEventos;

	@Wire("#comboAtividades")
	private Combobox comboAtividades;

	@Wire("#modalAtualizarParticipante #comboEstadosModal")
	private Combobox comboEstadosModal;

	@Wire("#modalAtualizarParticipante #comboMunicipios")
	private Combobox comboMunicipios;

	@Wire("#vlayoutParticipantes #comboParticipantes")
	private Combobox comboParticipantes;

	@Wire("#modalExcluirInscricao")
	private Window modalExcluirInscricao;

	@Wire("#modalConfirmarInscricao")
	private Window modalConfirmarInscricao;

	@Wire("#modalExcluirListaDeInteresse")
	private Window modalExcluirListaDeInteresse;

	@Wire("#modalAtualizarParticipante")
	private Window modalAtualizarParticipante;

	@Wire("#modalListarCapacitacoes")
	private Window modalListarCapacitacoes;

	@Wire("#modalRegistrarCapacitacao")
	private Window modalRegistrarCapacitacao;

	@Wire("#modalConfirmacao")
	private Window modalConfirmacao;

	@Wire("#modalConfirmaRemoverCapacitacao")
	private Window modalConfirmaRemoverCapacitacao;

	@Wire("#modalRelatorioMensal")
	private Window modalRelatorioMensal;

	@Wire("#vlayoutCategorias")
	private Vlayout vlayoutCategorias;

	@Wire("#vlayoutEventos")
	private Vlayout vlayoutEventos;

	@Wire("#vlayoutAtividades")
	private Vlayout vlayoutAtividades;

	@Wire("#vlayoutParticipantes")
	private Vlayout vlayoutParticipantes;

	@Wire("#vlayoutFiltroNome")
	private Vlayout vlayoutFiltroNome;

	@Wire("#modalRelatorioMensal #iframeRelatorioMensal")
	private Iframe iframeRelatorioMensal;

	public ParticipantesVM() {
		try {
			InitialContext ctx = new InitialContext();
			eventoFacade = (EventoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/EventoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.evento.EventoFacadeBean");
			atividadeFacade = (AtividadeFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/AtividadeFacadeBean!br.gov.ma.tce.sophia.ejb.beans.atividade.AtividadeFacadeBean");
			pessoaFacade = (PessoaFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/PessoaFacadeBean!br.gov.ma.tce.sophia.ejb.beans.pessoa.PessoaFacadeBean");
			participanteInteresseFacade = (ParticipanteInteresseFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ParticipanteInteresseFacadeBean!br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresseFacadeBean");
			participanteInscritoFacade = (ParticipanteInscritoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ParticipanteInscritoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscritoFacadeBean");
			diasAtividadeFacade = (DiasAtividadeFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/DiasAtividadeFacadeBean!br.gov.ma.tce.sophia.ejb.beans.diasatividade.DiasAtividadeFacadeBean");
			frequenciaFacade = (FrequenciaFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/FrequenciaFacadeBean!br.gov.ma.tce.sophia.ejb.beans.frequencia.FrequenciaFacadeBean");
			capacitacaoServidorFacade = (CapacitacaoServidorFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/CapacitacaoServidorFacadeBean!br.gov.ma.tce.sophia.ejb.beans.capacitacaoservidor.CapacitacaoServidorFacadeBean");
			estadoFacade = (EstadoFacade) ctx.lookup(
					"java:global/sophia_ear/gestores_server/gertrudes_EstadoFacadeBean!br.gov.ma.tce.gestores.server.beans.estado.EstadoFacade");
			municipioFacade = (MunicipioFacade) ctx.lookup(
					"java:global/sophia_ear/gestores_server/gertrudes_MunicipioFacadeBean!br.gov.ma.tce.gestores.server.beans.municipio.MunicipioFacade");
			usuarioSegFacade = (UsuarioFacadeBean) ctx.lookup(
					"java:global/sophia_ear/seguranca_interno_server/UsuarioFacadeBean!br.gov.ma.tce.seguranca.interno.server.beans.usuario.UsuarioFacadeBean");
		} catch (NamingException e) {
			Clients.showNotification("Erro de conexão interno. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
			e.printStackTrace();
		}
	}

	@Init
	public void init() {
		participantes = new ArrayList<Pessoa>();
		participantesInteressados = new ArrayList<ParticipanteInteresse>();
		participantesInscritos = new ArrayList<ParticipanteInscrito>();
		destinatarios = new ArrayList<DestinatarioUtil>();
		estados = estadoFacade.findAll();
		filtroParticipante = new Filtro();
		emailValidator = new EmailValidator();
		emailSOPHIA = new EmailSOPHIA();
		anoRelatorio = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		countVagas();
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}

	@Command
	@NotifyChange(".")
	private void countVagas() {
		if (atividadeSelecionada != null) {
			vagasTotal = atividadeSelecionada.getVagas();
			vagasDisponiveis = (int) (vagasTotal
					- participanteInscritoFacade.countInscricoesByAtividade(atividadeSelecionada));
			inscricoesAprovadas = vagasTotal - vagasDisponiveis;
			if (atividadeSelecionada.getVagasServidor() != null) {
				vagasSociedade = (int) (atividadeSelecionada.getVagasSociedade() - participanteInscritoFacade
						.countInscricoesByAtividadeAndTipoParticipante(atividadeSelecionada, "1"));
				vagasServidor = (int) (atividadeSelecionada.getVagasServidor() - participanteInscritoFacade
						.countInscricoesByAtividadeAndTipoParticipante(atividadeSelecionada, "2"));
				vagasJurisdicionado = (int) (atividadeSelecionada.getVagasJurisdicionado() - participanteInscritoFacade
						.countInscricoesByAtividadeAndTipoParticipante(atividadeSelecionada, "3"));
			}
		} else {
			vagasTotal = vagasDisponiveis = inscricoesAprovadas = destinatariosSelecionados = vagasSociedade = vagasServidor = vagasJurisdicionado = 0;
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarEventosPorCategoria(@BindingParam("categoria") String categoria) {
		try {
			if (filtroSelecionado == null && comboCategorias != null) {
				comboCategorias.setSelectedItem(null);
				throw new Exception("Selecione o filtro antes de selecionar a categoria.");
			}
			participantesInscritos = new ArrayList<ParticipanteInscrito>();
			destinatarios = new ArrayList<DestinatarioUtil>();
			eventos = eventoFacade.findEventosByCategoria(categoria);
			eventoSelecionado = null;
			comboEventos.setSelectedItem(null);
			atividadeSelecionada = null;
			atividades = null;
			comboAtividades.setSelectedItem(null);
			countVagas();
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarListaPorEvento() {
		try {
			if (filtroSelecionado != null && filtroSelecionado.equals("Evento")) {
				destinatarios = new ArrayList<DestinatarioUtil>();
				if (eventoSelecionado.getCategoria().equals("PREVISTO")) {
					participantesInteressados = participanteInteresseFacade
							.findListaDeInteresseByEvento(eventoSelecionado);
					for (ParticipanteInteresse pi : participantesInteressados) {
						DestinatarioUtil destinatario = new DestinatarioUtil(null, pi, pi.getParticipante(), true);
						destinatarios.add(destinatario);
					}
				} else {
					participantesInscritos = participanteInscritoFacade.findParticipantesByEvento(eventoSelecionado);
					for (ParticipanteInscrito pi : participantesInscritos) {
						DestinatarioUtil destinatario = new DestinatarioUtil(pi, null, pi.getParticipante(), true);
						destinatarios.add(destinatario);
					}
				}
				destinatariosSelecionados = destinatarios.size();
			} else {
				participantesInteressados = new ArrayList<ParticipanteInteresse>();
				participantesInscritos = new ArrayList<ParticipanteInscrito>();
				atividades = atividadeFacade.findAtividadesByEvento(eventoSelecionado);
				atividadeSelecionada = null;
				comboAtividades.setSelectedItem(null);
				countVagas();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarListaPorAtividade(@BindingParam("listaDeInteresse") boolean listaDeInteresse) {
		try {
			if (filtroSelecionado != null && filtroSelecionado.equals("Atividade")) {
				destinatarios = new ArrayList<DestinatarioUtil>();
				if (eventoSelecionado.getCategoria().equals("PREVISTO")) {
					participantesInteressados = participanteInteresseFacade
							.findListaDeInteresseByAtividade(atividadeSelecionada);
					for (ParticipanteInteresse pi : participantesInteressados) {
						DestinatarioUtil destinatario = new DestinatarioUtil(null, pi, pi.getParticipante(), true);
						destinatarios.add(destinatario);
					}
				} else {
					participantesInscritos = participanteInscritoFacade
							.findParticipantesByAtividade(atividadeSelecionada);
					for (ParticipanteInscrito pi : participantesInscritos) {
						DestinatarioUtil destinatario = new DestinatarioUtil(pi, null, pi.getParticipante(), true);
						destinatarios.add(destinatario);
					}
				}
				destinatariosSelecionados = destinatarios.size();
			} else {
				if (listaDeInteresse) {
					participantesInteressados = participanteInteresseFacade
							.findListaDeInteresseByAtividade(atividadeSelecionada);
				} else {
					countVagas();
					participantesInscritos = participanteInscritoFacade
							.findParticipantesByAtividade(atividadeSelecionada);
				}
				participantesComAlteracao = new ArrayList<ParticipanteInscrito>();
				participantesSemAlteracao = participanteInscritoFacade
						.findParticipantesSemAlteracaoByAtividade(atividadeSelecionada);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void atualizarLista() {
		try {
			if (atividadeSelecionada != null) {
				filtroParticipante = new Filtro();
				countVagas();
				participantesInscritos = participanteInscritoFacade.findParticipantesByAtividade(atividadeSelecionada);
				participantesComAlteracao = new ArrayList<ParticipanteInscrito>();
				participantesSemAlteracao = participanteInscritoFacade
						.findParticipantesSemAlteracaoByAtividade(atividadeSelecionada);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalExcluirListaDeInteresse(
			@BindingParam("participanteInteresse") ParticipanteInteresse participanteInteresse,
			@BindingParam("visible") Boolean visible) {
		try {
			if (visible) {
				this.participanteInteresse = participanteInteresse;
				modalExcluirListaDeInteresse.setVisible(visible);
			} else {
				modalExcluirListaDeInteresse.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void excluirListaDeInteresse() {
		try {
			participanteInteresseFacade.delete(participanteInteresse.getParticipanteInteresseId());
			carregarListaPorAtividade(true);
			abrirModalExcluirListaDeInteresse(null, false);
			Clients.showNotification("Interesse excluído com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
					3000, true);
		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro inesperado ao excluir esse interesse. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void aprovarInscricao(@BindingParam("participanteInscrito") ParticipanteInscrito participanteInscrito) {
		try {
			if (vagasDisponiveis == 0) {
				throw new Exception("Não há mais vagas disponíveis para essa atividade. "
						+ "Aumente o número de vagas ou remova alguma inscrição para dar lugar a essa.");
			} else {
				if (atividadeSelecionada.getVagasServidor() != null) {
					if (participanteInscrito.getParticipante().getTipoParticipanteStr().equals("SOCIEDADE")) {
						if (vagasSociedade == 0) {
							throw new Exception(
									"Não há mais vagas disponíveis para essa atividade para o público SOCIEDADE. "
											+ "Aumente o número de vagas ou remova alguma inscrição para dar lugar a essa.");
						} else {
							vagasSociedade--;
						}
					} else if (participanteInscrito.getParticipante().getTipoParticipanteStr()
							.equals("SERVIDOR DO TCE-MA")) {
						if (vagasServidor == 0) {
							throw new Exception("Não há mais vagas disponíveis para essa atividade para o público "
									+ "SERVIDOR DO TCE-MA. Aumente o número de vagas ou remova alguma inscrição "
									+ "para dar lugar a essa.");
						} else {
							vagasServidor--;
						}
					} else {
						if (vagasJurisdicionado == 0) {
							throw new Exception("Não há mais vagas disponíveis para essa atividade para o público "
									+ "JURISDICIONADO. Aumente o número de vagas ou remova alguma inscrição para "
									+ "dar lugar a essa.");
						} else {
							vagasJurisdicionado--;
						}
					}
				}
				vagasDisponiveis--;
				inscricoesAprovadas++;
				participanteInscrito.setFrequencia(0.0);
				participanteInscrito.setInscricaoAprovada(true);
				participanteInscrito.setCertificadoDisponivel(false);
				participanteInscrito.setAvaliacaoPreenchida(false);
				if (!participantesComAlteracao.contains(participanteInscrito)) {
					participantesComAlteracao.add(participanteInscrito);
				}
				participantesSemAlteracao.remove(participanteInscrito);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 4000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void desaprovarInscricao(@BindingParam("participanteInscrito") ParticipanteInscrito participanteInscrito) {
		try {
			if (vagasDisponiveis < vagasTotal && participanteInscrito.getInscricaoAprovada() != null) {
				if (atividadeSelecionada.getVagasServidor() != null) {
					if (participanteInscrito.getParticipante().getTipoParticipanteStr().equals("SOCIEDADE")) {
						vagasSociedade++;
					} else if (participanteInscrito.getParticipante().getTipoParticipanteStr()
							.equals("SERVIDOR DO TCE-MA")) {
						vagasServidor++;
					} else {
						vagasJurisdicionado++;
					}
				}
				vagasDisponiveis++;
				inscricoesAprovadas--;
			}
			participanteInscrito.setFrequencia(null);
			participanteInscrito.setInscricaoAprovada(false);
			participanteInscrito.setCertificadoDisponivel(null);
			participanteInscrito.setAvaliacaoPreenchida(null);
			if (!participantesComAlteracao.contains(participanteInscrito)) {
				participantesComAlteracao.add(participanteInscrito);
			}
			participantesSemAlteracao.remove(participanteInscrito);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void aprovarInscricoes() {
		try {
			if (vagasDisponiveis == 0) {
				throw new Exception("Não há mais vagas disponíveis para essa atividade.");
			} else {
				int cont = 0;
				for (ParticipanteInscrito pi : participantesInscritos) {
					if (vagasDisponiveis == 0) {
						throw new Exception("O número de inscrições ultrapassou a quantidade de vagas. "
								+ "Foram confirmadas apenas as " + cont + " inscrições disponíveis "
								+ "nessa atividade.");
					} else {
						if (participantesSemAlteracao.contains(pi)) {
							cont++;
							vagasDisponiveis--;
							inscricoesAprovadas++;
							pi.setFrequencia(0.0);
							pi.setInscricaoAprovada(true);
							pi.setCertificadoDisponivel(false);
							pi.setAvaliacaoPreenchida(false);
							if (!participantesComAlteracao.contains(pi)) {
								participantesComAlteracao.add(pi);
							}
							participantesSemAlteracao.remove(pi);
						}
					}
				}
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 5000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaAlteracoes(@BindingParam("visible") Boolean visible) {
		try {
			modalConfirmacao.setVisible(visible);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void confirmarAlteracoes() {
		try {
			boolean state;
			for (ParticipanteInscrito pi : participantesComAlteracao) {
				if (participanteInscritoFacade.update(pi) != null) {
					if (pi.getInscricaoAprovada() != null) {
						if (pi.getInscricaoAprovada()) {
							atividadeSelecionada
									.setDiasAtividade(diasAtividadeFacade.findDiasByAtividade(atividadeSelecionada));
							for (DiasAtividade da : atividadeSelecionada.getDiasAtividade()) {
								// verificar se o participante ja foi aprovado anteriormente para evitar
								// duplicatas na frequencia
								state = verificarParticipanteEDataNaFrequencia(da, pi);
								if (!state) {
									Frequencia frequencia = new Frequencia();
									frequencia.setDiasAtividade(da);
									frequencia.setParticipanteInscrito(pi);
									frequencia.setPresenca(false);

									if (frequenciaFacade.include(frequencia) == null) {
										// Se a inclusão de alguma frequência falhar,
										// remove as anteriores já inclusas
										Collection<Frequencia> frequencias = frequenciaFacade
												.findFrequenciasByParticipanteInscrito(pi);
										for (Frequencia f : frequencias) {
											frequenciaFacade.delete(f.getFrequenciaId());
										}
										// E também anula a inscrição já aprovada
										pi.setFrequencia(null);
										pi.setInscricaoAprovada(null);
										pi.setCertificadoDisponivel(null);
										participanteInscritoFacade.update(pi);
										break;
									}
								}
							}
						} else {
							// Se uma inscrição previamente aprovada for desaprovada,
							// apagar registros de frequência
							Collection<Frequencia> frequencias = frequenciaFacade
									.findFrequenciasByParticipanteInscrito(pi);
							if (!frequencias.isEmpty()) {
								for (Frequencia f : frequencias) {
									frequenciaFacade.delete(f.getFrequenciaId());
								}
							}
						}
					}
				}
			}

			ArrayList<ParticipanteInscrito> destinatarios = new ArrayList<ParticipanteInscrito>();
			destinatarios.addAll(participantesComAlteracao);
			new LongOperation() {
				@Override
				protected void execute() throws InterruptedException {
					emailSOPHIA.emailGerenciarInscricoes(destinatarios, " ");
				}
			}.start();

			participantesComAlteracao = new ArrayList<ParticipanteInscrito>();
			countVagas();
			modalConfirmacao.setVisible(false);
			Clients.showNotification("Alterações confirmadas com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
					3000, true);

		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro inesperado ao confirmar as alterações. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void aplicarFiltro(@BindingParam("listaDeInteresse") boolean listaDeInteresse) {
		try {
			if (comboAtividades != null) {
				if (atividadeSelecionada != null) {
					if (listaDeInteresse) {
						participantesInteressados = participanteInteresseFacade.findListaDeInteresseByNomeAndAtividade(
								filtroParticipante.getFiltro1(), atividadeSelecionada);
					} else {
						participantesInscritos = participanteInscritoFacade
								.findParticipantesInscritosByNomeAndAtividade(filtroParticipante.getFiltro1(),
										atividadeSelecionada);
					}
				}
			} else {
				if ((filtroParticipante.getFiltro1() != null && !filtroParticipante.getFiltro1().trim().equals(""))
						|| (filtroParticipante.getFiltro2() != null
								&& !filtroParticipante.getFiltro2().trim().equals(""))) {
					participantes = pessoaFacade.findPessoasByFiltro(filtroParticipante);
				} else {
					participantes = new ArrayList<Pessoa>();
				}
			}
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar busca por filtro. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void aplicarFiltroEmEnvioDeMensagens() {

		try {
			if ((filtroParticipante.getFiltro1() != null && !filtroParticipante.getFiltro1().trim().equals(""))) {
				destinatarios = new ArrayList<DestinatarioUtil>();
				if (filtroSelecionado != null && filtroSelecionado.equals("Evento")) {
					// Usuário seleciona o filtro evento e escolhe um evento
					if (eventoSelecionado != null) {
						if (eventoSelecionado.getCategoria().equals("PREVISTO")) {
							participantesInteressados = participanteInteresseFacade
									.findListaDeInteresseByEventoAndFiltro(eventoSelecionado, filtroParticipante);
							for (ParticipanteInteresse pi : participantesInteressados) {
								DestinatarioUtil destinatario = new DestinatarioUtil(null, pi, pi.getParticipante(),
										true);
								destinatarios.add(destinatario);
							}
						} else {
							participantesInscritos = participanteInscritoFacade
									.findParticipantesByEventoAndFiltro(eventoSelecionado, filtroParticipante);
							for (ParticipanteInscrito pi : participantesInscritos) {
								DestinatarioUtil destinatario = new DestinatarioUtil(pi, null, pi.getParticipante(),
										true);
								destinatarios.add(destinatario);
							}
						}

					} else {
						// Usuário seleciona o filtro evento, mas não escolhe um evento
						Clients.showNotification("Selecione o evento acima.", Clients.NOTIFICATION_TYPE_WARNING, null,
								null, 3000, true);

					}
				} else if (filtroSelecionado != null && filtroSelecionado.equals("Atividade")) {
					// Usuário seleciona o filtro atividade e escolhe uma atividade
					if (atividadeSelecionada != null) {
						if (eventoSelecionado.getCategoria().equals("PREVISTO")) {
							participantesInteressados = participanteInteresseFacade
									.findListaDeInteresseByNomeAndAtividade(filtroParticipante.getFiltro1(),
											atividadeSelecionada);
							for (ParticipanteInteresse pi : participantesInteressados) {
								DestinatarioUtil destinatario = new DestinatarioUtil(null, pi, pi.getParticipante(),
										true);
								destinatarios.add(destinatario);
							}
						} else {
							participantesInscritos = participanteInscritoFacade
									.findParticipantesInscritosByNomeAndAtividade(filtroParticipante.getFiltro1(),
											atividadeSelecionada);
							for (ParticipanteInscrito pi : participantesInscritos) {
								DestinatarioUtil destinatario = new DestinatarioUtil(pi, null, pi.getParticipante(),
										true);
								destinatarios.add(destinatario);
							}
						}
					} else {
						// Usuário seleciona o filtro atividade, mas não escolhe uma atividade
						Clients.showNotification("Selecione a atividade acima.", Clients.NOTIFICATION_TYPE_WARNING,
								null, null, 3000, true);
					}
				} else if (filtroSelecionado != null && filtroSelecionado.equals("Participantes")) {
					destinatarios = new ArrayList<DestinatarioUtil>();

					if (filtroTipoParticipante != null) {
						if (this.filtroTipoParticipante.equals("Todos os participantes")) {
							participantes = pessoaFacade.findPessoasByFiltro(filtroParticipante);
						}
						for (Pessoa p : participantes) {
							DestinatarioUtil destinatario = new DestinatarioUtil(null, null, p, true);
							destinatarios.add(destinatario);
						}
					} else {
						// Usuário seleciona o filtro Participantes, mas não escolhe o tipo
						Clients.showNotification("Selecione o tipo de Participante acima.",
								Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
					}

				} else {
					Clients.showNotification(
							"Erro ao realizar busca por filtro. Selecione o tipo de filtro acima (Evento, Atividade ou Participantes).",
							Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
				}
			} else if (eventoSelecionado != null || atividadeSelecionada != null || filtroTipoParticipante != null) {

				if (filtroSelecionado.equals("Evento")) {
					carregarListaPorEvento();
				} else if (filtroSelecionado.equals("Atividade")) {
					carregarListaPorAtividade(false);
				} else if (filtroSelecionado != null && filtroSelecionado.equals("Participantes")) {
					carregarParticipantes(filtroTipoParticipante, true);
				} else {
					destinatarios = new ArrayList<DestinatarioUtil>();
				}

			}
			// Quantidade de resultados encontrados
			destinatariosSelecionados = destinatarios.size();

		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar busca por filtro. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void limparFiltro() {
		if (!filtroParticipante.getFiltro1().isEmpty() || !filtroParticipante.getFiltro2().isEmpty()) {
			filtroParticipante.limparFiltro();
			participantes = new ArrayList<Pessoa>();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarListaPorEstado() {
		try {
			municipios = municipioFacade.findMunicipiosByEstado(participanteSelecionado.getEstado().getEstadoId());
			participanteSelecionado.setMunicipio(null);
			if (comboMunicipios != null) {
				comboMunicipios.setSelectedItem(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalExcluirInscricao(
			@BindingParam("participanteInscrito") ParticipanteInscrito participanteInscrito,
			@BindingParam("visible") Boolean visible) {
		try {
			if (visible) {
				this.participanteInscrito = participanteInscrito;
				modalExcluirInscricao.setVisible(visible);
			} else {
				modalExcluirInscricao.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void excluirInscricao() {
		try {
			participanteInscritoFacade.delete(participanteInscrito.getParticipanteInscritoId());
			countVagas();
			carregarListaPorAtividade(false);
			modalExcluirInscricao.setVisible(false);
			Clients.showNotification("Inscrição excluída com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
					3000, true);
		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro inesperado ao excluir essa inscrição. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalAtualizarParticipante(@BindingParam("participante") Pessoa participante,
			@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				this.participanteSelecionado = participante;
				this.alterarEstadoVisible = false;
				comboEstadosModal.setSelectedItem(null);
				comboMunicipios.setSelectedItem(null);
				modalAtualizarParticipante.setVisible(visible);
			} else {
				if (!filtroParticipante.getFiltro1().isEmpty() || !filtroParticipante.getFiltro2().isEmpty()) {
					participantes = pessoaFacade.findPessoasByFiltro(filtroParticipante);
				} else {
					participantes = new ArrayList<Pessoa>();
				}
				modalAtualizarParticipante.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void exibirOpcoesEditarEstado() {
		alterarEstadoVisible = true;
		participanteSelecionado.setEstado(null);
		participanteSelecionado.setMunicipio(null);
		comboEstadosModal.setSelectedItem(null);
		comboMunicipios.setSelectedItem(null);
		municipios = null;
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaAtualizarParticipante(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				consertaDados(participanteSelecionado);
				if ((participanteSelecionado.getNome() == null || participanteSelecionado.getNome().equals(""))
						|| participanteSelecionado.getCpf() == null || participanteSelecionado.getSexo() == null
						|| (participanteSelecionado.getLogradouro() == null
								|| participanteSelecionado.getLogradouro().equals(""))
						|| (participanteSelecionado.getNumero() == null
								|| participanteSelecionado.getNumero().equals(""))
						|| participanteSelecionado.getEstado() == null || participanteSelecionado.getMunicipio() == null
						|| (participanteSelecionado.getBairro() == null
								|| participanteSelecionado.getBairro().equals(""))
						|| participanteSelecionado.getCep() == null || participanteSelecionado.getDtNascimento() == null
						|| (participanteSelecionado.getEmail() == null
								|| participanteSelecionado.getEmail().equals(""))) {
					throw new Exception("Todos os campos precisam ser preechidos corretamente.");
				} else if (participanteSelecionado.getTelCelular() == null
						&& participanteSelecionado.getTelFixo() == null
						&& participanteSelecionado.getTelComercial() == null) {
					throw new Exception("Preencha pelo menos um telefone para contato.");
				} else if (!emailValidator.isValid(participanteSelecionado.getEmail(), null)) {
					throw new Exception("Insira um endereço de e-mail válido.");
				} else if (pessoaFacade.findByPrimaryKeyAndCpf(participanteSelecionado.getPessoaId(),
						participanteSelecionado.getCpf().replaceAll("\\D+", "")) != null) {
					throw new Exception("CPF já cadastrado para outro participante.");
				} else if (pessoaFacade.findByPrimaryKeyAndEmail(participanteSelecionado.getPessoaId(),
						participanteSelecionado.getEmail()) != null) {
					throw new Exception("E-mail já cadastrado para outro participante.");
				} else if (participanteSelecionado.getTipoPessoa().equals("2")
						&& participanteSelecionado.getServidorMatricula() == null) {
					throw new Exception("Insira a matrícula de servidor.");
				} else if (participanteSelecionado.getTipoPessoa().equals("2")
						&& pessoaFacade.findByMatricula(participanteSelecionado.getServidorMatricula()) != null) {
					throw new Exception("Matrícula de servidor já cadastrada para outro participante.");
				} else if (participanteSelecionado.getTipoPessoa().equals("2") && usuarioSegFacade
						.findByMatricula(Integer.parseInt(participanteSelecionado.getServidorMatricula())) == null) {
					throw new Exception("Matrícula inserida é inválida.");
				} else {
					modalConfirmacao.setVisible(visible);
				}
			} else {
				modalConfirmacao.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void atualizarParticipante() {
		try {
			if (!participanteSelecionado.getTipoPessoa().equals("2")) {
				participanteSelecionado.setServidorMatricula(null);
			}
			if (pessoaFacade.update(participanteSelecionado) != null) {
				modalConfirmacao.setVisible(false);
				modalAtualizarParticipante.setVisible(false);
				if (!filtroParticipante.getFiltro1().isEmpty() || !filtroParticipante.getFiltro2().isEmpty()) {
					participantes = pessoaFacade.findPessoasByFiltro(filtroParticipante);
				} else {
					participantes = new ArrayList<Pessoa>();
				}
				Clients.showNotification("Participante atualizado com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null,
						null, 3000, true);
			} else {
				throw new Exception(
						"Ocorreu um erro inesperado ao atualizar esse participante. Tente novamente mais tarde.");
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void selecionaFiltro(@BindingParam("filtro") String filtro) {
		try {
			comboCategorias.setSelectedItem(null);
			comboEventos.setSelectedItem(null);
			eventoSelecionado = null;
			comboAtividades.setSelectedItem(null);
			atividadeSelecionada = null;
			comboParticipantes.setSelectedItem(null);

			destinatarios = new ArrayList<DestinatarioUtil>();
			filtroSelecionado = filtro;
			if (filtro.equals("Evento")) {
				vlayoutCategorias.setVisible(true);
				vlayoutEventos.setVisible(true);
				vlayoutAtividades.setVisible(false);
				vlayoutParticipantes.setVisible(false);
				vlayoutFiltroNome.setVisible(true);
			} else if (filtro.equals("Atividade")) {
				vlayoutCategorias.setVisible(true);
				vlayoutEventos.setVisible(true);
				vlayoutAtividades.setVisible(true);
				vlayoutParticipantes.setVisible(false);
				vlayoutFiltroNome.setVisible(true);
			} else if (filtro.equals("Participantes")) {
				vlayoutCategorias.setVisible(false);
				vlayoutEventos.setVisible(false);
				vlayoutAtividades.setVisible(false);
				vlayoutParticipantes.setVisible(true);
			}
			countVagas();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void selecionarDestinatario(@BindingParam("destinatario") DestinatarioUtil destinatario) {
		try {
			destinatario.setSelecionado(!destinatario.getSelecionado());
			if (destinatario.getSelecionado()) {
				destinatariosSelecionados++;
			} else {
				destinatariosSelecionados--;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void selecionarApenasInscricaoAprovada() {
		try {
			destinatariosSelecionados = 0;
			for (DestinatarioUtil d : destinatarios) {
				if (d.getParticipanteInscrito().getInscricaoAprovada() != null
						&& d.getParticipanteInscrito().getInscricaoAprovada()) {
					d.setSelecionado(true);
					destinatariosSelecionados++;
				} else {
					d.setSelecionado(false);
				}
			}
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void selecionarTodosParticipantes() {
		try {
			for (DestinatarioUtil d : destinatarios) {
				d.setSelecionado(status);
			}
			if (status) {
				destinatariosSelecionados = destinatarios.size();
			} else {
				destinatariosSelecionados = 0;
			}
			status = !status;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarParticipantes(@BindingParam("filtro") String filtro,
			@BindingParam("envioMensagem") Boolean envioMensagem) {
		try {
			if (envioMensagem) {

				this.filtroTipoParticipante = filtro;
				if (filtro.equals("Todos os participantes")) {
					vlayoutFiltroNome.setVisible(true);
					participantes = pessoaFacade.findAll();
				} else if (filtro.equals("Todos SERVIDOR DO TCE-MA e TERCEIRIZADOS")) {
					vlayoutFiltroNome.setVisible(false);
					participantes = pessoaFacade.findServidoresAndTerceirizados();
				} else if (filtro.equals("Todos JURISDICIONADO")) {
					vlayoutFiltroNome.setVisible(false);
					participantes = pessoaFacade.findByTipoPessoa("3");
				} else if (filtro.equals("Todos SOCIEDADE")) {
					vlayoutFiltroNome.setVisible(false);
					participantes = pessoaFacade.findByTipoPessoa("1");
				}
				destinatarios = new ArrayList<DestinatarioUtil>();
				for (Pessoa p : participantes) {
					DestinatarioUtil destinatario = new DestinatarioUtil(null, null, p, true);
					destinatarios.add(destinatario);
				}

				destinatariosSelecionados = destinatarios.size();
			} else {
				limparFiltro();
				tipoPessoaSelecionado = filtro;
				participantes = pessoaFacade.findByTipoPessoa(tipoPessoaSelecionado);
			}
		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro ao carregar a lista de participantes. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaEnviarMensagem(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				if (textoMensagem == null || textoMensagem.trim().equals("")) {
					throw new Exception("Escreva uma para mensagem para ser enviada.");
				} else {
					modalConfirmacao.setVisible(visible);
				}
			} else {
				modalConfirmacao.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void enviarMensagem() {
		try {
			ArrayList<DestinatarioUtil> destinatarios = new ArrayList<>();
			for (DestinatarioUtil du : this.destinatarios) {
				if (du.getSelecionado()) {
					destinatarios.add(du);
				}
			}

			String textoMensagem = this.textoMensagem;
			new LongOperation() {
				@Override
				protected void execute() throws InterruptedException {
					emailSOPHIA.emailMensagem(filtroSelecionado, textoMensagem, destinatarios, " ");
				}
			}.start();

			this.textoMensagem = null;
			modalConfirmacao.setVisible(false);
			Clients.showNotification(
					"Mensagem(ns) enviada(s) com sucesso! OBS: Levará algum tempo até que todos os destinatários recebam a mensagem.",
					Clients.NOTIFICATION_TYPE_INFO, null, null, 5000, true);
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar envio de mensagem. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalListarCapacitacoes(@BindingParam("participante") Pessoa participante,
			@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				this.participanteSelecionado = participante;
				capacitacoes = capacitacaoServidorFacade.findCapacitacoesByParticipante(participante);
				if (capacitacoes.isEmpty()) {
					throw new Exception("Não foram encontradas capacitações registradas para esse servidor.");
				}
				modalListarCapacitacoes.setVisible(visible);
			} else {
				modalListarCapacitacoes.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaRemoverCapacitacao(
			@BindingParam("capacitacaoServidor") CapacitacaoServidor capacitacaoServidor,
			@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				this.capacitacaoServidor = capacitacaoServidor;
				modalConfirmaRemoverCapacitacao.setVisible(visible);
			} else {
				modalConfirmaRemoverCapacitacao.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void removerCapacitacao() {
		try {
			capacitacoes.remove(capacitacaoServidor);
			capacitacaoServidorFacade.delete(capacitacaoServidor.getCapacitacaoServidorId());
			capacitacaoServidor = new CapacitacaoServidor();
			modalConfirmaRemoverCapacitacao.setVisible(false);
			Clients.showNotification("Capacitação removida com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
					3000, true);
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar remoção de capacitação. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalRegistrarCapacitacao(@BindingParam("participante") Pessoa participante,
			@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				capacitacaoServidor = new CapacitacaoServidor();
				capacitacaoServidor.setDtCadastro(new Date());
				capacitacaoServidor.setParticipante(participante);
				modalRegistrarCapacitacao.setVisible(visible);
			} else {
				modalRegistrarCapacitacao.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaRegistrarCapacitacao(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				if ((capacitacaoServidor.getNomeCapacitacao() == null
						|| capacitacaoServidor.getNomeCapacitacao().equals(""))
						|| (capacitacaoServidor.getInstituicaoProvedora() == null
								|| capacitacaoServidor.getInstituicaoProvedora().equals(""))
						|| capacitacaoServidor.getDataInicio() == null || capacitacaoServidor.getDataFim() == null
						|| capacitacaoServidor.getCargaHoraria() == null) {
					throw new Exception("Todos os campos precisam ser preechidos corretamente.");
				} else {
					modalConfirmacao.setVisible(visible);
				}
			} else {
				modalConfirmacao.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void registrarCapacitacao() {
		try {
			capacitacaoServidorFacade.include(capacitacaoServidor);
			modalRegistrarCapacitacao.setVisible(false);
			modalConfirmacao.setVisible(false);
			Clients.showNotification("Capacitação registrada com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
					3000, true);
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar registro de capacitação. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalRelatorioMensal(@BindingParam("mes") String mes, @BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				if ((mesRelatorio == null || mesRelatorio.equals(""))
						|| (anoRelatorio == null || anoRelatorio.equals(""))) {
					throw new Exception("Preencha mês e ano corretamente.");
				} else {
					String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
					participantesInscritos = participanteInscritoFacade
							.findParticipantesInscritosByPeriodo(mesRelatorio, anoRelatorio);
					capacitacoes = capacitacaoServidorFacade.findCapacitacoesByPeriodo(mesRelatorio, anoRelatorio);
					if (participantesInscritos.isEmpty() && capacitacoes.isEmpty()) {
						throw new Exception("Não foram encontradas capacitações no período especificado.");
					}
					List<RelatorioMensalCapacitacoesVO> listaRelatorioMensal = RelatorioMensalCapacitacoesVO
							.preencheLista(participantesInscritos, capacitacoes);

					Map<String, Object> parameters = new HashMap<String, Object>();
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					parameters.put("DATA_CRIACAO", df.format(new Date()));
					parameters.put("MES_ANO", mes + " / " + anoRelatorio);
					parameters.put("IMAGE_DIR", path + "/imagens");

					String filename = "relatorio_mensal_capacitacoes.jasper";
					lista = Report.getReportRelatorioMensal(path + "/jasper", listaRelatorioMensal, filename,
							parameters);
					iframeRelatorioMensal.setContent(new AMedia(lista, "application/pdf", null));
					modalRelatorioMensal.setVisible(visible);
				}
			} else {
				lista = null;
				iframeRelatorioMensal.setContent(null);
				modalRelatorioMensal.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

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

	private void consertaDados(Pessoa participante) {
		if (!testaQuantNumeros(participante.getCpf(), 11)) {
			participante.setCpf(null);
		}

		if (!testaQuantNumeros(participante.getTelFixo(), 10)) {
			participante.setTelFixo(null);
		}

		if (!testaQuantNumeros(participante.getTelCelular(), 11)) {
			participante.setTelCelular(null);
		}

		if (!testaQuantNumeros(participante.getTelComercial(), 10)) {
			participante.setTelComercial(null);
		}

		if (!testaQuantNumeros(participante.getCep(), 8)) {
			participante.setCep(null);
		}

		if (participante.getComplemento() != null && participante.getComplemento().trim().equals("")) {
			participante.setComplemento(null);
		}
	}

	boolean verificarParticipanteEDataNaFrequencia(DiasAtividade da, ParticipanteInscrito pi) {
		Collection<Frequencia> frequencias = frequenciaFacade.findFrequenciasByDiaAndParticipanteInscrito(da, pi);
		if (frequencias.isEmpty()) {
			return false;
		}
		return true;
	}

	public Evento getEventoSelecionado() {
		return eventoSelecionado;
	}

	public void setEventoSelecionado(Evento eventoSelecionado) {
		this.eventoSelecionado = eventoSelecionado;
	}

	public Atividade getAtividadeSelecionada() {
		return atividadeSelecionada;
	}

	public void setAtividadeSelecionada(Atividade atividadeSelecionada) {
		this.atividadeSelecionada = atividadeSelecionada;
	}

	public Pessoa getParticipanteSelecionado() {
		return participanteSelecionado;
	}

	public void setParticipanteSelecionado(Pessoa participanteSelecionado) {
		this.participanteSelecionado = participanteSelecionado;
	}

	public CapacitacaoServidor getCapacitacaoServidor() {
		return capacitacaoServidor;
	}

	public void setCapacitacaoServidor(CapacitacaoServidor capacitacaoServidor) {
		this.capacitacaoServidor = capacitacaoServidor;
	}

	public Collection<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(Collection<Evento> eventos) {
		this.eventos = eventos;
	}

	public Collection<Atividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(Collection<Atividade> atividades) {
		this.atividades = atividades;
	}

	public Collection<Pessoa> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(Collection<Pessoa> participantes) {
		this.participantes = participantes;
	}

	public Collection<ParticipanteInteresse> getParticipantesInteressados() {
		return participantesInteressados;
	}

	public void setParticipantesInteressados(Collection<ParticipanteInteresse> participantesInteressados) {
		this.participantesInteressados = participantesInteressados;
	}

	public Collection<ParticipanteInscrito> getParticipantesInscritos() {
		return participantesInscritos;
	}

	public void setParticipantesInscritos(Collection<ParticipanteInscrito> participantesInscritos) {
		this.participantesInscritos = participantesInscritos;
	}

	public Collection<CapacitacaoServidor> getCapacitacoes() {
		return capacitacoes;
	}

	public void setCapacitacoes(Collection<CapacitacaoServidor> capacitacoes) {
		this.capacitacoes = capacitacoes;
	}

	public Collection<DestinatarioUtil> getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(Collection<DestinatarioUtil> destinatarios) {
		this.destinatarios = destinatarios;
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

	public boolean isAlterarEstadoVisible() {
		return alterarEstadoVisible;
	}

	public void setAlterarEstadoVisible(boolean alterarEstadoVisible) {
		this.alterarEstadoVisible = alterarEstadoVisible;
	}

	public String getTextoMensagem() {
		return textoMensagem;
	}

	public void setTextoMensagem(String textoEmail) {
		this.textoMensagem = textoEmail;
	}

	public String getMesRelatorio() {
		return mesRelatorio;
	}

	public void setMesRelatorio(String mesRelatorio) {
		this.mesRelatorio = mesRelatorio;
	}

	public String getAnoRelatorio() {
		return anoRelatorio;
	}

	public void setAnoRelatorio(String anoRelatorio) {
		this.anoRelatorio = anoRelatorio;
	}

	public int getVagasSociedade() {
		return vagasSociedade;
	}

	public void setVagasSociedade(int vagasSociedade) {
		this.vagasSociedade = vagasSociedade;
	}

	public int getVagasServidor() {
		return vagasServidor;
	}

	public void setVagasServidor(int vagasServidor) {
		this.vagasServidor = vagasServidor;
	}

	public int getVagasJurisdicionado() {
		return vagasJurisdicionado;
	}

	public void setVagasJurisdicionado(int vagasJurisdicionado) {
		this.vagasJurisdicionado = vagasJurisdicionado;
	}

	public Filtro getFiltroParticipante() {
		return filtroParticipante;
	}

	public void setFiltroParticipante(Filtro filtroParticipante) {
		this.filtroParticipante = filtroParticipante;
	}

	public String getFiltroTipoParticipante() {
		return filtroTipoParticipante;
	}

	public void setFiltroTipoParticipante(String filtroTipoParticipante) {
		this.filtroTipoParticipante = filtroTipoParticipante;
	}

	public String getTextoRodape1() {
		return String.format(textoRodape1, participantes.size());
	}

	public String getTextoRodape2() {
		return String.format(textoRodape2, participantesInscritos.size(), inscricoesAprovadas, vagasDisponiveis,
				vagasTotal);
	}

	public String getTextoRodape3() {
		return String.format(textoRodape3, participantesInteressados.size());
	}

	public String getTextoRodape4() {
		return String.format(textoRodape4, destinatarios.size(), destinatariosSelecionados);
	}

}