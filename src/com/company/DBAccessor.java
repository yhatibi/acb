package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.*;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static java.sql.Types.NULL;

public class DBAccessor {
    private String dbname;
    private String host;
    private String port;
    private String user;
    private String passwd;
    private String schema;
    Connection conn = null;

    /**
     * Initializes the class loading the database properties file and assigns
     * values to the instance variables.
     *
     * @throws RuntimeException
     *             Properties file could not be found.
     */
    public void init() {
        Properties prop = new Properties();
        InputStream propStream = this.getClass().getClassLoader().getResourceAsStream("db.properties");

        try {
            prop.load(propStream);
            this.host = prop.getProperty("host");
            this.port = prop.getProperty("port");
            this.dbname = prop.getProperty("dbname");
            this.schema = prop.getProperty("schema");
        } catch (IOException e) {
            String message = "ERROR: db.properties file could not be found";
            System.err.println(message);
            throw new RuntimeException(message, e);
        }
    }

    /**
     * Obtains a {@link Connection} to the database, based on the values of the
     * <code>db.properties</code> file.
     *
     * @return DB connection or null if a problem occurred when trying to
     *         connect.
     */
    public Connection getConnection(Identity identity) {

        // Implement the DB connection
        String url = null;
        try {
            // Loads the driver
            Class.forName("org.postgresql.Driver");

            // Preprara connexió a la base de dades
            StringBuffer sbUrl = new StringBuffer();
            sbUrl.append("jdbc:postgresql:");
            if (host != null && !host.equals("")) {
                sbUrl.append("//").append(host);
                if (port != null && !port.equals("")) {
                    sbUrl.append(":").append(port);
                }
            }
            sbUrl.append("/").append(dbname);
            url = sbUrl.toString();

            // Utilitza connexió a la base de dades
            conn = DriverManager.getConnection(url, identity.getUser(), identity.getPassword());
            conn.setAutoCommit(false);
        } catch (ClassNotFoundException e1) {
            System.err.println("ERROR: Al Carregar el driver JDBC");
            System.err.println(e1.getMessage());
        } catch (SQLException e2) {
            System.err.println("ERROR: No connectat  a la BD " + url);
            System.err.println(e2.getMessage());
        }

        // Sets the search_path
        if (conn != null) {
            Statement statement = null;
            try {
                statement = conn.createStatement();
                statement.executeUpdate("SET search_path TO " + this.schema);
                // missatge de prova: verificació
                System.out.println("OK: connectat a l'esquema " + this.schema + " de la base de dades " + url
                        + " usuari: " + user + " password:" + passwd);
                System.out.println();
                //
            } catch (SQLException e) {
                System.err.println("ERROR: Unable to set search_path");
                System.err.println(e.getMessage());
            } finally {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("ERROR: Closing statement");
                    System.err.println(e.getMessage());
                }
            }
        }

