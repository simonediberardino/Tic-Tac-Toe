/*
    Autore: Simone Di Berardino;
    Data: 18/05/2021;
    Oggetto: Celebre versione del gioco "Tic Tac Toe", riprodotta in Java.
*/

package main;

import AI.AI;
import GUI.Background;
import GUI.Grid;
import GUI.Scoreboard;
import giocatore.Giocatore;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class TicTacToe {
    public static final String[] segni = {"x", "o"};
    public static final String[] SCORE_LIMITS = {"3", "5", "7"};

    /* PUNTEGGIO LIMITE; */
    public static int SCORE_LIMIT;

    /* DIMENSIONE GRIGLIA; */
    public static final int GRID_SIZE = 3;

    /* GIOCATORE A CUI SPETTA IL TURNO; */
    protected static Giocatore giocante;

    /* AI */
    protected static AI AI;

    /* FRAME PRINCIPALE; */
    protected static JFrame f;

    /* PANNELLO PRINCIPALE; */
    private static JPanel panel;

    /* PANNELLO CONTENENTE I BOTTONI; */
    private static JPanel pGioco;

    private static JPanel box, box_funcs;

    /* PANNELLO CONTENENTE LO STATO DELLA PARTITA; */
    private static JPanel status;

    protected static Scoreboard scoreboard;

    /* STATO DELLA PARTITA; */
    protected static JLabel titolo;

    /* CREDITI; */
    protected static JLabel footer;

    /* ARRAY CONTENENTE I GIOCATORI; */
    protected static Giocatore[] giocatori = new Giocatore[2];

    /* VETTORE CONTENENTE I PULSANTI LIBERI; */
    protected static ArrayList<JButton> bottoniLiberi = new ArrayList<>();

    /* MATRICE DEI BOTTONI DEL PIANO DI GIOCO; */
    protected static Grid grid;

    /* BOTTONI SECONDARI; */
    private static JButton quit, restart, difficoltà;

    public static void main(String args[]) {
        creaInterfaccia();
        impostazioniPartita();
        registraGiocatori();
    }

    static void creaInterfaccia() {
        creaFrame();
        creaPannello();
        creaStatus();
        creaSottoPannelli();
        creaPGioco();
        aggiungiSfondo();
        aggiungiBottoni();
        aggiornaFrame();
    }

    static void creaFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        f = new JFrame("Tic Tac Toe!");
        f.setResizable(false);
        f.setBackground(null);
        f.setSize(800, 480);
        f.setLocation((int) screenSize.getWidth() / 2 - f.getWidth() / 2, (int) screenSize.getHeight() / 2 - f.getHeight() / 2);
        f.setVisible(true);
    }

    static void creaPannello() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 30, 30, 30));
        panel.setOpaque(false);
        f.add(panel);
    }

    static void creaStatus(){
        status = new JPanel();
        status.setOpaque(false);

        titolo = new JLabel("Tic Tac Toe!", SwingConstants.CENTER);
        titolo.setPreferredSize(new Dimension(f.getWidth(), 75));
        titolo.setFont(new Font("Georgia", Font.BOLD, 35));
        titolo.setForeground(new Color(3, 119, 252));

        footer = new JLabel("Simone Di Berardino © 2021", SwingConstants.RIGHT);
        footer.setFont(new Font("Calibri", 10, 10));

        status.add(titolo);
        panel.add(status, "North");
        panel.add(footer, "South");
    }

    static void creaSottoPannelli(){
        box = new JPanel();
        box.setPreferredSize(new Dimension(300, 300));
        box.setLayout(new GridLayout(2,1));
        box.setOpaque(false);

        scoreboard = new Scoreboard();

        box_funcs = new JPanel();
        box_funcs.setOpaque(false);

        box.add(scoreboard);
        panel.add(box, "East");
    }

    static void creaPGioco(){
        pGioco = new JPanel();
        pGioco.setLayout(new GridLayout(3,3));
        pGioco.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        pGioco.setBackground(Color.gray);
        pGioco.setPreferredSize(new Dimension(300, 300));

        panel.add(pGioco, "West");
    }

    static void aggiungiSfondo(){
        f.add(new Background("../img/background.jpg"));
    }

    static void aggiungiBottoni(){
        creaGriglia();
        creaBottoneRestart();
        bottoneDifficoltà();
        creaBottoneUscita();
    }

    static void creaGriglia(){
        grid = new Grid(pGioco, GRID_SIZE);
        pulisciBottoni();
    }

    static void pulisciGriglia(){
        pulisciBottoni();
        grid.clear();
    }

    static void pulisciBottoni(){
        bottoniLiberi.clear();

        for(Integer i = 0; i < GRID_SIZE; i++){
            for(Integer j = 0; j < GRID_SIZE; j++)
                bottoniLiberi.add(grid.button[i][j]);
        }
    }

    static void creaBottoneRestart(){
        box.add(box_funcs);

        restart = new JButton("Ricomincia partita");

        restart.addActionListener(e -> {
            int risposta = JOptionPane.showConfirmDialog(null, "Vuoi davvero ricominciare la partita?");
            if(risposta == JOptionPane.YES_OPTION)
                riavviaPartita();
        });

        box_funcs.add(restart);
    }

    static void bottoneDifficoltà(){
        difficoltà = new JButton("Cambia difficoltà");
        difficoltà.setEnabled(false);

        difficoltà.addActionListener(e -> {
            AI.cambiaDifficoltà();
        });

        box_funcs.add(difficoltà);
    }

    static void creaBottoneUscita(){
        quit = new JButton("Torna al desktop");

        quit.addActionListener(e -> {
            int risposta = JOptionPane.showConfirmDialog(null, "Vuoi davvero chiudere il gioco?");
            if(risposta == JOptionPane.YES_OPTION)
                System.exit(1);
        });

        box_funcs.add(quit);
    }

    static void aggiornaFrame(){
        f.revalidate();
    }

    static void setVincitore(Giocatore g){
        String stato;

        if(g == null)
            stato = "In pareggio.";
        else
            stato = "Vince " + g.getNome() + ".";

        scoreboard.setTitle(stato);
    }

    static void impostazioniPartita(){
        int index = JOptionPane.showOptionDialog(null, "Selezionare il punteggio limite della partita","Tic Tac Toe!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, SCORE_LIMITS, SCORE_LIMITS[0]);

        if(index == -1)
            index = 0;

        SCORE_LIMIT = Integer.parseInt(SCORE_LIMITS[index]);
    }

    static void registraGiocatori(){
        String[] options = {"CPU", "Giocatore reale"};
        int risposta = JOptionPane.showOptionDialog(null, "Contro chi vuoi giocare?","Tic Tac Toe!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        int index = 0;

        if(risposta == 0){
            difficoltà.setEnabled(true);
            AI = new AI("AI", scoreboard.score[index], segni[index]);
            AI.aggiornaScoreboard();
            giocatori[index] = AI;
            AI.cambiaDifficoltà();
            index++;
        }

        for(; index < giocatori.length; index++){
            String nome = JOptionPane.showInputDialog(null,"Inserire il nome del " + (index + 1) + "° giocatore:","Tic Tac Toe!", JOptionPane.INFORMATION_MESSAGE);

            if(nome == null || nome.equals(""))
                nome = "Giocatore " + (index + 1);

            giocatori[index] = new Giocatore(nome, scoreboard.score[index], segni[index]);
        }

        riavviaPartita();
    }

    static void riavviaPartita(){
        for(Giocatore p : giocatori)
            p.azzeraPunteggio();

        pulisciGriglia();
        setVincitore(null);

        giocareCasuale().toccaA();
    }

    public static String segnoOpposto(String segno){
        for(String s : segni)
            if(!s.equals(segno))
                return s;
        return null;
    }

    public static boolean disponibile(JButton button){
        return button.getText().equals("");
    }

    static Giocatore giocareCasuale(){
        return giocatori[(int) (Math.random() * giocatori.length)];
    }
}
