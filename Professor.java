public class Professor extends Usuario {
    public Professor(int id, String nome) {
        super(id, nome, 5);
    }

    @Override
    public String getTipoUsuario() { return "PROFESSOR"; }
}