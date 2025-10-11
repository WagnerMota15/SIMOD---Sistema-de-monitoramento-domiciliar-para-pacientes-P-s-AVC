package entidades;

public abstract class Usuario {
	
	protected Integer CPF;
	protected String nomeCompleto;
	protected String email;
	protected String senha;
	protected String numeroTelefone;

	
	public Usuario(Integer CPF, String nomeCompleto, String email, String senha, String numeroTelefone) {
		this.CPF = CPF;
		this.nomeCompleto = nomeCompleto;
		this.email = email;
		this.senha = senha;
		this.numeroTelefone = numeroTelefone;
	}
	public Integer getCPF() {
		return CPF;
	}
	public void setCPF(Integer cPF) {
		CPF = cPF;
	}
	public String getNomeCompleto() {
		return nomeCompleto;
	}
	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getNumeroTelefone() {
		return numeroTelefone;
	}
	public void setNumeroTelefone(String numeroTelefone) {
		this.numeroTelefone = numeroTelefone;
	}
	
	
}
