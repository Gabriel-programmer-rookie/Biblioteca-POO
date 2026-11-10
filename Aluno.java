public class Aluno extends Usuario {
    public Aluno(int id, String nome) {
        super(id, nome, 3);
    }

    @Override
    public String getTipoUsuario() { return "ALUNO"; }
}