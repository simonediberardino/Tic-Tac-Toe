import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/* CLASSE CHE GESTISCE LA PRESSIONE DEI BOTTONI E LO SVOLGIMENTO DELLA PARTITA; */
public class GameEngine extends TicTacToe implements ActionListener {
    private JButton button;

    /* COORDINATE DEL BOTTONE; */
    private Integer x, y;

    public GameEngine(JButton button, int x, int y){
        this.button = button;
        this.x = x;
        this.y = y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String contenuto = e.getActionCommand();

        if(!contenuto.equals(""))
            return;

        bottoniLiberi.remove(button);
        button.setText(giocante.getSegno());
        giocante.incrementaMosse();

        if(vinto()){
            termina(giocante);
        }else{
            /* SE IL NUMERO DI MOSSE E' UGUALE AL NUMERO DEI BOTTONI; */
            if(getMosse() == Math.pow(GRID_SIZE, 2))
                termina(null);
            else
                turnoSuccessivo(prossimoGiocatore(giocante));
        }
    }

    boolean vinto(){
        boolean verticale = this.controllaVerticale();
        if(verticale)
            return true;

        boolean orizzontale = this.controllaOrizzontale();
        if(orizzontale)
            return true;

        boolean diagonale = this.controllaDiagonale();
        if(diagonale)
            return true;

        boolean diagonaleInv = this.controllaDiagonaleInv();
        if(diagonaleInv)
            return true;

        return false;
    }

    boolean controllaVerticale(){
        String segno = this.button.getText();

        for(int y = 0; y < GRID_SIZE; y++){
            String scorri = grid.button[this.x][y].getText();

            if(!scorri.equals(segno))
                return false;
        }

        /* IL GIOCATORE HA VINTO: RIPERCORRIAMO LA VERTICALE COLORANDO I BOTTONI; */
        for(int y = 0; y < GRID_SIZE; y++)
            grid.button[this.x][y].setForeground(Color.red);

        return true;
    }

    boolean controllaOrizzontale(){
        String segno = this.button.getText();

        for(int x = 0; x < GRID_SIZE; x++){
            String scorri = grid.button[x][this.y].getText();

            if(!scorri.equals(segno))
                return false;
        }

        /* IL GIOCATORE HA VINTO: RIPERCORRIAMO LA ORIZZONTALE COLORANDO I BOTTONI; */
        for(int x = 0; x < GRID_SIZE; x++)
            grid.button[x][this.y].setForeground(Color.red);

        return true;
    }

    boolean controllaDiagonale(){
        String segno = this.button.getText();

        for(int x = 0, y = 0; x < GRID_SIZE; x++, y++){
            String scorri = grid.button[x][y].getText();

            if(!scorri.equals(segno))
                return false;
        }

        /* IL GIOCATORE HA VINTO: RIPERCORRIAMO LA DIAGONALE COLORANDO I BOTTONI; */
        for(int x = 0, y = 0; x < GRID_SIZE; x++, y++)
            grid.button[x][y].setForeground(Color.red);

        return true;
    }

    boolean controllaDiagonaleInv(){
        String segno = this.button.getText();

        for(int x = GRID_SIZE - 1, y = 0; x >= 0; x--, y++){
            String scorri = grid.button[x][y].getText();

            if(!scorri.equals(segno))
                return false;
        }

        for(int x = GRID_SIZE - 1, y = 0; x >= 0; x--, y++)
            grid.button[x][y].setForeground(Color.red);

        return true;
    }

    static void termina(Giocatore vincitore){
        aggiornaVincitore(vincitore);

        if(vincitore == null || vincitore.getPunteggio() < TicTacToe.SCORE_LIMIT)
            terminaRound(vincitore);
        else
            terminaPartita(vincitore);
    }

    static void terminaPartita(Giocatore vincitore){
        titolo.setText("Partita terminata!");

        int risposta = JOptionPane.showConfirmDialog(null, vincitore.getNome() + " ha vinto! Vuoi rigiocare?");
        if(risposta == JOptionPane.YES_OPTION)
            riavviaPartita();
        else
            System.exit(0);
    }

    static void terminaRound(Giocatore vincitore){
        titolo.setText("Round terminato!");

        String messaggio = ((vincitore == null) ? "Pareggio! " : vincitore.getNome() + " ha vinto! ");
        messaggio += "Premere ok per il prossimo round!";

        JOptionPane.showMessageDialog(null, messaggio);

        pulisciGriglia();
        turnoSuccessivo(vincitore); // Il turno passa al giocatore vincente;
    }

    static void turnoSuccessivo(Giocatore p){
        if(p == null)
            p = prossimoGiocatore(null); // Tocca a un giocatore casuale;

        p.toccaA();
    }

    static Giocatore prossimoGiocatore(Giocatore g){
        if(g == null)
            return giocareCasuale();

        for(Giocatore p : giocatori)
            if(p.getSegno() != g.getSegno())
                return p;

        return giocareCasuale();
    }

    static void aggiornaVincitore(Giocatore ultimoVincitore){
        if(ultimoVincitore == null)
            return;

        ultimoVincitore.aggiungiPunteggio();
        setVincitore(getMaxP());
    }

    /* METODO CHE RESTITUISCE IL GIOCATORE CON PUNTEGGIO PIU' ALTO; */
    static Giocatore getMaxP(){
        if(giocatori[0].getPunteggio() == giocatori[1].getPunteggio())
            return null;

        if(giocatori[0].getPunteggio() > giocatori[1].getPunteggio())
            return giocatori[0];

        return giocatori[1];
    }

    static int getMosse(){
        return (int) Math.pow(GRID_SIZE, 2) - bottoniLiberi.size();
    }
}
