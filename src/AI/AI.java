package AI;

import giocatore.Giocatore;
import main.TicTacToe;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AI extends Giocatore {
    public static String[] difficoltà = {"Facile", "Normale", "Difficile", "Estrema"};
    private Integer skill;

    public AI(String nome, JLabel label, String segno) {
        this(nome, label, segno, 1);
    }

    public AI(String nome, JLabel label, String segno, Integer skill) {
        super(nome, label, segno, true);
        this.skill = skill;
    }

    @Override
    public void toccaA(){
        super.toccaA();
        scegli().doClick(10);
    }

    public void cambiaDifficoltà(){
        Integer skill = JOptionPane.showOptionDialog(null, "Inserire il livello di difficoltà","Benvenuto a Tic Tac Toe!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, difficoltà, difficoltà[1]);

        if(skill == -1)
            return;

        this.skill = skill;
        this.aggiornaScoreboard();
    }

    @Override
    public void aggiornaScoreboard(){
        this.label.setText(this.nome + " ("+difficoltà[this.skill]+"): " + punteggio + "/"+ TicTacToe.SCORE_LIMIT);
    }

    public JButton scegli(){
        Integer random = (int) (Math.random() * 100);
        Integer percentuale_sbaglio;

        switch(skill){
            case 0: percentuale_sbaglio = 30; break;
            case 1: percentuale_sbaglio = 20; break;
            case 2: percentuale_sbaglio = 10; break;
            case 3: percentuale_sbaglio = 0; break;
            default: percentuale_sbaglio = 20; break;
        }

        /* L'AVVERSARIO SBAGLIA PROPOSITAMENTE; */
        if(random < percentuale_sbaglio)
            return mossaCasuale();
        else
            return miglioreMossa();
    }

    public JButton mossaCasuale(){
        return bottoniLiberi.get((int)(Math.random() * bottoniLiberi.size()));
    }

    /*
        L'AVVERSARIO POSIZIONERA' IL PROPRIO SEGNO NEL MIGLIOR SPAZIO DISPONIBILE, CERCANDO DI BLOCCARE IL PROPRIO AVVERSARIO SE NECESSARIO.
    */
    public JButton miglioreMossa(){
        HashSet<Scelta> posValide = new HashSet<Scelta>();
        posValide.addAll(this.getPunteggioVerticali());
        posValide.addAll(this.getPunteggioOrizzontali());
        posValide.addAll(this.getPunteggioDiagonale());
        posValide.addAll(this.getPunteggioDiagonaleInv());

        Scelta mossaOttimale = max(posValide);

        if(mossaOttimale == null)
            return mossaCasuale();
        else
            return mossaOttimale.getButton();
    }

    ArrayList<Scelta> getPunteggioVerticali(){
        ArrayList<Scelta> verticali = new ArrayList<>();

        for(int y = 0; y < GRID_SIZE; y++){
            int punteggio = 0;

            // CALCOLA IL PUNTEGGIO, IL QUALE DIPENDE DAL CONTENUTO DI OGNI CELLA DEL RELATIVO TERZETTO
            for(int x = 0; x < GRID_SIZE; x++){
                String segno = grid.button[x][y].getText();
                punteggio = punteggio(punteggio, segno);
            }

            /*
                ASSEGNA IL PUNTEGGIO CALCOLATO AD OGNI BOTTONE DELLA RELATIVA VERTICALE
                E LO AGGIUNGE ALL'ARRAYLIST NEL CASO IN CUI IL BOTTONE SIA DISPONIBILE;
            */
            for(int x = 0; x < GRID_SIZE; x++)
                if(disponibile(grid.button[x][y]))
                    verticali.add(new Scelta(grid.button[x][y], punteggio));
        }

        return verticali;
    }

    ArrayList<Scelta> getPunteggioOrizzontali(){
        ArrayList<Scelta> verticali = new ArrayList<>();

        for(int x = 0; x < GRID_SIZE; x++){
            int punteggio = 0;


            // CALCOLA IL PUNTEGGIO, IL QUALE DIPENDE DAL CONTENUTO DI OGNI CELLA DEL RELATIVO TERZETTO
            for(int y = 0; y < GRID_SIZE; y++){
                String segno = grid.button[x][y].getText();
                punteggio = punteggio(punteggio, segno);
            }

            /*
                ASSEGNA IL PUNTEGGIO CALCOLATO AD OGNI BOTTONE DELLA RELATIVA ORIZZONTALE
                E LO AGGIUNGE ALL'ARRAYLIST NEL CASO IN CUI IL BOTTONE SIA DISPONIBILE;
            */
            for(int y = 0; y < GRID_SIZE; y++)
                if(disponibile(grid.button[x][y]))
                    verticali.add(new Scelta(grid.button[x][y], punteggio));
        }

        return verticali;
    }

    ArrayList<Scelta> getPunteggioDiagonale(){
        ArrayList<Scelta> diagonale = new ArrayList<>();

        int punteggio = 0;

        // CALCOLA IL PUNTEGGIO, IL QUALE DIPENDE DAL CONTENUTO DI OGNI CELLA DELLA DIAGONALE
        for(int x = 0, y = 0; x < GRID_SIZE; x++, y++){
            String segno = grid.button[x][y].getText();
            punteggio = punteggio(punteggio, segno);
        }

        /*
            ASSEGNA IL PUNTEGGIO CALCOLATO AD OGNI BOTTONE DELLA DIAGONALE
            E LO AGGIUNGE ALL'ARRAYLIST NEL CASO IN CUI IL BOTTONE SIA DISPONIBILE;
        */
        for(int x = 0, y = 0; x < GRID_SIZE; x++, y++)
            if(disponibile(grid.button[x][y]))
                diagonale.add(new Scelta(grid.button[x][y], punteggio));

        return diagonale;
    }

    ArrayList<Scelta> getPunteggioDiagonaleInv(){
        ArrayList<Scelta> diagonaleInv = new ArrayList<>();

        int punteggio = 0;

        // CALCOLA IL PUNTEGGIO, IL QUALE DIPENDE DAL CONTENUTO DI OGNI CELLA DELLA DIAGONALE
        for(int x = GRID_SIZE - 1, y = 0; x >= 0; x--, y++){
            String segno = grid.button[x][y].getText();
            punteggio = punteggio(punteggio, segno);
        }

        /*
            ASSEGNA IL PUNTEGGIO CALCOLATO AD OGNI BOTTONE DELLA DIAGONALE
            E LO AGGIUNGE ALL'ARRAYLIST NEL CASO IN CUI IL BOTTONE SIA DISPONIBILE;
        */
        for(int x = GRID_SIZE - 1, y = 0; x >= 0; x--, y++)
            if(disponibile(grid.button[x][y]))
                diagonaleInv.add(new Scelta(grid.button[x][y], punteggio));

        return diagonaleInv;
    }

    /* RICERCA DELL'OGGETTO CON IL PUNTEGGIO MASSIMO; */
    public static Scelta max(Set<Scelta> scelte){
        Scelta migliore = null;

        for(Scelta s : scelte){
            if(migliore == null){
                migliore = s;
                continue;
            }

            if(s.getPunteggio() > migliore.getPunteggio())
                migliore = s;
        }

        return migliore;
    }

    public Integer punteggio(Integer punteggio, String segno){
        if(segno.equals(segnoOpposto(this.segno)))
            return punteggio - 3;
        else if(segno.equals(this.segno))
            return punteggio + 5;
        else
            return punteggio;
    }

    public class Scelta{
        private JButton button;
        private Integer punteggio;

        public Scelta(JButton button, Integer punteggio){
            this.button = button;

            // Il punteggio viene trasformato in un intero positivo laddove sia negativo;
            this.punteggio = Math.abs(punteggio);
        }

        public JButton getButton() {
            return button;
        }

        public Integer getPunteggio() {
            return punteggio;
        }
    }
}
