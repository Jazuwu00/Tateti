import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.sql.ResultSet;


public class conectaDB {
    private String conexion;
    private String usuario;
    private String password;


    public conectaDB(String conexion, String usuario, String password) {
        this.conexion = conexion;
        this.usuario = usuario;
        this.password = password;
    }

    public String getConexion() {
        return conexion;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPassword() {
        return password;
    }


    public void imprimirEstadisticas(int idioma, String name) {
        try {

            //Crear conexion
            Connection miConexion = DriverManager.getConnection("jdbc:mysql://" + this.conexion, this.usuario, this.password);
            Statement miStatement = miConexion.createStatement();
            ResultSet miResultSet;

            //mensaje " contra "
            String contra = mensajexIdioma(idioma,35);
            String computadora = mensajexIdioma(idioma,36);

            // resultado de la partida
            String resultado = " ";
            if (name == null) {
                //obtener registros
                //mensaje estadisticas
                System.out.println(mensajexIdioma(idioma, 30));

                miResultSet = miStatement.executeQuery("SELECT * FROM registrodepartida rp INNER JOIN jugador j ON rp.idjugador = j.idjugador");
                System.out.println(mensajexIdioma(idioma, 19) + "   | " + mensajexIdioma(idioma, 20) + "   |      " + mensajexIdioma(idioma, 21));
                System.out.println("____________________________________________________________________________________________");
                while (miResultSet.next()) {
                    //para el mensaje gano o perdio o empato

                    if (miResultSet.getInt("gano") == 1) {
                        //gano
                        resultado = mensajexIdioma(idioma, 25);

                    } else if (miResultSet.getInt("gano") == 0) {
                        //perdio
                        resultado = mensajexIdioma(idioma, 26);

                    } else {
                        //empate
                        resultado = mensajexIdioma(idioma, 27);
                    }
                    System.out.println(miResultSet.getString("inicioDePartida") + " | " + miResultSet.getString("FinDePartida") + " | " + miResultSet.getString("nombre") + "  " + resultado + " " + contra + " " + computadora);

                }
                System.out.println("____________________________________________________________________________________________");
                System.out.println("\n");


            } else {
                //mensaje estadisticas

                Connection miCon = DriverManager.getConnection("jdbc:mysql://" + this.conexion, this.usuario, this.password);
                Statement miState = miCon.createStatement();
                miState.getConnection();
                boolean existe=false;
                miResultSet = miState.executeQuery("SELECT nombre FROM jugador WHERE nombre= '"+ name +"'");
                while (miResultSet.next()){
                    if(name.equals(miResultSet.getString("nombre"))){
                        existe=true;
                    }
                }
                if(existe){
                    System.out.println(mensajexIdioma(idioma, 18) + ": " + name + "\n");
                    System.out.println(mensajexIdioma(idioma, 19) + "   | " + mensajexIdioma(idioma, 20) + "   |      " + mensajexIdioma(idioma, 21));
                    System.out.println("____________________________________________________________________________________________");
                    miResultSet = miState.executeQuery("SELECT * FROM registrodepartida rp INNER JOIN jugador j ON rp.idjugador = j.idjugador WHERE j.nombre = '"+ name + "' ");

                    while (miResultSet.next()) {

                        if (miResultSet.getInt("gano") == 1) {
                            //gano
                            resultado = mensajexIdioma(idioma, 25);

                        } else if (miResultSet.getInt("gano") == 0) {
                            //perdio
                            resultado = mensajexIdioma(idioma, 26);

                        } else {
                            //empate
                            resultado = mensajexIdioma(idioma, 27);
                        }
                        System.out.println(" | " + miResultSet.getString("inicioDePartida") + " | " + miResultSet.getString("FinDePartida") + " | " + miResultSet.getString("nombre") + " " + resultado + " " + contra + " " + computadora);
                    }
                    System.out.println("____________________________________________________________________________________________");
                } else {
                    System.out.println(mensajexIdioma(idioma, 34) + ": " + name + "\n");
                }

                System.out.println("\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void GuardaPartida(DateFormat date, Date inicio, Date fin, int ganador, String nombre, int idioma) {
        try {
            //Crear conexion
            Connection miConexion = DriverManager.getConnection("jdbc:mysql://" + this.conexion, this.usuario, this.password);
            Statement miStatement = miConexion.createStatement();

            System.out.println(mensajexIdioma(idioma,22));

            PreparedStatement ps = miConexion.prepareStatement("SELECT*FROM jugador WHERE nombre = '" + nombre + "'");
            ResultSet jugador = ps.executeQuery();

            int idjugador = 0;

            while (jugador.next()) {
                idjugador = jugador.getInt("idjugador");
            }

            int dato = idjugador;

            String instruccionSQLB = "INSERT INTO registrodepartida(inicioDePartida,FinDePartida,gano,idjugador) VALUES ('" +
                    date.format(inicio) + "','" + date.format(fin) + "','" + ganador + "','" + dato + "')";

            miStatement.executeUpdate(instruccionSQLB);
            System.out.println(mensajexIdioma(idioma,24));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String mensajexIdioma(int idIdioma, int idMensaje) {
        // metodo que muestra un mensaje dependiendo del ididioma y el idmensaje
        String mensaje = "";
        try {
            Connection miConexion = DriverManager.getConnection("jdbc:mysql://" + this.conexion, this.usuario, this.password);
            Statement miStatement = miConexion.createStatement();
            ResultSet miResulSet = miStatement.executeQuery("SELECT descripcion from mensajexidioma where id_Mensaje= " + idMensaje + " and  id_idioma =" + idIdioma + " ");
            while (miResulSet.next()) {
                mensaje = miResulSet.getString("descripcion");
            }
            miConexion.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return mensaje;
    }
}
	

