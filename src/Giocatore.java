import javax.swing.*;

public class Giocatore extends TicTacToe{
    protected boolean CPU;
    protected String nome;
    protected String segno;
    protected JLabel label;
    protected int punteggio;
    protected int mosse;

    public Giocatore(String nome, JLabel label, String segno){
        this(nome, label, segno, false);
    }

    protected Giocatore(String nome, JLabel label, String segno, boolean CPU){
        this.nome = nome;
        this.label = label;
        this.segno = segno;
        this.CPU = CPU;
        this.punteggio = 0;
        this.mosse = 0;
    }

    public String getNome() {
        return nome;
    }

    public String getSegno() {
        return segno;
    }

    public int getPunteggio(){
        return punteggio;
    }

    public void aggiungiPunteggio(){
        this.punteggio++;
        this.aggiornaScoreboard();
    }

    public void azzeraPunteggio(){
        this.punteggio = 0;
        this.mosse = 0;
        this.aggiornaScoreboard();
    }

    public void aggiornaScoreboard(){
        this.label.setText(this.nome + ": " + punteggio + "/"+TicTacToe.SCORE_LIMIT);
    }

    public void toccaA(){
        giocante = this;
        titolo.setText("Tocca a " + this.getNome() + "!");
    }

    public boolean isCPU(){
        return CPU;
    }

    public int getMosse(){
        return mosse;
    }

    public void incrementaMosse(){
        mosse++;
    }
}
