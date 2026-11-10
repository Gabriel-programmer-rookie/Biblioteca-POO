public class Revista extends ItemBiblioteca {
    private final int edicao;

    public Revista(int id, String titulo, int ano, int edicao) {
        super(id, titulo, ano);
        this.edicao = edicao;
    }

    @Override
    public String getTipoItem() { return "REVISTA"; }

    public int getEdicao() { return edicao; }

    @Override
    public String toString() {
        return super.toString() + " | Edição: " + edicao;
    }
}