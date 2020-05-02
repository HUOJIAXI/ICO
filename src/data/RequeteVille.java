package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import city.City;

public class RequeteVille {
	private Connection con;
	private String villeACherher;
	
	public RequeteVille(String ville) {
		this.villeACherher=ville;
		this.setConnection();
	}

	public void setConnection() {
		//声明Connection对象
        this.con=null;
        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        //URL指向要访问的数据库名studb
        String url = "jdbc:mysql://localhost:3306/villes_france";
        //MySQL配置时的用户名
        String user = "root";
        //MySQL配置时的密码
        String password = "f19961104zj";
        
        //遍历查询结果集
        try {
            //加载驱动程序
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url,user,password);
            
//            if(!con.isClosed())
//                System.out.println("Succeeded connecting to the Database!");
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
	
	public City chercherVille() throws SQLException {
            //2.创建statement类对象，用来执行SQL语句！！
			City ville=null;
            String sql = "SELECT * FROM villes_france_free WHERE  ville_nom = ? ";
            PreparedStatement pstmt=this.con.prepareStatement(sql);
            
            pstmt.setString(1,this.villeACherher.toUpperCase());
            
            //3.ResultSet类，用来存放获取的结果集！！
            ResultSet res =pstmt.executeQuery();
//            System.out.println("执行一次查找");
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
            con.close();   
            return ville;
	}
	
	
	
    public static void main(String[] args) {
        //声明Connection对象
        Connection con;
        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        //URL指向要访问的数据库名studb
        String url = "jdbc:mysql://localhost:3306/villes_france";
        //MySQL配置时的用户名
        String user = "root";
        //MySQL配置时的密码
        String password = "f19961104zj";
        
        //遍历查询结果集
        try {
            
            //加载驱动程序
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url,user,password);
            
            if(!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");
//            //2.创建statement类对象，用来执行SQL语句！！
//            Statement statement = con.createStatement();
//            //要执行的SQL语句
//            String sql = "select ";
//            
//            //3.ResultSet类，用来存放获取的结果集！！
//            ResultSet rs = statement.executeQuery(sql);
//            System.out.println("-----------------");
//            System.out.println("执行结果如下所示:"); 
//            System.out.println("-----------------"); 
//            System.out.println("姓名" + "\t" + "地址"); 
//            System.out.println("-----------------");  
//            
//            String address = null;
//            
//            String name = null;
//            
//            while(rs.next()){
//                //获取sname这列数据
//                name = rs.getString("sname");
//                //获取address这列数据
//                address = rs.getString("address");
//                //输出结果
//                System.out.println(name + "\t" + address);
//                
//            }
//            rs.close();
//            con.close();
//            
            
        }catch(ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!"); 
            e.printStackTrace();
        }catch(SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace(); 
        }catch (Exception e) {
             e.printStackTrace();
            
        }finally{
            System.out.println("数据库数据成功获取！！");
            
        }

    }

}