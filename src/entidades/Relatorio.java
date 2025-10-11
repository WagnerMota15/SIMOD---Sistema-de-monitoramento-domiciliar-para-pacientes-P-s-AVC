package entidades;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Relatorio {
	
	private LocalDateTime dataEmissao;
	private int idPaciente;
	private int idProfissionalSaude;
	private String feedbackPaciente;
	//sugestão: guardar as classes de medicamentos,atividades,dieta e etc
	
	
	public Relatorio(LocalDateTime dataEmissao, int idPaciente, int idProfissionalSaude, String feedbackPaciente) {
		this.dataEmissao = dataEmissao;
		this.idPaciente = idPaciente;
		this.idProfissionalSaude = idProfissionalSaude;
		this.feedbackPaciente = feedbackPaciente;
	}


	public LocalDateTime getDataEmissao() {
		return dataEmissao;
	}


	public void setDataEmissao(LocalDateTime dataEmissao) {
		this.dataEmissao = dataEmissao;
	}


	public int getIdPaciente() {
		return idPaciente;
	}


	public void setIdPaciente(int idPaciente) {
		this.idPaciente = idPaciente;
	}


	public int getIdProfissionalSaude() {
		return idProfissionalSaude;
	}


	public void setIdProfissionalSaude(int idProfissionalSaude) {
		this.idProfissionalSaude = idProfissionalSaude;
	}


	public String getFeedbackPaciente() {
		return feedbackPaciente;
	}


	public void setFeedbackPaciente(String feedbackPaciente) {
		this.feedbackPaciente = feedbackPaciente;
	}
	
	
}
