package br.gov.ma.tce.sophia.ejb.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.InitialContext;

import br.gov.ma.tce.seguranca.interno.server.beans.usuario.Usuario;
import br.gov.ma.tce.seguranca.interno.server.beans.usuario.UsuarioFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.capacitacaoservidor.CapacitacaoServidor;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;

public class RelatorioMensalCapacitacoesVO {
	private String servidor;
	private String matricula;
	private String cargo;
	private String lotacao;
	private String capacitacao;
	private String periodo;
	private String cargaHoraria;
	private String instituicaoProvedora;

	public RelatorioMensalCapacitacoesVO(String servidor, String matricula, String cargo, String lotacao,
			String capacitacao, String periodo, String cargaHoraria, String instituicaoProvedora) {
		super();
		this.servidor = servidor;
		this.matricula = matricula;
		this.cargo = cargo;
		this.lotacao = lotacao;
		this.capacitacao = capacitacao;
		this.periodo = periodo;
		this.cargaHoraria = cargaHoraria;
		this.instituicaoProvedora = instituicaoProvedora;
	}

	public static List<RelatorioMensalCapacitacoesVO> preencheLista(
			Collection<ParticipanteInscrito> participantesInscritos,
			Collection<CapacitacaoServidor> capacitacoesServidores) {
		try {
			InitialContext ctx = new InitialContext();
			UsuarioFacadeBean usuarioSegFacade = (UsuarioFacadeBean) ctx.lookup(
					"java:global/sophia_ear/seguranca_cache_server/SegCache_UsuarioFacadeBean!br.gov.ma.tce.seguranca.server.beans.usuario.UsuarioFacade");
			List<RelatorioMensalCapacitacoesVO> listaCapacitacoes = new ArrayList<RelatorioMensalCapacitacoesVO>();
			for (ParticipanteInscrito pi : participantesInscritos) {
				String cargo = "Cargo não disponível", lotacao = "Lotação não disponível";
				Usuario u = usuarioSegFacade.findByMatricula(Integer.parseInt(pi.getParticipante().getServidorMatricula()));
				try {
					if (u.getServidor().getCargoFuncao() != null) {
						cargo = u.getServidor().getCargoFuncao();
					}
					if (u.getServidor().getLotacaoExercicio() != null) {
						lotacao = u.getServidor().getLotacaoExercicio();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				RelatorioMensalCapacitacoesVO object = new RelatorioMensalCapacitacoesVO(pi.getNomeParticipante(),
						pi.getParticipante().getServidorMatricula(), cargo, lotacao, pi.getAtividade().getNome(),
						pi.getAtividade().getDataStr(), pi.getAtividade().getCargaHorariaStr(), "ESCEX (TCE/MA)");
				listaCapacitacoes.add(object);
			}

			for (CapacitacaoServidor cs : capacitacoesServidores) {
				String cargo = "Cargo não disponível", lotacao = "Lotação não disponível";
				Usuario u = usuarioSegFacade.findByMatricula(Integer.parseInt(cs.getParticipante().getServidorMatricula()));
				try {
					if (u.getServidor().getCargoFuncao() != null) {
						cargo = u.getServidor().getCargoFuncao();
					}
					if (u.getServidor().getLotacaoExercicio() != null) {
						lotacao = u.getServidor().getLotacaoExercicio();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				RelatorioMensalCapacitacoesVO object = new RelatorioMensalCapacitacoesVO(cs.getNomeParticipante(),
						cs.getParticipante().getServidorMatricula(), cargo, lotacao, cs.getNomeCapacitacao(),
						cs.getDataStr(), cs.getCargaHorariaStr(), cs.getInstituicaoProvedora());
				listaCapacitacoes.add(object);
			}

			return listaCapacitacoes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getServidor() {
		return servidor;
	}

	public void setServidor(String servidor) {
		this.servidor = servidor;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getLotacao() {
		return lotacao;
	}

	public void setLotacao(String lotacao) {
		this.lotacao = lotacao;
	}

	public String getCapacitacao() {
		return capacitacao;
	}

	public void setCapacitacao(String capacitacao) {
		this.capacitacao = capacitacao;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(String cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public String getInstituicaoProvedora() {
		return instituicaoProvedora;
	}

	public void setInstituicaoProvedora(String instituicaoProvedora) {
		this.instituicaoProvedora = instituicaoProvedora;
	}

}
