package br.gov.ma.tce.sophia.client.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.zkoss.zk.ui.util.Clients;

import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.emaillog.EmailLog;
import br.gov.ma.tce.sophia.ejb.beans.emaillog.EmailLogFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;
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
			Clients.showNotification("Erro de conexão interno. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
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
		new SimpleMail(emailSophia, senhaSophia, "joorgemelo@gmail.com", this.CC,
				"(Cadastro Realizado) Sophia - Tribunal de Contas do Estado do Maranhão", " ", html.toString(), " ");
	}

	public void emailCadastro(Pessoa destinatario, String CC) {
		StringBuffer html = new StringBuffer();
		html.append("<!DOCTYPE html>");
		html.append("<html lang=\"pt-br\">");
		html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
		html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getNome());
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

		try {
			new SimpleMail(emailSophia, senhaSophia, destinatario.getEmail(), this.CC,
					"(Cadastro Realizado) Sophia - Tribunal de Contas do Estado do Maranhão", " ", html.toString(),
					" ");
		} catch (Exception e) {
			emailLog = new EmailLog();
			emailLog.setLog(e.getMessage());
			emailLog.setErroRelacionado("cadastro");
			emailLog.setEmailDestinatario(destinatario.getEmail());
			emailLogFacade.include(emailLog);
		}
	}

	public void emailRecuperaSenha(Pessoa destinatario, String senha, String CC) {
		StringBuffer html = new StringBuffer();
		html.append("<!DOCTYPE html>");
		html.append("<html lang=\"pt-br\">");
		html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
		html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getNome());
		html.append("<br/>Uma requisi&ccedil;&atilde;o de recupera&ccedil;&atilde;o "
				+ "de senha foi recebida em nosso sistema.<br/>");
		html.append("O seu acesso pode ser feito com os seguintes dados:<br/>");
		html.append("<br/>Login: " + destinatario.getCpf() + "<br/>Senha: " + senha + "<br/>");
		html.append("<br/>Sua senha pode ser alterada em " + destinatario.getNomeSobrenome() + " > Trocar senha.<br/>");
		html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
				+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
		html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
		html.append("<tr><td><div align=\"left\">"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
		html.append("</table></body></html>");

		try {
			new SimpleMail(emailSophia, senhaSophia, destinatario.getEmail(), this.CC,
					"(Recuperação de Senha) Sophia - Tribunal de Contas do Estado do Maranhão", " ", html.toString(),
					" ");
		} catch (Exception e) {
			emailLog = new EmailLog();
			emailLog.setLog(e.getMessage());
			emailLog.setErroRelacionado("recuperar senha");
			emailLog.setEmailDestinatario(destinatario.getEmail());
			emailLogFacade.include(emailLog);
		}

	}

	public void emailListaDeInteresse(Pessoa destinatario, Atividade atividade, String CC) {
		StringBuffer html = new StringBuffer();
		html.append("<!DOCTYPE html>");
		html.append("<html lang=\"pt-br\">");
		html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
		html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getNome());
		html.append("<br/>Recebemos a sua solicita&ccedil;&atilde;o de interesse na atividade <b>" + atividade.getNome()
				+ "</b>, do evento <b>" + atividade.getEvento().getNome() + "</b>.<br/>");
		html.append(" Essa atividade est&aacute; prevista para o dia <b>" + atividade.getDataInicioStr()
				+ "</b>, das <b>" + atividade.getHoraInicioStr() + "</b> &agrave;s <b>" + atividade.getHoraFimStr()
				+ "</b>, por&eacute;m a data pode ser alterada caso n&atilde;o atinja a quantidade "
				+ "m&iacute;nima de interessados ou por outros fatores.<br/>");
		html.append("Caso voc&ecirc; satisfa&ccedil;a os requisitos da atividade, sua "
				+ "inscri&ccedil;&atilde;o ser&aacute; realizada automaticamente. Qualquer "
				+ "altera&ccedil;&atilde;o ser&aacute; avisada com "
				+ "anteced&ecirc;ncia em seu e-mail cadastrado.<br/>");
		html.append("<br/>As informa&ccedil;&otilde;es da atividade e do evento podem ser consultadas diretamente "
				+ "no Sophia. Em caso de eventuais d&uacute;vidas, entre em contato conosco por e-mail "
				+ "<br/>escex.sec@tce.ma.gov.br ou telefone (98) 2016-6178.<br/>");
		html.append("<br/>Para visualizar sua Lista de Interesse, esteja logado no Sophia e acesse "
				+ "Participante > Lista de interesse. &Eacute; poss&iacute;vel sair de alguma lista nesta "
				+ "se&ccedil;&atilde;o.<br/>");
		html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
				+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
		html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
		html.append("<tr><td><div align=\"left\">"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
		html.append("</table></body></html>");

		try {
			new SimpleMail(emailSophia, senhaSophia, destinatario.getEmail(), this.CC,
					"(Lista de Interesse) Sophia - Tribunal de Contas do Estado do Maranhão", " ", html.toString(),
					" ");
		} catch (Exception e) {
			emailLog = new EmailLog();
			emailLog.setLog(e.getMessage());
			emailLog.setErroRelacionado("lista de interesse");
			emailLog.setEmailDestinatario(destinatario.getEmail());
			emailLogFacade.include(emailLog);
		}
	}

	public void emailPreInscricao(Pessoa destinatario, Atividade atividade, String CC) {
		StringBuffer html = new StringBuffer();
		html.append("<!DOCTYPE html>");
		html.append("<html lang=\"pt-br\">");
		html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
		html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getNome());
		html.append("<br/>Recebemos a sua pr&eacute;-inscri&ccedil;&atilde;o na atividade <b>" + atividade.getNome()
				+ "</b>, do evento <b>" + atividade.getEvento().getNome() + "</b>.");
		html.append(" Essa atividade est&aacute; prevista para o dia <b>" + atividade.getDataInicioStr()
				+ "</b>, das <b>" + atividade.getHoraInicioStr() + "</b> &agrave;s <b>" + atividade.getHoraFimStr()
				+ "</b>, por&eacute;m a data pode ser alterada caso n&atilde;o atinja a quantidade "
				+ "m&iacute;nima de interessados ou por outros fatores.<br/>");
		html.append("Voc&ecirc; receber&aacute; um e-mail sobre a aprova&ccedil;&atilde;o ou n&atilde;o "
				+ "de sua inscri&ccedil;&atilde;o. Favor aguardar.<br/>");
		html.append("<br/>As informa&ccedil;&otilde;es da atividade e do evento podem ser consultadas diretamente "
				+ "no Sophia. Em caso de eventuais d&uacute;vidas, entre em contato conosco por e-mail "
				+ "<br/>escex.sec@tce.ma.gov.br ou telefone (98) 2016-6178.<br/>");
		html.append("<br/><b>Voc&ecirc; pode cancelar sua pr&eacute;-inscri&ccedil;&atilde;o enquanto esta n&atilde;o "
				+ "for confirmada.</b> Para isso, acesse o sistema, em Participante > Minhas "
				+ "pr&eacute;-inscri&ccedil;&otilde;es e cancele sua pr&eacute;-inscri&ccedil;&atilde;o.<br/>");
		html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
				+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
		html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
		html.append("<tr><td><div align=\"left\">"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
		html.append("</table></body></html>");

		try {
			new SimpleMail(emailSophia, senhaSophia, destinatario.getEmail(), this.CC,
					"(Pré-inscrição) Sophia - Tribunal de Contas do Estado do Maranhão", " ", html.toString(), " ");
		} catch (Exception e) {
			emailLog = new EmailLog();
			emailLog.setLog(e.getMessage());
			emailLog.setErroRelacionado("pré-inscrição");
			emailLog.setEmailDestinatario(destinatario.getEmail());
			emailLogFacade.include(emailLog);
		}
	}

	// Não utilizado
	public void emailSairListaDeInteresse(Pessoa destinatario, Atividade atividade, String CC) {
		StringBuffer html = new StringBuffer();
		html.append("<!DOCTYPE html>");
		html.append("<html lang=\"pt-br\">");
		html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
		html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getNome());
		html.append("<br/>Recebemos a sua solicita&ccedil;&atilde;o de sa&iacute;da da "
				+ "lista de interesse da atividade <b>" + atividade.getNome() + "</b>, do evento <b>"
				+ atividade.getEvento().getNome() + "</b>.<br/>");
		html.append("Voc&ecirc; foi removido dessa lista de interesse com sucesso." + "<br/>");
		html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
				+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
		html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
		html.append("<tr><td><div align=\"left\">"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
		html.append("</table></body></html>");
		new SimpleMail(emailSophia, senhaSophia, destinatario.getEmail(), this.CC,
				"(Saída de Lista de Interesse) Sophia - Tribunal de Contas do Estado do Maranhão", " ", html.toString(),
				" ");
	}

	// Não utilizado
	public void emailCancelarPreInscricao(Pessoa destinatario, Atividade atividade, String CC) {
		StringBuffer html = new StringBuffer();
		html.append("<!DOCTYPE html>");
		html.append("<html lang=\"pt-br\">");
		html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
		html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getNome());
		html.append("<br/>Recebemos a sua solicita&ccedil;&atilde;o de cancelamento de "
				+ "pr&eacute;-inscri&ccedil;&atilde;o na atividade <b>" + atividade.getNome() + "</b>, do evento <b>"
				+ atividade.getEvento().getNome() + "</b>.<br/>");
		html.append("Sua pr&eacute;-inscri&ccedil;&atilde;o nessa atividade foi cancelada com sucesso." + "<br/>");
		html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
				+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
		html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
		html.append("<tr><td><div align=\"left\">"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
		html.append("</table></body></html>");
		new SimpleMail(emailSophia, senhaSophia, destinatario.getEmail(), this.CC,
				"(Cancelamento de Pré-inscrição) Sophia - Tribunal de Contas do Estado do Maranhão", " ",
				html.toString(), " ");
	}

	// Não utilizado
	public void emailCadastroColaborador(Pessoa destinatario, String CC) {
		StringBuffer html = new StringBuffer();
		html.append("<!DOCTYPE html>");
		html.append("<html lang=\"pt-br\">");
		html.append("<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		html.append("<title>E-mail SOPHIA</title></head><body><table width=\"100%\" border=\"0\">");
		html.append("<tr><td>Prezado(a) Senhor(a), " + destinatario.getNome());
		html.append("<br/>Seu cadastro de colaborador foi realizado com sucesso.<br/>");
		html.append("Voc&ecirc; receber&aacute; um e-mail sobre a aprova&ccedil;&atilde;o do seu "
				+ "cadastro de colaborador. Favor aguardar.<br/>");
		html.append("Caso deseje que seu cadastro de colaborador seja desativado, entre em contato conosco por e-mail "
				+ "<br/>escex.sec@tce.ma.gov.br ou telefone (98) 2016-6178.<br/>");
		html.append("<br/>Atenciosamente,<br/>Escola Superior de Controle Externo<br/>"
				+ "Tribunal de Contas do Estado do Maranh&atilde;o<br/>");
		html.append("<br/><i>E-mail gerado automaticamente, favor n&atilde;o responder.</i></td></tr><br/><br/>");
		html.append("<tr><td><div align=\"left\">"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/SOPHIA3.png\" height=\"75\" />"
				+ "<img src=\"https://www6.tce.ma.gov.br/sophia/imagens/ESCEX.png\" height=\"75\" /></div></td></tr>");
		html.append("</table></body></html>");
		new SimpleMail(emailSophia, senhaSophia, destinatario.getEmail(), this.CC,
				"(Cadastro de Colaborador) Sophia - Tribunal de Contas do Estado do Maranhão", " ", html.toString(),
				" ");
	}

}
