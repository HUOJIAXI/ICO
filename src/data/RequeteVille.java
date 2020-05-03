package data;

/**
*@description: Méthode de connexion de la base de données
*@author: Author: HUO Jiaxi & FANG Zhengjie
*@created: 02/05/2020
*/

/**
 *@description: Module algorithme Récuit Simulé. Ce module est de construire la structure de l'algorithme récuit simulé, celui qui permet d'être appelé par le module externe
 *@author: FANG Zhengjie & HUO Jiaxi
 *@created: 02/05/2020
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import city.City;

public class RequeteVille {
	private Connection connection;
	private String villeACherher;
	
	public RequeteVille(String ville) {
		this.villeACherher=ville;
		this.setConnection();
	}

	/*
	 * Initialisation de connectnexion
	 */
	public void setConnection() {
		//声明Connection对象 Déclarer objet Connection
        this.connection=null;
        //驱动程序名 actionneur
        String driver = "com.mysql.jdbc.Driver";
        //URL指向要访问的数据库名  URL pour la base de données, celle qui nous avons connectstruit pour stocker les données de villes
        String url = "jdbc:mysql://localhost:3306/villes_france";
        //MySQL配置时的用户名  Identifiant de la base de données
        String user = "root";
        //MySQL配置时的密码  Mot de passe de la base de données
        String password = "f19961104zj";
        

        try {
            //加载驱动程序
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库   Méthode getconnectnection() pour connectnecter à la base de données
            connection = DriverManager.getConnection(url,user,password);
        }catch(ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!"); 
            e.printStackTrace();
        }catch(SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace(); 
        }catch (Exception e) {
             e.printStackTrace();    
        }	
	}
	
	/*
	 * L'exécution de la requête de la base de données
	 */
	public City chercherVille() throws SQLException {
            //2.创建statement类对象，用来执行SQL语句！！
			City ville=null;
            String sql = "SELECT * FROM villes_france_free WHERE  ville_nom = ? ";
            PreparedStatement pstmt=this.connection.prepareStatement(sql);
            
            pstmt.setString(1,this.villeACherher.toUpperCase());
            
            //3.ResultSet类，用来存放获取的结果集       ResultSet: Pour stocker les résultats de la requête
            ResultSet res =pstmt.executeQuery();
            while(res.next()){
            	String villeName=res.getString("ville_nom");
//            	System.out.println(villeName);
	            double latitude=res.getDouble("ville_latitude_deg");
//	            System.out.println(latitude);
	            double longitude=res.getDouble("ville_longitude_deg");
//	            System.out.println(longitude);
	            ville=new City(this.villeACherher,latitude,longitude);
            }         
            res.close();
            connection.close();   
            return ville;
	}
	
	
	/*
	 * Pour tester la connexion à la base de données	
	 */
    public static void main(String[] args) {
        Connection con;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/villes_france";
        String user = "root";
        String password = "f19961104zj";
        
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url,user,password);
            if(!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");          
        }catch(ClassNotFoundException e) {
            System.out.println("Sorry,can`t find the Driver!"); 
            e.printStackTrace();
        }catch(SQLException e) {
            e.printStackTrace(); 
        }catch (Exception e) {
             e.printStackTrace();
            
        }finally{
            System.out.println("Succeeded taking the datas from the Database!");
            
        }

    }

}