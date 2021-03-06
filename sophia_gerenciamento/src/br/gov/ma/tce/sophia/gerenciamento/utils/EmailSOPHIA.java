package br.gov.ma.tce.sophia.gerenciamento.utils;

import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaborador;
import br.gov.ma.tce.sophia.ejb.beans.emaillog.EmailLog;
import br.gov.ma.tce.sophia.ejb.beans.emaillog.EmailLogFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.evento.Evento;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;
import br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresse;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;
import br.gov.ma.tce.sophia.ejb.beans.tipocolaborador.TipoColaborador;
import br.gov.ma.tce.tlsClient.SimpleMail;

public class EmailSOPHIA {
	private final String emailSophia = "";
	private final String senhaSophia = "";
	private final String CC = "escex.tce.ma@gmail.com";

	private EmailLog emailLog;

	private EmailLogFacadeBean emailLogFacade;

	public EmailSOPHIA() {
		try {
			InitialContext ctx = new InitialContext();
			emailLogFacade = (EmailLogFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/EmailLogFacadeBean!br.gov.ma.tce.sophia.ejb.beans.emaillog.EmailLogFacadeBean");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public void emailParaTestes() {
		StringBuffer html = new StringBuffer();
		html.append("<!DOCTYPE html>");
		html.append("<html lang=\"pt-br\">");
		html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
		html.append("<tr><td>Prezado(a) Senhor(a), ");
		html.append("<br/>A sua conta foi criada com sucesso no Sistema de Gerenciamento "
				+ "da Escola de Contas (Sophia).<br/>");
		html.append("<b>Voc&ecirc; pode acessar o sistema em:</b> "
				+ "<a href=\"https://www6.tce.ma.gov.br/sophia/login.zul\">"
				+ "www6.tce.ma.gov.br/sophia/login.zul</a><br/>");
		html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
				+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
		html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
		html.append("<tr><td><div align=\"left\">"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
		html.append("</table></body></html>");
		
		new SimpleMail(emailSophia, senhaSophia, "ersn.dudu1@gmail.com", this.CC,
				"(Cadastro Realizado) Sophia - Tribunal de Contas do Estado do Maranhão", " ", html.toString(), " ");
	}

	public void emailConfirmarCadastroColaborador(Pessoa destinatario, String CC) {
		StringBuffer html = new StringBuffer();
		html.append("<!DOCTYPE html>");
		html.append("<html lang=\"pt-br\">");
		html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
		html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getNome());
		html.append("<br/>Seu cadastro de colaborador foi confirmado!<br/>");
		html.append("Futuras atribui&ccedil;&otilde;es a atividades ser&atilde;o avisadas em seu e-mail.<br/>");
		html.append("Caso deseje que seu cadastro de colaborador seja desativado, entre em contato conosco por e-mail "
				+ "<br/>escex.sec@tce.ma.gov.br ou telefone (98) 2016-6178.<br/>");
		html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
				+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
		html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
		html.append("<tr><td><div align=\"left\">"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
		html.append("</table></body></html>");

		try {
			new SimpleMail(emailSophia, senhaSophia, destinatario.getEmail(), "escex.tce.ma@gmail.com",
					"(Cadastro de Colaborador Confirmado) Sophia - Tribunal de Contas do Estado do Maranhão", " ",
					html.toString(), " ");
		} catch (Exception e) {
			emailLog = new EmailLog();
			emailLog.setLog(e.getMessage());
			emailLog.setErroRelacionado("confirma cadastro colaborador");
			emailLog.setEmailDestinatario(destinatario.getEmail());
			emailLogFacade.include(emailLog);
		}

	}

	public void emailDesativarCadastroColaborador(Pessoa destinatario, String CC) {
		StringBuffer html = new StringBuffer();
		html.append("<!DOCTYPE html>");
		html.append("<html lang=\"pt-br\">");
		html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
		html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getNome());
		html.append("<br/>Seu cadastro de colaborador foi desativado.<br/>");
		html.append("Em caso de eventuais d&uacute;vidas, entre em contato conosco por e-mail "
				+ "<br/>escex.sec@tce.ma.gov.br ou telefone (98) 2016-6178.<br/>");
		html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
				+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
		html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
		html.append("<tr><td><div align=\"left\">"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
		html.append("</table></body></html>");

		try {
			new SimpleMail(emailSophia, senhaSophia, destinatario.getEmail(), this.CC,
					"(Cadastro de Colaborador Desativado) Sophia - Tribunal de Contas do Estado do Maranhão", " ",
					html.toString(), " ");
		} catch (Exception e) {
			emailLog = new EmailLog();
			emailLog.setLog(e.getMessage());
			emailLog.setErroRelacionado("desativa cadastro colaborador");
			emailLog.setEmailDestinatario(destinatario.getEmail());
			emailLogFacade.include(emailLog);
		}
	}

	public void emailAtribuirColaborador(Pessoa destinatario, Atividade atividade, TipoColaborador tipoColaborador,
			String CC) {
		StringBuffer html = new StringBuffer();
		html.append("<!DOCTYPE html>");
		html.append("<html lang=\"pt-br\">");
		html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
		html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getNome());
		html.append("<br/>Voc&ecirc; foi atribu&iacute;do a atividade <b>" + atividade.getNome() + "</b>, do evento <b>"
				+ atividade.getEvento().getNome() + "</b> como <b>" + tipoColaborador.getNome() + "</b>.<br/>");
		html.append("Suas colabora&ccedil;&otilde;es podem ser visualizadas em Colaboradores > Minhas "
				+ "atividades como colaborador.<br/>");
		html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
				+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
		html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
		html.append("<tr><td><div align=\"left\">"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
		html.append("</table></body></html>");

		try {
			new SimpleMail(emailSophia, senhaSophia, destinatario.getEmail(), this.CC,
					"(Atribuição de Colaborador) Sophia - Tribunal de Contas do Estado do Maranhão", " ",
					html.toString(), " ");
		} catch (Exception e) {
			emailLog = new EmailLog();
			emailLog.setLog(e.getMessage());
			emailLog.setErroRelacionado("atribui colaborador");
			emailLog.setEmailDestinatario(destinatario.getEmail());
			emailLogFacade.include(emailLog);
		}
	}

	// Não utilizado
	public void emailAprovarInscricao(Pessoa destinatario, Atividade atividade, String CC) {
		StringBuffer html = new StringBuffer();
		html.append("<!DOCTYPE html>");
		html.append("<html lang=\"pt-br\">");
		html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
		html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getNome());
		html.append("<br/>Sua inscri&ccedil;&atilde;o foi aprovada para a atividade <b>" + atividade.getNome()
				+ "</b>, do evento <b>" + atividade.getEvento().getNome() + "</b>.<br/>");
		if (atividade.getModalidade().equals("PRESENCIAL")) {
			html.append("A atividade acontecerá no endereço: <b>" + atividade.getEnderecoStr()
					+ "</b>,<br/> no(s) dia(s): <b>" + atividade.getDataStr() + "</b> e no horário: <b>"
					+ atividade.getHorarioStr() + "</b>.<br/>");
		}
		html.append("<br/>Suas inscri&ccedil;&otilde;es podem ser visualizadas em Eventos > Meus eventos.<br/>");
		html.append("Caso n&atilde;o possa estar presente no(s) dia(s) da atividade, entre em contato com a "
				+ "ESCEX solicitando o cancelamento da sua inscri&ccedil;&atilde;o com <b>no m&aacute;ximo "
				+ "3 (tr&ecirc;s) dias de anteced&ecirc;ncia do dia do in&iacute;cio da atividade.</b><br/>");
		html.append("E-mail: escex.sec@tce.ma.gov.br, telefone (98) 2016-6178.<br/>");
		html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
				+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
		html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
		html.append("<tr><td><div align=\"left\">"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
		html.append("</table></body></html>");
		new SimpleMail(emailSophia, senhaSophia, destinatario.getEmail(), this.CC,
				"(Inscrição Aprovada) Sophia - Tribunal de Contas do Estado do Maranhão", " ", html.toString(), " ");
	}

	// Não utilizado
	public void emailDesaprovarInscricao(Pessoa destinatario, Atividade atividade, String CC) {
		StringBuffer html = new StringBuffer();
		html.append("<!DOCTYPE html>");
		html.append("<html lang=\"pt-br\">");
		html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
		html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getNome());
		html.append("<br/>Infelizmente sua inscri&ccedil;&atilde;o n&atilde;o foi aprovada para a atividade <b>"
				+ atividade.getNome() + "</b>, do evento <b>" + atividade.getEvento().getNome() + "</b>.<br/>");
		html.append("Entre em contato com a ESCEX para obter mais informa&ccedil;&otilde;es.<br/>");
		html.append("E-mail: escex.sec@tce.ma.gov.br, telefone (98) 2016-6178.<br/>");
		html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
				+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
		html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
		html.append("<tr><td><div align=\"left\">"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
		html.append("</table></body></html>");
		new SimpleMail(emailSophia, senhaSophia, destinatario.getEmail(), this.CC,
				"(Inscrição Não Aprovada) Sophia - Tribunal de Contas do Estado do Maranhão", " ", html.toString(),
				" ");
	}

	public void emailGerenciarInscricoes(ArrayList<ParticipanteInscrito> destinatarios, String CC) {
		for (ParticipanteInscrito pi : destinatarios) {
			ParticipanteInscrito destinatario = new ParticipanteInscrito();
			destinatario.setParticipante(pi.getParticipante());
			destinatario.setAtividade(pi.getAtividade());
			destinatario.setInscricaoAprovada(pi.getInscricaoAprovada());

			if (destinatario.getInscricaoAprovada()) {
				StringBuffer html = new StringBuffer();
				html.append("<!DOCTYPE html>");
				html.append("<html lang=\"pt-br\">");
				html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
				html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
				html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getParticipante().getNome());
				html.append("<br/>Sua inscri&ccedil;&atilde;o foi aprovada para a atividade <b>"
						+ destinatario.getAtividade().getNome() + "</b>, do evento <b>"
						+ destinatario.getAtividade().getEvento().getNome() + "</b>.<br/>");
				if (destinatario.getAtividade().getModalidade().equals("PRESENCIAL")) {
					html.append("A atividade acontecerá no endereço: <b>" + destinatario.getAtividade().getEnderecoStr()
							+ "</b>,<br/> no(s) dia(s): <b>" + destinatario.getAtividade().getDataStr()
							+ "</b> e no horário: <b>" + destinatario.getAtividade().getHorarioStr() + "</b>.<br/>");
				}
				html.append(
						"<br/>Suas inscri&ccedil;&otilde;es podem ser visualizadas em Eventos > Meus eventos.<br/>");
				html.append("Caso n&atilde;o possa estar presente no(s) dia(s) da atividade, entre em contato com a "
						+ "ESCEX solicitando o cancelamento da sua inscri&ccedil;&atilde;o com <b>no m&aacute;ximo "
						+ "3 (tr&ecirc;s) dias de anteced&ecirc;ncia do dia do in&iacute;cio da atividade.</b><br/>");
				html.append("E-mail: escex.sec@tce.ma.gov.br, telefone (98) 2016-6178.<br/>");
				html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
						+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
				html.append(
						"<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
				html.append("<tr><td><div align=\"left\">"
						+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
						+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
				html.append("</table></body></html>");

				try {
					new SimpleMail(emailSophia, senhaSophia, destinatario.getParticipante().getEmail(), this.CC,
							"(Inscrição Aprovada) Sophia - Tribunal de Contas do Estado do Maranhão", " ",
							html.toString(), " ");
				} catch (Exception e) {
					emailLog = new EmailLog();
					emailLog.setLog(e.getMessage());
					emailLog.setErroRelacionado("inscrição aprovada");
					emailLog.setEmailDestinatario(destinatario.getParticipante().getEmail());
					emailLogFacade.include(emailLog);
				}
			} else {
				StringBuffer html = new StringBuffer();
				html.append("<!DOCTYPE html>");
				html.append("<html lang=\"pt-br\">");
				html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
				html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
				html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getParticipante().getNome());
				html.append("<br/>Infelizmente sua inscri&ccedil;&atilde;o n&atilde;o foi aprovada para a atividade <b>"
						+ destinatario.getAtividade().getNome() + "</b>, do evento <b>"
						+ destinatario.getAtividade().getEvento().getNome() + "</b>.<br/>");
				html.append("Entre em contato com a ESCEX para obter mais informa&ccedil;&otilde;es.<br/>");
				html.append("E-mail: escex.sec@tce.ma.gov.br, telefone (98) 2016-6178.<br/>");
				html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
						+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
				html.append(
						"<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
				html.append("<tr><td><div align=\"left\">"
						+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
						+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
				html.append("</table></body></html>");

				try {
					new SimpleMail(emailSophia, senhaSophia, destinatario.getParticipante().getEmail(), CC,
							"(Inscrição Não Aprovada) Sophia - Tribunal de Contas do Estado do Maranhão", " ",
							html.toString(), " ");
				} catch (Exception e) {
					emailLog = new EmailLog();
					emailLog.setLog(e.getMessage());
					emailLog.setErroRelacionado("inscrição não aprovada");
					emailLog.setEmailDestinatario(destinatario.getParticipante().getEmail());
					emailLogFacade.include(emailLog);
				}
			}
		}
	}

	public void emailAlterarCategoriaPrevisto(ArrayList<ParticipanteInteresse> destinatarios, String novaCategoria,
			Atividade atividade, String CC) {
		for (ParticipanteInteresse pi : destinatarios) {
			ParticipanteInteresse destinatario = new ParticipanteInteresse();
			destinatario.setParticipante(pi.getParticipante());
			destinatario.setAtividade(pi.getAtividade());

			StringBuffer html = new StringBuffer();
			html.append("<!DOCTYPE html>");
			html.append("<html lang=\"pt-br\">");
			html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
			html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
			html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getParticipante().getNome());
			html.append(
					"<br/>Voc&ecirc; est&aacute; recebendo esse e-mail pois estava presente na lista de interesse da "
							+ "atividade <b>" + atividade.getNome() + "</b>, do evento <b>"
							+ atividade.getEvento().getNome() + "</b>. " + "O evento passou da categoria PREVISTO para "
							+ novaCategoria
							+ " e sua inscri&ccedil;&atilde;o foi automaticamente confirmada e aprovada para a atividade "
							+ "mencionada.<br/>");
			if (atividade.getModalidade().equals("PRESENCIAL")) {
				html.append("A atividade acontecerá no endereço: <b>" + atividade.getEnderecoStr()
						+ "</b>,<br/> no(s) dia(s): <b>" + atividade.getDataStr() + "</b> e no horário: <b>"
						+ atividade.getHorarioStr() + "</b>.<br/>");
			}
			html.append("Caso n&atilde;o possa estar presente no(s) dia(s) da atividade, entre em contato com a "
					+ "ESCEX solicitando o cancelamento da sua inscri&ccedil;&atilde;o com <b>no m&aacute;ximo "
					+ "3 (tr&ecirc;s) dias de anteced&ecirc;ncia do dia do in&iacute;cio da atividade.</b><br/>");
			html.append("E-mail: escex.sec@tce.ma.gov.br, telefone (98) 2016-6178.<br/>");
			html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
					+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
			html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
			html.append("<tr><td><div align=\"left\">"
					+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
					+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
			html.append("</table></body></html>");
			new SimpleMail(emailSophia, senhaSophia, destinatario.getParticipante().getEmail(), this.CC,
					"(Inscrição realizada para evento previsto) Sophia - Tribunal de Contas do Estado do Maranhão", " ",
					html.toString(), " ");
		}
	}

	public void emailAlterarCategoriaAberto(ArrayList<ParticipanteInscrito> destinatarios, Atividade atividade,
			String CC) {
		for (ParticipanteInscrito pi : destinatarios) {
			ParticipanteInscrito destinatario = new ParticipanteInscrito();
			destinatario.setParticipante(pi.getParticipante());
			destinatario.setAtividade(pi.getAtividade());

			StringBuffer html = new StringBuffer();
			html.append("<!DOCTYPE html>");
			html.append("<html lang=\"pt-br\">");
			html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
			html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
			html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getParticipante().getNome());
			html.append("<br/>Voc&ecirc; est&aacute; recebendo esse e-mail pois estava inscrito na atividade <b>"
					+ atividade.getNome() + "</b>, do evento <b>" + atividade.getEvento().getNome() + "</b>. "
					+ "O evento passou da categoria ABERTO para PREVISTO e voc&ecirc; foi colocado automaticamente "
					+ "na lista de interesse da atividade mencionada.<br/>");
			if (atividade.getModalidade().equals("PRESENCIAL")) {
				html.append("A atividade est&aacute; prevista pra acontecer no endereço: <b>"
						+ atividade.getEnderecoStr() + "</b>,<br/> no(s) dia(s): <b>" + atividade.getDataStr()
						+ "</b> e no horário: <b>" + atividade.getHorarioStr() + "</b>.<br/>");
			}
			html.append("Caso deseje sair da lista de interesse dessa atividade, esteja logado no Sophia e acesse "
					+ "Participante > Lista de Interesse e selecione a op&ccedil;&atilde;o &quot;Sair da Lista de "
					+ "Interesse&quot;.<br/>");
			html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
					+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
			html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
			html.append("<tr><td><div align=\"left\">"
					+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
					+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
			html.append("</table></body></html>");
			new SimpleMail(emailSophia, senhaSophia, destinatario.getParticipante().getEmail(), this.CC,
					"(Inserção na lista de interesse) Sophia - Tribunal de Contas do Estado do Maranhão", " ",
					html.toString(), " ");
		}
	}

	public void emailAlterarCategoriaEmAndamento(ArrayList<ParticipanteInscrito> destinatarios, Atividade atividade,
			String CC) {
		for (ParticipanteInscrito pi : destinatarios) {
			ParticipanteInscrito destinatario = new ParticipanteInscrito();
			destinatario.setParticipante(pi.getParticipante());
			destinatario.setAtividade(pi.getAtividade());

			StringBuffer html = new StringBuffer();
			html.append("<!DOCTYPE html>");
			html.append("<html lang=\"pt-br\">");
			html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
			html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
			html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getParticipante().getNome());
			html.append("<br/>Voc&ecirc; est&aacute; recebendo esse e-mail pois estava inscrito na atividade <b>"
					+ atividade.getNome() + "</b>, do evento <b>" + atividade.getEvento().getNome() + "</b>. "
					+ "O evento passou da categoria EM ANDAMENTO para PREVISTO e voc&ecirc; foi colocado automaticamente "
					+ "na lista de interesse da atividade mencionada.<br/>");
			if (atividade.getModalidade().equals("PRESENCIAL")) {
				html.append("A atividade est&aacute; prevista pra acontecer no endereço: <b>"
						+ atividade.getEnderecoStr() + "</b>,<br/> no(s) dia(s): <b>" + atividade.getDataStr()
						+ "</b> e no horário: <b>" + atividade.getHorarioStr() + "</b>.<br/>");
			}
			html.append("Caso deseje sair da lista de interesse dessa atividade, esteja logado no Sophia e acesse "
					+ "Participante > Lista de Interesse e selecione a op&ccedil;&atilde;o &quot;Sair da Lista de "
					+ "Interesse&quot;.<br/>");
			html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
					+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
			html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
			html.append("<tr><td><div align=\"left\">"
					+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
					+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
			html.append("</table></body></html>");
			new SimpleMail(emailSophia, senhaSophia, destinatario.getParticipante().getEmail(), this.CC,
					"(Inserção na lista de interesse) Sophia - Tribunal de Contas do Estado do Maranhão", " ",
					html.toString(), " ");
		}
	}

	public void emailEventoPrevistoCancelado(ArrayList<ParticipanteInteresse> destinatarios, boolean eventoCancelado,
			String CC) {
		for (ParticipanteInteresse pi : destinatarios) {
			ParticipanteInteresse destinatario = new ParticipanteInteresse();
			destinatario.setParticipante(pi.getParticipante());
			destinatario.setAtividade(pi.getAtividade());

			StringBuffer html = new StringBuffer();
			html.append("<!DOCTYPE html>");
			html.append("<html lang=\"pt-br\">");
			html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
			html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
			html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getParticipante().getNome());
			html.append("<br/>Voc&ecirc; est&aacute; recebendo esse e-mail pois estava registrado na "
					+ "lista de interesse da atividade <b>" + destinatario.getAtividade().getNome()
					+ "</b>, do evento <b>" + destinatario.getAtividade().getEvento().getNome() + "</b>.");
			if (eventoCancelado) {
				html.append("Este evento foi cancelado pelo motivo: "
						+ pi.getAtividade().getEvento().getMotivoCancelamento() + ".<br/>");
			} else {
				html.append("Esta atividade foi cancelada pelo motivo: " + pi.getAtividade().getMotivoCancelamento()
						+ ".<br/>");
			}
			html.append("Entre em contato com a ESCEX para obter mais informa&ccedil;&otilde;es.<br/>");
			html.append("E-mail: escex.sec@tce.ma.gov.br, telefone (98) 2016-6178.<br/>");
			html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
					+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
			html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
			html.append("<tr><td><div align=\"left\">"
					+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
					+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
			html.append("</table></body></html>");

			try {
				new SimpleMail(emailSophia, senhaSophia, destinatario.getParticipante().getEmail(), this.CC,
						"(Evento Cancelado) Sophia - Tribunal de Contas do Estado do Maranhão", " ", html.toString(),
						" ");
			} catch (Exception e) {
				emailLog = new EmailLog();
				emailLog.setLog(e.getMessage());
				emailLog.setErroRelacionado("evento cancelado");
				emailLog.setEmailDestinatario(destinatario.getParticipante().getEmail());
				emailLogFacade.include(emailLog);
			}
		}
	}

	public void emailEventoAbertoOuEmAndamentoCancelado(ArrayList<ParticipanteInscrito> destinatarios,
			boolean eventoCancelado, String CC) {
		for (ParticipanteInscrito pi : destinatarios) {
			ParticipanteInscrito destinatario = new ParticipanteInscrito();
			destinatario.setParticipante(pi.getParticipante());
			destinatario.setAtividade(pi.getAtividade());

			StringBuffer html = new StringBuffer();
			html.append("<!DOCTYPE html>");
			html.append("<html lang=\"pt-br\">");
			html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
			html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
			html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getParticipante().getNome());
			html.append("<br/>Voc&ecirc; est&aacute; recebendo esse e-mail pois estava "
					+ "pr&eacute;-inscrito ou inscrito na atividade <b>" + destinatario.getAtividade().getNome()
					+ "</b>, do evento <b>" + destinatario.getAtividade().getEvento().getNome());
			if (eventoCancelado) {
				html.append("Este evento foi cancelado pelo motivo: "
						+ pi.getAtividade().getEvento().getMotivoCancelamento() + ".<br/>");
			} else {
				html.append("Esta atividade foi cancelada pelo motivo: " + pi.getAtividade().getMotivoCancelamento()
						+ ".<br/>");
			}
			html.append("Entre em contato com a ESCEX para obter mais informa&ccedil;&otilde;es.<br/>");
			html.append("E-mail: escex.sec@tce.ma.gov.br, telefone (98) 2016-6178.<br/>");
			html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
					+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
			html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
			html.append("<tr><td><div align=\"left\">"
					+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
					+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
			html.append("</table></body></html>");

			try {
				new SimpleMail(emailSophia, senhaSophia, destinatario.getParticipante().getEmail(), this.CC,
						"(Evento Cancelado) Sophia - Tribunal de Contas do Estado do Maranhão", " ", html.toString(),
						" ");
			} catch (Exception e) {
				emailLog = new EmailLog();
				emailLog.setLog(e.getMessage());
				emailLog.setErroRelacionado("evento cancelado");
				emailLog.setEmailDestinatario(destinatario.getParticipante().getEmail());
				emailLogFacade.include(emailLog);
			}
		}
	}

	public void emailParticipanteInscrito(Pessoa destinatario, Atividade atividade, String CC) {
		StringBuffer html = new StringBuffer();
		html.append("<!DOCTYPE html>");
		html.append("<html lang=\"pt-br\">");
		html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
		html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getNome());
		html.append("<br/>Sua inscri&ccedil;&atilde;o foi realizada pela secretaria da ESCEX para a atividade <b>"
				+ atividade.getNome() + "</b>, do evento <b>" + atividade.getEvento().getNome() + "</b>.<br/>");
		html.append("<br/>Suas inscri&ccedil;&otilde;es podem ser visualizadas em Eventos > Meus eventos.<br/>");
		html.append("Aguarde o registro de sua(s) frequ&ecirc;ncia(s) para realizar a emiss&atilde;o "
				+ "de seu certificado.<br/>");
		html.append("OBS: para atividades que acontecem em mais de um dia, &eacute; exigido um <b>m&iacute;nimo "
				+ "de 75%</b> de frequ&ecirc;ncia para emitir o certificado.<br/>");
		html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
				+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
		html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
		html.append("<tr><td><div align=\"left\">"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
		html.append("</table></body></html>");

		try {
			new SimpleMail(emailSophia, senhaSophia, destinatario.getEmail(), this.CC,
					"(Inscrição Realizada) Sophia - Tribunal de Contas do Estado do Maranhão", " ", html.toString(),
					" ");
		} catch (Exception e) {
			emailLog = new EmailLog();
			emailLog.setLog(e.getMessage());
			emailLog.setErroRelacionado("inscreve participante - gerenciamento");
			emailLog.setEmailDestinatario(destinatario.getEmail());
			emailLogFacade.include(emailLog);
		}
	}

	// Não utilizado
	public void emailCertificadoParticipanteDisponivel(Pessoa destinatario, Atividade atividade, String CC) {
		StringBuffer html = new StringBuffer();
		html.append("<!DOCTYPE html>");
		html.append("<html lang=\"pt-br\">");
		html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
		html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getNome());
		html.append("<br/>Seu certificado de participa&ccedil;&atilde;o na atividade <b>" + atividade.getNome()
				+ "</b>, do evento <b>" + atividade.getEvento().getNome() + "</b> foi "
				+ "gerado e disponibilizado para download. Para realizar o download do arquivo de "
				+ "certificado, esteja logado no Sophia e acesse Eventos > Meus eventos.<br/>");
		html.append("<br/>Na parte inferior do certificado consta um token de verifica&ccedil;&atilde;o "
				+ "&uacute;nico, que pode ser usado para verificar a autenticidade do certificado. "
				+ "&Eacute; poss&iacute;vel valid&aacute;-lo no Sophia na op&ccedil;&atilde;o "
				+ "VALIDAR CERTIFICADO.<br/>");
		html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
				+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
		html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
		html.append("<tr><td><div align=\"left\">"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
		html.append("</table></body></html>");
		new SimpleMail(emailSophia, senhaSophia, destinatario.getEmail(), this.CC,
				"(Certificado de Participação Disponível) Sophia - Tribunal de Contas do Estado do Maranhão", " ",
				html.toString(), " ");
	}

	public void emailCertificadoParticipanteDisponivel(ArrayList<ParticipanteInscrito> destinatarios, String CC) {
		for (ParticipanteInscrito pi : destinatarios) {
			ParticipanteInscrito destinatario = new ParticipanteInscrito();
			destinatario.setParticipante(pi.getParticipante());
			destinatario.setAtividade(pi.getAtividade());

			StringBuffer html = new StringBuffer();
			html.append("<!DOCTYPE html>");
			html.append("<html lang=\"pt-br\">");
			html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
			html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
			html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getParticipante().getNome());
			html.append("<br/>Seu certificado de participa&ccedil;&atilde;o na atividade <b>"
					+ destinatario.getAtividade().getNome() + "</b>, do evento <b>"
					+ destinatario.getAtividade().getEvento().getNome() + "</b> foi "
					+ "gerado e disponibilizado para download. Para realizar o download do arquivo de "
					+ "certificado, esteja logado no Sophia e acesse " + pi.getParticipante().getNomeSobrenome()
					+ " > Meus certificados > Certificados de participação.<br/>"
					+ "<b>O download &eacute; liberado ap&oacute;s responder a avalia&ccedil;&atilde;o "
					+ "de rea&ccedil;&atilde;o da atividade.</b><br/>");
			html.append("<br/>Na parte inferior do certificado consta um token de verifica&ccedil;&atilde;o "
					+ "&uacute;nico, que pode ser usado para verificar a autenticidade do certificado. "
					+ "&Eacute; poss&iacute;vel valid&aacute;-lo no Sophia na op&ccedil;&atilde;o "
					+ "Validar certificado.<br/>");
			html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
					+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
			html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
			html.append("<tr><td><div align=\"left\">"
					+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
					+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
			html.append("</table></body></html>");

			try {
				new SimpleMail(emailSophia, senhaSophia, destinatario.getParticipante().getEmail(), this.CC,
						"(Certificado de Participação Disponível) Sophia - Tribunal de Contas do Estado do Maranhão",
						" ", html.toString(), " ");
			} catch (Exception e) {
				emailLog = new EmailLog();
				emailLog.setLog(e.getMessage());
				emailLog.setErroRelacionado("certificado participante disponível");
				emailLog.setEmailDestinatario(destinatario.getParticipante().getEmail());
				emailLogFacade.include(emailLog);
			}
		}
	}

	// Não utilizado
	public void emailCertificadoColaboradorDisponivel(Pessoa destinatario, Atividade atividade, String CC) {
		StringBuffer html = new StringBuffer();
		html.append("<!DOCTYPE html>");
		html.append("<html lang=\"pt-br\">");
		html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
		html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getNome());
		html.append("<br/>Seu certificado de colabora&ccedil;&atilde;o na atividade <b>" + atividade.getNome()
				+ "</b>, do evento <b>" + atividade.getEvento().getNome() + "</b> foi "
				+ "gerado e disponibilizado para download. Para realizar o download do arquivo de "
				+ "certificado, esteja logado no Sophia e acesse Colaborador > Minhas atividades como colaborador.<br/>");
		html.append("<br/>Na parte inferior do certificado consta um token de verifica&ccedil;&atilde;o "
				+ "&uacute;nico, que pode ser usado para verificar a autenticidade do certificado. "
				+ "&Eacute; poss&iacute;vel valid&aacute;-lo no Sophia na op&ccedil;&atilde;o "
				+ "Validar certificado.<br/>");
		html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
				+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
		html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
		html.append("<tr><td><div align=\"left\">"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
		html.append("</table></body></html>");
		new SimpleMail(emailSophia, senhaSophia, destinatario.getEmail(), CC,
				"(Certificado de Colaboração Disponível) Sophia - Tribunal de Contas do Estado do Maranhão", " ",
				html.toString(), " ");
	}

	public void emailCertificadoColaboradorDisponivel(ArrayList<AtividadeColaborador> destinatarios, String CC) {
		for (AtividadeColaborador ac : destinatarios) {
			AtividadeColaborador destinatario = new AtividadeColaborador();
			destinatario.setColaborador(ac.getColaborador());
			destinatario.setAtividade(ac.getAtividade());

			StringBuffer html = new StringBuffer();
			html.append("<!DOCTYPE html>");
			html.append("<html lang=\"pt-br\">");
			html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
			html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
			html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getColaborador().getPessoa().getNome());
			html.append("<br/>Seu certificado de colabora&ccedil;&atilde;o na atividade <b>"
					+ destinatario.getAtividade().getNome() + "</b>, do evento <b>"
					+ destinatario.getAtividade().getEvento().getNome() + "</b> foi "
					+ "gerado e disponibilizado para download. Para realizar o download do arquivo de "
					+ "certificado, esteja logado no Sophia e acesse "
					+ ac.getColaborador().getPessoa().getNomeSobrenome()
					+ " > Meus certificados > Certificados de colaboração.<br/>");
			html.append("<br/>Na parte inferior do certificado consta um token de verifica&ccedil;&atilde;o "
					+ "&uacute;nico, que pode ser usado para verificar a autenticidade do certificado. "
					+ "&Eacute; poss&iacute;vel valid&aacute;-lo no Sophia na op&ccedil;&atilde;o "
					+ "Validar certificado.<br/>");
			html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
					+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
			html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
			html.append("<tr><td><div align=\"left\">"
					+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
					+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
			html.append("</table></body></html>");

			try {
				new SimpleMail(emailSophia, senhaSophia, destinatario.getColaborador().getPessoa().getEmail(), this.CC,
						"(Certificado de Colaboração Disponível) Sophia - Tribunal de Contas do Estado do Maranhão",
						" ", html.toString(), " ");
			} catch (Exception e) {
				emailLog = new EmailLog();
				emailLog.setLog(e.getMessage());
				emailLog.setErroRelacionado("certificado colaborador disponível");
				emailLog.setEmailDestinatario(destinatario.getColaborador().getPessoa().getEmail());
				emailLogFacade.include(emailLog);
			}
		}
	}

	public void emailMensagem(String filtroSelecionado, String textoMensagem, ArrayList<DestinatarioUtil> destinatarios,
			String CC) {
		for (DestinatarioUtil du : destinatarios) {
			String nome = "", email = "";
			Evento evento = null;
			Atividade atividade = null;

			if (du.getParticipanteInscrito() != null) {
				nome = du.getParticipanteInscrito().getNomeParticipante();
				email = du.getParticipanteInscrito().getParticipante().getEmail();
				evento = du.getParticipanteInscrito().getAtividade().getEvento();
				atividade = du.getParticipanteInscrito().getAtividade();
			} else if (du.getParticipanteInteresse() != null) {
				nome = du.getParticipanteInteresse().getNomeParticipante();
				email = du.getParticipanteInteresse().getParticipante().getEmail();
				evento = du.getParticipanteInteresse().getAtividade().getEvento();
				atividade = du.getParticipanteInteresse().getAtividade();
			} else {
				nome = du.getPessoa().getNome();
				email = du.getPessoa().getEmail();
			}

			StringBuffer html = new StringBuffer();
			html.append("<!DOCTYPE html>");
			html.append("<html lang=\"pt-br\">");
			html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
			html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
			html.append("<tr><td>Prezado(a) Senhor(a), " + nome);

			if (filtroSelecionado.equals("Evento")) {
				html.append("<br/>Voc&ecirc; est&aacute; recebendo essa mensagem pois est&aacute; pr&eacute;-inscrito "
						+ "ou inscrito em uma ou mais atividades do evento <b>" + evento.getNome() + "</b>.");
			} else if (filtroSelecionado.equals("Atividade")) {
				html.append("<br/>Voc&ecirc; est&aacute; recebendo essa mensagem pois est&aacute; pr&eacute;-inscrito "
						+ "ou inscrito na atividade <b>" + atividade.getNome() + "</b>, parte do evento <b>"
						+ evento.getNome() + "</b>.");
			} else if (filtroSelecionado.equals("Participantes")) {
				html.append("<br/>A Escola Superior de Controle Externo direcionou uma mensagem a voc&ecirc;.");
			}

			html.append("<br/>A mensagem enviada &eacute; a que segue: ");
			html.append("<br/><br/>" + textoMensagem);
			html.append("<br/><br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
					+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
			html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
			html.append("<tr><td><div align=\"left\">"
					+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
					+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
			html.append("</table></body></html>");

			try {
				new SimpleMail(emailSophia, senhaSophia, email, this.CC,
						"(Mensagem da ESCEX) Sophia - Tribunal de Contas do Estado do Maranhão", " ", html.toString(),
						" ");
			} catch (Exception e) {
				emailLog = new EmailLog();
				emailLog.setLog(e.getMessage());
				emailLog.setErroRelacionado("mensagem escex");
				emailLog.setEmailDestinatario(email);
				emailLogFacade.include(emailLog);
			}
		}
	}
}
