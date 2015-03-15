package cn.edu.bjut.sse.wifi.server.dao;

import java.sql.Connection;
import java.util.List;

import cn.edu.bjut.sse.wifi.server.model.LocationDetails;

public class FingerPrintOption {
	
	private DataBaseOption dbo = null;
	
	
	/**
	 * 找出总共有哪些采集点
	 * 方法已重写，之前是从所有记录里面取，这次是直接从数据库里面查询。
	 * @param lds
	 * @return
	 */
	public List<String> getLocationType(){
		
		
		List<String> types = null;
		
		dbo = new DataBaseOption();
		Connection connection = dbo.getConnection();
		
		types = dbo.selectLocationType(connection);
		/*
		for(int i = 0;i < lds.size();i++){
			if(!types.contains(lds.get(i).getType())){
				types.add(lds.get(i).getType());
			}
		}*/
		dbo.closeConnection();
		return types;
		
	}
	
	
	/**
	 * 取出指纹库，公用功能
	 * @return
	 */
	public List<LocationDetails> getWifisFingerPrint(){
		
		List<LocationDetails> fingerPrintLists = null;
		
		dbo  = new DataBaseOption();
		
		Connection connection = dbo.getConnection();
		
		fingerPrintLists = dbo.selectLocationDetails(connection);
		
		/*
		for(int i = 0;i < fingerPrintLists.size();i++){
			Collections.sort(fingerPrintLists.get(i).getWifiDetails(), wifiComparator);
		}*/
		
		
		dbo.closeConnection();
		
		return fingerPrintLists;
		
	}
	
	public List<LocationDetails> getWifisFingerPrintByLocationType(String type){
		List<LocationDetails> fps = null;
		dbo = new DataBaseOption();
		
		Connection connection = dbo.getConnection();
		
		fps = dbo.selectLocationDetailsByType(connection, type);
		
		dbo.closeConnection();
		
		return fps;
	}
	
	
	
}
