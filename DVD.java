public class DVD extends ItemBiblioteca {
    private final int duracaoMinutos;

    public DVD(int id, String titulo, int ano, int duracaoMinutos) {
        super(id, titulo, ano);
        this.duracaoMinutos = duracaoMinutos;
    }

    @Override
    public String getTipoItem() { return "DVD"; }

    public int getDuracaoMinutos() { return duracaoMinutos; }

    @Override
    public String toString() {
        return super.toString() + " | Duração: " + duracaoMinutos + " min";
    }
}