package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.rowset.CachedRowSet;
import com.sun.rowset.CachedRowSetImpl;


public class DBUtilitaires {
	
		private static final String JDBC_DRIVER="com.mysql.cj.jdbc.Driver";
		private static Connection connection=null;
		
		//REMPLACER etudiant par le nom de ta base de données
		private static final String connStr="jdbc:mysql://lessonplus.com:3306/lessonpl_max?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC";
			
				
		public static void dbConnect() throws SQLException,ClassNotFoundException
		{
			try
			{
				Class.forName(JDBC_DRIVER);
			}catch(ClassNotFoundException e)
			{
			System.out.println("Le pilote mysql n'a pas été trouvé");
			e.printStackTrace();
			throw e;
			}
			
			
			try {
				//!!!!! METTRE LE NOM D'USAGER ET MOT DE PASSE DE TA BASE DE DONNÉES
				connection=DriverManager.getConnection(connStr, "lessonpl_max", "monPass123!");
			}
			catch(SQLException e)
			{
				System.out.println("La connection n'a pas réussi..Consulter la console " +e);
				throw e;
			}
		}
		/*===================================================================
		 * Fermeture de la connection si elle est ouverte
		 * ===================================================================
		 */
		public static void dbDisconnect() throws SQLException
		{
			try
			{
				if(connection != null && !connection.isClosed())
				{
					connection.close();
				}
			}
			catch(Exception e)
			{
				throw e;
			}
		}
		
		/*===================================================================
		 * Ajouter , Modifier, Effacer
		 * ===================================================================
		 */
		public static void dbExecuteQuery(String sqlStmt) throws SQLException, ClassNotFoundException
		{
			Statement stmt=null;
			try
			{
				dbConnect();
				stmt=connection.createStatement();
				stmt.executeUpdate(sqlStmt);
			}catch(SQLException e)
			{
				System.out.println("Problème lors de l'exécutions de la requete "+ e);
				throw e;
			}
			finally {
				if(stmt!=null)
					stmt.close();
			}
			dbDisconnect();
		}
		
		/*===================================================================
		 * Lecture et recupération des données
		 * ===================================================================
		 */
		public static ResultSet dbExecute(String sqlQuery) throws ClassNotFoundException, SQLException
		{
			Statement stmt=null;
			ResultSet rs=null;
			CachedRowSet crs=null;
			try
			{
				dbConnect();
				stmt=connection.createStatement();
				rs=stmt.executeQuery(sqlQuery); 
				crs=new CachedRowSetImpl();
				crs.populate(rs);
						
			}catch(SQLException e)
			{
				System.out.println("Erreur lors de l'exécution de l'opération dbExecute " + e);
				throw e;
			}
			finally 
			{
				if(rs !=null)
				{
					rs.close();
				}
				if(stmt !=null)
				{
					stmt.close();
				} 
				dbDisconnect();
			}
			return crs; 
		}
		
	}

	
	
	
	
	
	
	
	
	
	


