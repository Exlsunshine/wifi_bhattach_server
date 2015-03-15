package cn.edu.bjut.sse.wifi.server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.edu.bjut.sse.wifi.server.calculateProbaility.SignalProbability;
import cn.edu.bjut.sse.wifi.server.model.LocationDetails;
import cn.edu.bjut.sse.wifi.server.model.WifiDetail;

public class DataBaseOption {
	
	private  Connection connection;
	
	public  Connection getConnection(){
				
		//Connection connection = null;
		
		String url = "jdbc:mysql://localhost:3306/wifi?useUnicode=true&characterEncoding=utf8"
				+ "&user=root&password=root";
		//String username = "root";
		//String password = "root";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			connection = DriverManager.getConnection(url);
			
			return connection;
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
			return null;
		}catch (SQLException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	public  int saveLocationDetails(LocationDetails ld,Connection connection){
		
		//Connection connection = DataBaseOption.getConnection();
		
		String sql = "";
		
		if(null != connection){
			try {
				Statement statement = connection.createStatement();
				
				sql = "insert into tb_location (x,y,floor,createTime,type) values("+ld.getX()+","+ld.getY()
						+","+ld.getFloor()+", now()"+",'"+ld.getType()+"')";
				
				statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
				
				ResultSet key = statement.getGeneratedKeys();
				
				key.next();
				int result = key.getInt(1);
				
				return result;
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return -1;
	}
	
	public int saveWifiInfo(List<WifiDetail> wifis,Connection connection,int locationId){
		
		String sql = "";
		
		if(null != connection){
			try {
				Statement statement = connection.createStatement();
				
				for(int i = 0;i < wifis.size();i++){
				
					sql = "insert into tb_wifiInfo (locationId,BSSID,SSID,RSS) values("
							+ locationId+",'"+wifis.get(i).getBSSID()+"','"
									+wifis.get(i).getSSID()+"',"+wifis.get(i).getRSS()+ ")";
					
					statement.execute(sql);
				}
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return 1;
		}
		
		
		return -1;
	}
	
	public int saveSignalPros(List<SignalProbability> pros,Connection connection){
		String sql = "";
		if(null != connection){
			try {
				Statement statement = connection.createStatement();
				
				for(int i = 0;i < pros.size();i++){
					sql = "insert into tb_signalpro (locationType,bssid,rss,probability) values('"+
				pros.get(i).getLocationType()+"','"+pros.get(i).getBssid()+"',"+pros.get(i).getRss()+","
						+pros.get(i).getProbability()+")";
					statement.execute(sql);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return 1;
		}
		return -1;
	}
	
	
	
	
	public List<LocationDetails> selectLocationDetails(Connection connection){
		List<LocationDetails> list = new ArrayList<LocationDetails>();
		LocationDetails ld = null;
		String locationSql = "";
		
		if(null != connection){
			
			try {
				Statement statement = connection.createStatement();
				
				locationSql = "select * from tb_location";
				
				ResultSet location = statement.executeQuery(locationSql);
				
				while(location.next()){
					ld = new LocationDetails();
					ld.setLocationId(location.getLong("locationId"));
					ld.setX(location.getInt("x"));
					ld.setY(location.getInt("y"));
					ld.setFloor(location.getInt("floor"));
					ld.setType(location.getString("type"));
					
					ld.setWifiDetails(this.selectWifiDetails(connection, ld.getLocationId()));
					
					list.add(ld);
				}	
				
				
				return list;
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
			
		}
		
		
		return null;
		
	}
	
	
	public List<WifiDetail> selectWifiDetails(Connection connection,long locationId){
		
		List<WifiDetail> wifis = new ArrayList<WifiDetail>();
		
		WifiDetail wifi = null;
		
		String wifiSql = "select * from tb_wifiInfo where locationId = "+locationId;
		
		if(null != connection){
			
			try {
				Statement statement = connection.createStatement();
				
				ResultSet wifiSet = statement.executeQuery(wifiSql);
				
				while (wifiSet.next()) {

					wifi  = new WifiDetail();
					
					wifi.setLocationId(locationId);
					wifi.setBSSID(wifiSet.getString("BSSID"));
					wifi.setRSS(wifiSet.getInt("RSS"));
					wifi.setSSID(wifiSet.getString("SSID"));
					wifi.setWifiId(wifiSet.getLong("wifiId"));
					
					wifis.add(wifi);
					
				}
				
				
				return wifis;
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return null;
		
	}
	
	
	/**
	 * 通过一个位置来查找相关的信息
	 * @param connection
	 * @param locationType
	 * @return
	 */
	public List<LocationDetails> selectLocationDetailsByType(Connection connection,String locationType){
		
		ArrayList<LocationDetails> list = new ArrayList<LocationDetails>();
		
		LocationDetails ld = null;
		String locationSql = "";
		
		if(null != connection){
			
			try {
				Statement statement = connection.createStatement();
				
				locationSql = "select * from tb_location where type= '"+locationType+"'";
				
				ResultSet location = statement.executeQuery(locationSql);
				
				while(location.next()){
					ld = new LocationDetails();
					ld.setLocationId(location.getLong("locationId"));
					ld.setX(location.getInt("x"));
					ld.setY(location.getInt("y"));
					ld.setFloor(location.getInt("floor"));
					ld.setType(location.getString("type"));
					
					ld.setWifiDetails(this.selectWifiDetails(connection, ld.getLocationId()));
					
					list.add(ld);
				}	
				
				
				return list;
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return null;
		
		
	}
	
	
	/**
	 * 取出所有计算好的概率值
	 */
	public List<SignalProbability> selectAllSignalPro(Connection connection){
		List<SignalProbability> pros = new ArrayList<SignalProbability>();
		SignalProbability signalPro = null;
		String sql = "";
		
		if(null != connection){
			Statement statement;
			try {
				statement = connection.createStatement();
				sql = "select * from tb_signalpro";
				
				ResultSet signalPros = statement.executeQuery(sql);
				while(signalPros.next()){
					signalPro = new SignalProbability();
					
					signalPro.setBssid(signalPros.getNString("bssid"));
					signalPro.setId(signalPros.getInt("id"));
					signalPro.setLocationType(signalPros.getNString("locationType"));
					signalPro.setProbability(signalPros.getDouble("probability"));
					signalPro.setRss(signalPros.getDouble("rss"));
					
					pros.add(signalPro);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
			
			return pros;
		}
		
		return null;
		
	}
	
	
	/**
	 * 通过采集点的信息来取出一个样本点的概率信息
	 * @param locationType
	 * @param connection
	 * @return
	 */
	public List<SignalProbability> selectSignalProsByLocationType(String locationType,Connection connection){
		List<SignalProbability> pros = new ArrayList<SignalProbability>();
		SignalProbability signalPro = null;
		String sql = "";
		
		if(null != connection){
			Statement statement;
			try {
				statement = connection.createStatement();
				sql = "select * from tb_signalpro where locationType = "+locationType;
				
				ResultSet signalPros = statement.executeQuery(sql);
				while(signalPros.next()){
					signalPro = new SignalProbability();
					
					signalPro.setBssid(signalPros.getNString("bssid"));
					signalPro.setId(signalPros.getInt("id"));
					signalPro.setLocationType(signalPros.getNString("locationType"));
					signalPro.setProbability(signalPros.getDouble("probability"));
					signalPro.setRss(signalPros.getDouble("rss"));
					
					pros.add(signalPro);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
			
			return pros;
		}
		return null;
		
	}
	
	
	/*
	 * 查出所有的位置信息
	 */
	public List<String> selectLocationType(Connection connection){
		List<String> types = new ArrayList<String>();
		
		String sql = "";
		
		if(null != connection){
			Statement statement = null;
			
			try {
				statement = connection.createStatement();
				
				sql = "select distinct type from tb_location";
				
				ResultSet typeSet = statement.executeQuery(sql);
				
				while (typeSet.next()) {
					types.add(typeSet.getString("type"));
				}
				
			} catch (SQLException e) {

				e.printStackTrace();
				
				return null;
			}
		}
		
		
		return types;
	}
	
	
	public void closeConnection(){
		
		if(null != connection){
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
}
