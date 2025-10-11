package entidades;

public class Cuidador extends Usuario{
	
	protected int idPaciente;

	
	public Cuidador(String cPF, String nomeCompleto, String email, String senha, String numeroTelefone, int id,
			int idPaciente) {
		super(cPF, nomeCompleto, email, senha, numeroTelefone, id);
		this.idPaciente = idPaciente;
	}

	
	public int getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(int idPaciente) {
		this.idPaciente = idPaciente;
	}

}
