package entidades;

import java.util.ArrayList;
import java.util.List;

public class Paciente extends Usuario{
	
	protected List<String> contatosFamiliares = new ArrayList<>();
	protected List<String> historicoMedico = new ArrayList<>();
	protected String tipoAVC;
	protected Boolean statusVinculo;
	protected String enderecoPaciente;


	public Paciente(String cPF, String nomeCompleto, String email, String senha, String numeroTelefone, int id,
			List<String> contatosFamiliares, List<String> historicoMedico, String tipoAVC, Boolean statusVinculo,
			String enderecoPaciente) {
		super(cPF, nomeCompleto, email, senha, numeroTelefone, id);
		this.contatosFamiliares = contatosFamiliares;
		this.historicoMedico = historicoMedico;
		this.tipoAVC = tipoAVC;
		this.statusVinculo = statusVinculo;
		this.enderecoPaciente = enderecoPaciente;
	}
	
	
	

	//não ponho o método set da lista de contatos e histórico médico por que não quero que o usuário fique trocando de lista
	//,mas sim adicionando contatos em uma mesma lista pertencente a ele.
	//Para isso,devo implementar dois métodos

	public List<String> getHistoricoMedico() {
		return historicoMedico;
	}


	public List<String> getContatosFamiliares() {
		return contatosFamiliares;
	}
	

	public String getTipoAVC() {
		return tipoAVC;
	}


	public void setTipoAVC(String tipoAVC) {
		this.tipoAVC = tipoAVC;
	}


	public Boolean getStatusVinculo() {
		return statusVinculo;
	}


	public void setStatusVinculo(Boolean statusVinculo) {
		this.statusVinculo = statusVinculo;
	}


	public String getEnderecoPaciente() {
		return enderecoPaciente;
	}


	public void setEnderecoPaciente(String enderecoPaciente) {
		this.enderecoPaciente = enderecoPaciente;
	}
	
	
}
