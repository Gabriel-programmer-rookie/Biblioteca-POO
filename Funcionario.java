public class Funcionario extends Usuario {
    public Funcionario(int id, String nome) {
        super(id, nome, 2);
    }

    @Override
    public String getTipoUsuario() { return "FUNCIONARIO"; }
}