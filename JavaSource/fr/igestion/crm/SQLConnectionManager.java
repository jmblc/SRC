package fr.igestion.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class SQLConnectionManager {
	
	private static int maxActivesConnections = 10;
	
	private static ThreadLocal<Connection> localConnection = new ThreadLocal<Connection>();
    
    private static ThreadLocal<HashMap<Integer, PreparedStatement>> localStatements = new ThreadLocal<HashMap<Integer, PreparedStatement>>();
    
    private static ThreadLocal<Boolean> inProcess = new ThreadLocal<Boolean>() {
    		 @Override protected Boolean initialValue() {
                 return Boolean.valueOf(false);
    		 }
    };
    
    
    private static int nbActivesConnections = 0;
    
    
    static void addConnection() {
    	nbActivesConnections++;
    	if (nbActivesConnections > maxActivesConnections) {
    		System.out.println(">> Attention ! " + nbActivesConnections + " connexions actives !");
    		StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
    		for(int i=0; i<10 && i<traceElements.length; i++) {
    			System.out.println(traceElements[i].getClassName() + "#" + traceElements[i].getMethodName());
    		}
    	}
    }
    
    static void removeConnection() {
    	if (nbActivesConnections > maxActivesConnections) {
    		System.out.println(">> OK ! suppression d'une connexion : reste " + (nbActivesConnections-1) + " connexion(s) active(s) ");
    		StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
    		for(int i=0; i<10 && i<traceElements.length; i++) {
    			System.out.println(traceElements[i].getClassName() + "#" + traceElements[i].getMethodName());
    		}
    	}
    	nbActivesConnections--;    	
    }

    
    public static Connection getConnection() {
    	
    	Connection localCon = localConnection.get();
    	Connection connection;

        try {
        	
			if (localCon == null || localCon.isClosed() || !inProcess.get()) {
				
				Context initContext = new InitialContext();
				Context envContext = (Context) initContext.lookup("java:/comp/env");
				DataSource ds = (DataSource) envContext.lookup("jdbc/hcontacts");
				connection = ds.getConnection();
				
				if (inProcess.get()) {
					if (localCon != null) {
						end();
					}
					localConnection.set(connection);
				}
				addConnection();
				
			} else {
				connection = localCon;
			}
		} catch (Exception e) {
//			try {
//				if (connection == null || connection.isClosed()) {
//					end();
//					OracleDataSource ds = new OracleDataSource();
//					ds.setURL("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=higeged1.svc.ext.tdc)(PORT=1522)))(CONNECT_DATA=(SERVICE_NAME = HIGEGED1)))");
//					ds.setUser("U_HCONTACTS");
//					ds.setPassword("U_HCONTACTS");
//					connection = ds.getConnection();
//					localConnection.set(connection);
//				}
//			} catch (Exception e2) {
				throw new IContactsException("getConnexion", e);
//			}
		}
        
        return connection;

    }
    
    
    public static void setConnection(Connection connection) throws Exception {
    	
    	end();
    	localConnection.set(connection);
    	
    }
    
    private static void setInProcess(boolean inProcess) {
    	SQLConnectionManager.inProcess.set(Boolean.valueOf(inProcess));
    }
    
    public static boolean isInProcess(Connection connection) {
    	
    	if (!inProcess.get()) {
    		return false;    		
		} else {
			return isSameConnection(connection);
		}
    }
    
    private static boolean isSameConnection(Connection connection) {
    	
    	Connection currentConnection = localConnection.get();
		if (currentConnection == null) {
			return false;
		} else {
			boolean connectionsEgales = currentConnection.equals(connection);
			boolean connectionsIdentiques = currentConnection == connection;
			return (connectionsIdentiques || connectionsEgales);
		}
    }
    
    
    public static void begin() throws Exception {    	
    	
    	setInProcess(true);
    	Connection con = getConnection();
    	if (con != null) {
    		con.setAutoCommit(false);
    	}
    }
    
    public static void commit() throws SQLException {
    	
    	Connection con = localConnection.get();
		if (con != null && !con.isClosed() && !con.getAutoCommit()) {
			con.commit();
		}
    	
    }
    
    public static void rollback() throws SQLException {
    	
    	Connection con = localConnection.get();
		if (con != null && !con.isClosed() && !con.getAutoCommit()) {
			con.rollback();
		}
    	
    }
    
    public static void end() throws Exception {
    	end(true);
    }
    
    public static void end(boolean isOK) throws Exception {
    	
    	if (isOK) {
			commit();
		} else {
			rollback();
		}
    	
    	releaseStatements();
    	
    	Connection con = localConnection.get();    	
		if (con != null && !con.isClosed()) {
			con.close();
			removeConnection();
		}		
		localConnection.set(null);
		
		setInProcess(false);
    }
    
    public static void releaseStatements() throws SQLException {
    	
    	HashMap<Integer, PreparedStatement> stmts = localStatements.get();
		if (stmts != null) {
			for (PreparedStatement stmt : stmts.values()) {
				try {
					stmt.close();
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
			}
			localStatements.set(null);
		}
    }
    
    public static boolean hasStatementInProcess(Connection connection) throws SQLException {
    	
    	if (!isSameConnection(connection)) {
    		return false;
		} else {
			HashMap<Integer, PreparedStatement> stmts = localStatements.get();
			if (stmts == null || stmts.size() == 0) {
				return false;
			} else {
				return true;
			}
		}
    }
    
    public static PreparedStatement prepareStatement(Connection connection, String sql) throws SQLException {
    	
    	PreparedStatement stmt = null;
    	
    	if (connection != null && !isSameConnection(connection)) {
    		stmt = connection.prepareStatement(sql);
    		
		} else {

			Integer code = Integer.valueOf(sql.hashCode());
			HashMap<Integer, PreparedStatement> stmts = localStatements.get();

			if (stmts == null) {
				stmts = new HashMap<Integer, PreparedStatement>();
				localStatements.set(stmts);
			}

			stmt = stmts.get(code);
			if (stmt == null) {
				stmt = connection.prepareStatement(sql);
				stmts.put(code, stmt);
			}
		}
    	
    	return stmt;
    }
    
	public static boolean releaseStatement(PreparedStatement preparedStatement) throws SQLException {

		boolean retour = false;
		
		HashMap<Integer, PreparedStatement> stmts = localStatements.get();
		if (stmts != null) {
			for (Integer code : stmts.keySet()) {
				PreparedStatement stmt = stmts.get(code);
				if (stmt == preparedStatement) {
					stmt.close();
					stmts.remove(code);
					retour = true;
					break;
				}
			}
		}

		return retour;
	}
    

}