        return conn;
    }


    public void altaAutor() throws SQLException, IOException, ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Scanner reader = new Scanner(System.in);

        System.out.println("Introdueix el Codi de lisencia de Federacio");
        int federation_license_code = reader.nextInt();

        System.out.println("Introdueix el nom del Jugador");
        reader.nextLine();
        String first_name = reader.nextLine();

        System.out.println("Introdueix cognom del Jugador");
        String last_name = reader.nextLine();

        System.out.println("Introdueix la data de naixament");
        String date = reader.nextLine();
        Date birth_date = format.parse(date);

        System.out.println("Introdueix Genere del Jugador");
        char gender = reader.next().charAt(0);

        System.out.println("Introdueix la altura del Jugador");
        int height = reader.nextInt();

        System.out.println("Introdueix el nom del Equip");
        reader.nextLine();
        String team_name = reader.nextLine();

        System.out.println("Introdueix numero total de MVPs");
        int mvp_total = reader.nextInt();



        Statement statement = null;
        statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO player (federation_license_code, first_name, last_name, birth_date, gender, height, team_name, mvp_total) VALUES ("+federation_license_code+",'"+first_name+"','"+last_name+"','"+birth_date+"','"+gender+"',"+height+",'"+team_name+"',"+mvp_total+")");

    }

    public void altaRevista() throws SQLException, NumberFormatException, IOException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Scanner reader = new Scanner(System.in);
        System.out.println("Introdueix el id de la revista");
        int id = reader.nextInt();
        System.out.println("Introdueix el titol");
        reader.nextLine();
        String titol = reader.nextLine();
        System.out.println("Introdueix la data de publicacio (yyyy-mm-dd)");
        Date date = format.parse(reader.nextLine());
        Statement statement = null;
        statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO revistes (id_revista, titol, data_publicacio) VALUES ("+id+",'"+titol+"','"+date+"')");

        statement.close();
    }


    public void altaArticle() throws SQLException, NumberFormatException, IOException, ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Scanner reader = new Scanner(System.in);
        System.out.println("Introdueix el nom del Equip");
        String name = reader.nextLine();

        System.out.println("Introdueix type del Equip");
        String type = reader.nextLine();

        System.out.println("Introdueix el Pais del Equip");
        String country = reader.nextLine();

        System.out.println("Introdueix la Ciutat del Equip");
        String city = reader.nextLine();

        System.out.println("Introdueix court Name");
        String court_name = reader.nextLine();



        Statement statement = null;
        statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO team (name, type, country, city, court_name) VALUES ('"+name+"','"+type+"','"+country+"','"+city+"','"+court_name+"')");



    }

    public void afegeixArticleARevista(Connection conn) throws SQLException {

        ResultSet rs = null;
        Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        Scanner reader = new Scanner(System.in);
        System.out.println("Escriut el federation_license_code del jugador que li vols dessasignar el equip:");
        String federation_license_code = reader.nextLine();

        try {
            rs = st.executeQuery("SELECT * FROM player WHERE federation_license_code = '"+federation_license_code+"'");

            while (rs.next()) {
                System.out.println("Nom: " + rs.getString("first_name"));

                System.out.println("Vols cambiar el equip del Jugador?");
                String resposta = br.readLine();

                if (resposta.equals("si")) {
                    // demana l'identificador de la revista
                    System.out.println("Introdueix el nou Equip");
                    // actualitza el camp
                    rs.updateString("team_name", "NULL");
                    // actualitza la fila
                    rs.updateRow();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void actualitzarTitolRevistes(Connection conn) throws SQLException {

        ResultSet rs = null;
        Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        Scanner reader = new Scanner(System.in);
        System.out.println("Escriut el federation_license_code del jugador que li vols asignar el equip:");
        String federation_license_code = reader.nextLine();

        try {
            rs = st.executeQuery("SELECT * FROM player WHERE federation_license_code = '"+federation_license_code+"'");

            while (rs.next()) {
                System.out.println("Nom: " + rs.getString("first_name"));

                System.out.println("Vols cambiar el equip del Jugador?");
                String resposta = br.readLine();

                if (resposta.equals("si")) {
                    // demana l'identificador de la revista
                    System.out.println("Introdueix el nou Equip");
                    String team_name = reader.nextLine();
                    // actualitza el camp
                    rs.updateString("team_name",  team_name);
                    // actualitza la fila
                    rs.updateRow();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void desassignaArticleARevista(Connection conn) throws SQLException, IOException {

        // TODO
        // seguint l'exemple de la funció afegeixArticleARevista:
        // definir variables locals
        // sol·licitar l'identificador de la revista
        // realitzar la consulta de tots els articles que corresponen a aquesta
        // revista
        // si no hi ha articles, emetre el missatge corresponent
        // en altre cas, mentre hi hagi articles:
        // Mostrar el títol de l'article i l'identificador de la revista
        // demanar si es vol rescindir la seva incorporació a la revista
        // en cas de que la resposta sigui "si"
        // actualitzar el camp corresponent a null
        // actualitzar la fila
        // en altre cas imprimir "operació cancel·lada"

        ResultSet rs = null;
        Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        Scanner reader = new Scanner(System.in);

        System.out.println("Id de la revista: ");
        int idrev = reader.nextInt();
        rs = st.executeQuery("SELECT * FROM articles WHERE id_revista = "+idrev+"");


        if (rs.getFetchSize() != 0) {
            System.out.println("No hi ha articles associats a aquesta revista. ");
        } else {
            try {


                while (rs.next()) {
                    System.out.println("Titol: " + rs.getString("titol") + " || Id revista: " + rs.getString("id_revista"));

                    System.out.println("Vol desassignar aquesta revista del article?");
                    String resposta = br.readLine();

                    if (resposta.equals("si")) {
                        // actualitza el camp
                        rs.updateInt("id_revista", NULL);

                        // actualitza la fila
                        rs.updateRow();
                        System.out.println("Desassignada!");
                    } else {
                        System.out.println("operació cancel·lada");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void mostraAutors() throws SQLException, IOException {
        Statement st = conn.createStatement();
        Scanner reader = new Scanner(System.in);
        ResultSet rs;

        rs = st.executeQuery("SELECT * FROM team");
        while (rs.next()) System.out.println("Name: " +rs.getString(1) + "\tType: " + rs.getString(2) + "\tCountry: " + rs.getString(3) + "\tCity: " + rs.getString(4) + "\tCourt Name: " + rs.getString(5));
        rs.close();
        st.close();
    }


    public void mostraRevistes() throws SQLException, IOException {
        Statement st = conn.createStatement();
        Scanner reader = new Scanner(System.in);
        ResultSet rs;

        System.out.println("Escriu el nom del equip:");
        String equip = reader.nextLine();

        rs = st.executeQuery("SELECT * FROM player WHERE team_name ='"+equip+"'");
        while (rs.next()) System.out.println("Name: " +rs.getString(2) + "\tLast Name: " + rs.getString(3) + "\tEquipo: " + rs.getString(7));
        rs.close();
        st.close();
    }


    public void mostraRevistesArticlesAutors() throws SQLException, IOException {
        Statement st = conn.createStatement();
        Scanner reader = new Scanner(System.in);
        ResultSet rs;


        rs = st.executeQuery("SELECT * FROM player WHERE team_name ='NULL'");
        while (rs.next()) System.out.println("Name: " +rs.getString(2) + "\tLast Name: " + rs.getString(3) + "\tEquipo: " + rs.getString(7));
        rs.close();
        st.close();

    }

    public void sortir() throws SQLException {
        System.out.println("ADÉU!");
        conn.close();
    }


    public void carregaAutors(Connection conn) throws SQLException, NumberFormatException, IOException, ParseException {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Yasin\\Desktop\\estadistiques.csv"));
        List<String> lines = new ArrayList<>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }


        String sql="INSERT INTO match_statistics VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);

        for (int i = 0; i < lines.size() ; i++) {

            if (lines.get(i).charAt(0) != '#') {

                String[] parts = lines.get(i).split(",");
                String home_team = parts[0];
                String visitor_team = parts[1];
                String date = parts[2];
                Date match_date = format.parse(date);
                String player = parts[3];
                int minutes_played = Integer.parseInt(parts[4]);
                int points = Integer.parseInt(parts[5]);
                int offensive_rebounds = Integer.parseInt(parts[6]);
                int defensive_rebounds = Integer.parseInt(parts[7]);
                int assists = Integer.parseInt(parts[8]);
                int committed_fouls = Integer.parseInt(parts[9]);
                int received_fouls = Integer.parseInt(parts[10]);
                int free_throw_attempts = Integer.parseInt(parts[11]);
                int free_throw_made = Integer.parseInt(parts[12]);
                int two_point_attempts = Integer.parseInt(parts[13]);
                int two_point_made = Integer.parseInt(parts[14]);
                int three_point_attempts = Integer.parseInt(parts[15]);
                int three_point_made = Integer.parseInt(parts[16]);
                int blocks = Integer.parseInt(parts[17]);
                int blocks_against = Integer.parseInt(parts[18]);
                int steals = Integer.parseInt(parts[19]);
                int turnovers = Integer.parseInt(parts[20]);
                int mvp_score = Integer.parseInt(parts[21]);

                Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs= st.executeQuery("SELECT * FROM match_statistics");
                boolean antiRepeat = false;

                while(rs.next())
                {
                    antiRepeat = true;
                    String a = rs.getString(1);
                    String b = rs.getString(2);
                    Date c = rs.getDate(3);
                    String d = rs.getString(4);
                    String aa;

                    if (a.equalsIgnoreCase("Club B…squet Manresa")){aa="Club Bàsquet Manresa";} else {aa=a;}
                    if (aa.equalsIgnoreCase(home_team) && b.equalsIgnoreCase(visitor_team) && c.equals(match_date) && d.equalsIgnoreCase(player)) {
                        System.out.println("Hello World");
                        rs.updateInt("minutes_played", minutes_played);
                        rs.updateInt("points", points);
                        rs.updateInt("offensive_rebounds", offensive_rebounds);
                        rs.updateInt("defensive_rebounds", defensive_rebounds);
                        rs.updateInt("assists", assists);
                        rs.updateInt("committed_fouls", committed_fouls);
                        rs.updateInt("received_fouls", received_fouls);
                        rs.updateInt("free_throw_attempts", free_throw_attempts);
                        rs.updateInt("free_throw_made", free_throw_made);
                        rs.updateInt("two_point_attempts", two_point_attempts);
                        rs.updateInt("two_point_made", two_point_made);
                        rs.updateInt("three_point_attempts", three_point_attempts);
                        rs.updateInt("three_point_made", three_point_made);
                        rs.updateInt("blocks", blocks);
                        rs.updateInt("blocks_against", blocks_against);
                        rs.updateInt("steals", steals);
                        rs.updateInt("turnovers", turnovers);
                        rs.updateInt("mvp_score", mvp_score);
                        rs.updateRow();
                        System.out.println("Hello World");
                        antiRepeat = false;
                        break;
                    } else {

                    }

                }

                if (home_team.equalsIgnoreCase("Club Bàsquet Manresa")){home_team="Club B…squet Manresa";} else {}
                if (antiRepeat == true) {
                    pst.clearParameters();
                    pst.setString(1, home_team);
                    pst.setString(2, visitor_team);
                    pst.setDate(3, new java.sql.Date(match_date.getTime()));
                    pst.setString(4,player);
                    pst.setInt(5,minutes_played);
                    pst.setInt(6,points);
                    pst.setInt(7,offensive_rebounds);
                    pst.setInt(8,defensive_rebounds);
                    pst.setInt(9,assists);
                    pst.setInt(10,committed_fouls);
                    pst.setInt(11,received_fouls);
                    pst.setInt(12,free_throw_attempts);
                    pst.setInt(13,free_throw_made);
                    pst.setInt(14,two_point_attempts);
                    pst.setInt(15,two_point_made);
                    pst.setInt(16,three_point_attempts);
                    pst.setInt(17,three_point_made);
                    pst.setInt(18,blocks);
                    pst.setInt(19,blocks_against);
                    pst.setInt(20,steals);
                    pst.setInt(21, turnovers);
                    pst.setInt(22, mvp_score);
                    pst.executeUpdate();
                    System.out.println("Done");

                }

                rs.close();
                st.close();
            } else {}

        }



        //tanquem Statement i Connection.
        pst.close();
        conn.close();



    }
}
