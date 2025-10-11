package entidades;

public class ProfissionalSaude extends Usuario{
	
	public String numConselho;

	public ProfissionalSaude(String cPF, String nomeCompleto, String email, String senha, String numeroTelefone, int id,
			String numConselho) {
		super(cPF, nomeCompleto, email, senha, numeroTelefone, id);
		this.numConselho = numConselho;
	}

	public String getNumConselho() {
		return numConselho;
	}

	public void setNumConselho(String numConselho) {
		this.numConselho = numConselho;
	}
	
	
}
