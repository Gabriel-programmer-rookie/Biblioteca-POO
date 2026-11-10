public abstract class Usuario {
    private final int id;
    private final String nome;
    private final int limiteEmprestimos;
    private int qtdEmprestados;

    public Usuario(int id, String nome, int limiteEmprestimos) {
        this.id = id;
        this.nome = nome;
        this.limiteEmprestimos = limiteEmprestimos;
        this.qtdEmprestados = 0;
    }

    public abstract String getTipoUsuario();

    public int getId() { return id; }
    public String getNome() { return nome; }
    public int getLimiteEmprestimos() { return limiteEmprestimos; }
    public int getQtdEmprestados() { return qtdEmprestados; }

    public void incrementarEmprestimo() { this.qtdEmprestados++; }
    public void decrementarEmprestimo() { if (this.qtdEmprestados > 0) this.qtdEmprestados--; }

    @Override
    public String toString() {
        return "[" + getTipoUsuario() + "] ID: " + id + " | Nome: " + nome + 
               " | Empréstimos: " + qtdEmprestados + "/" + limiteEmprestimos;
    }
}