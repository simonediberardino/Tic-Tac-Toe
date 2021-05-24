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
        this.label.setText(this.nome + " ("+difficoltà[this.skill]+"): " + punteggio + "/"+TicTacToe.SCORE_LIMIT);
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
        System.out.println("Mossa casuale");
        return bottoniLiberi.get((int)(Math.random() * bottoniLiberi.size()));
    }

    /*
        L'AVVERSARIO POSIZIONERA' IL PROPRIO SEGNO NEL MIGLIOR SPAZIO DISPONIBILE, CERCANDO DI BLOCCARE IL PROPRIO AVVERSARIO SE NECESSARIO.
    */
    public JButton miglioreMossa(){
        HashSet<Scelta> posValide = new HashSet<Scelta>();
        posValide.addAll(this.getVerticaliLibere());
        posValide.addAll(this.getOrizzontaliLibere());
        posValide.addAll(this.getDiagonaleLibera());
        posValide.addAll(this.getDiagonaleInvLibera());

        Scelta mossaOttimale = max(posValide);

        if(mossaOttimale == null)
            return mossaCasuale();
        else
            return mossaOttimale.getButton();
    }


    ArrayList<Scelta> getVerticaliLibere(){
        ArrayList<Scelta> verticali = new ArrayList<>();

        for(int y = 0; y < GRID_SIZE; y++){
            int punteggio = 0;

            for(int x = 0; x < GRID_SIZE; x++){
                String segno = grid.button[x][y].getText();
                punteggio = punteggio(punteggio, segno);
            }

            for(int x = 0; x < GRID_SIZE; x++)
                if(disponibile(grid.button[x][y]))
                    verticali.add(new Scelta(grid.button[x][y], punteggio));
        }

        return verticali;
    }

    ArrayList<Scelta> getOrizzontaliLibere(){
        ArrayList<Scelta> verticali = new ArrayList<>();

        for(int x = 0; x < GRID_SIZE; x++){
            int punteggio = 0;

            for(int y = 0; y < GRID_SIZE; y++){
                String segno = grid.button[x][y].getText();
                punteggio = punteggio(punteggio, segno);
            }

            for(int y = 0; y < GRID_SIZE; y++)
                if(disponibile(grid.button[x][y]))
                    verticali.add(new Scelta(grid.button[x][y], punteggio));
        }

        return verticali;
    }

    ArrayList<Scelta> getDiagonaleLibera(){
        ArrayList<Scelta> diagonale = new ArrayList<>();

        int punteggio = 0;

        for(int x = 0, y = 0; x < GRID_SIZE; x++, y++){
            String segno = grid.button[x][y].getText();
            punteggio = punteggio(punteggio, segno);
        }

        for(int x = 0, y = 0; x < GRID_SIZE; x++, y++)
            if(disponibile(grid.button[x][y]))
                diagonale.add(new Scelta(grid.button[x][y], punteggio));

        return diagonale;
    }

    ArrayList<Scelta> getDiagonaleInvLibera(){
        ArrayList<Scelta> diagonaleInv = new ArrayList<>();

        int punteggio = 0;

        for(int x = GRID_SIZE - 1, y = 0; x >= 0; x--, y++){
            String segno = grid.button[x][y].getText();
            punteggio = punteggio(punteggio, segno);
        }

        for(int x = GRID_SIZE - 1, y = 0; x >= 0; x--, y++)
            if(disponibile(grid.button[x][y]))
                diagonaleInv.add(new Scelta(grid.button[x][y], punteggio));

        return diagonaleInv;
    }

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
