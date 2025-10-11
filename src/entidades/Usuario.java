package entidades;

public abstract class Usuario {
	
	protected String CPF;
	protected String nomeCompleto;
	protected String email;
	protected String senha;
	protected String numeroTelefone;
	protected int id;
	
	
	public Usuario(String cPF, String nomeCompleto, String email, String senha, String numeroTelefone, int id) {
		CPF = cPF;
		this.nomeCompleto = nomeCompleto;
		this.email = email;
		this.senha = senha;
		this.numeroTelefone = numeroTelefone;
		this.id = id;
	}


	public String getCPF() {
		return CPF;
	}


	public void setCPF(String cPF) {
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


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	
}
