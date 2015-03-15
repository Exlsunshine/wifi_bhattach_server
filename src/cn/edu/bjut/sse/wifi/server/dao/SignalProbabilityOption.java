package cn.edu.bjut.sse.wifi.server.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.bjut.sse.wifi.server.calculateProbaility.SignalProbability;

public class SignalProbabilityOption {
	private DataBaseOption dbo = null;
	
	/**
	 * 存储计算出的每个信号的的概率值
	 * @param pros
	 * @return
	 */
	public int saveSignalPros(List<SignalProbability> pros){
		dbo = new DataBaseOption();
		Connection connection = dbo.getConnection();
		dbo.saveSignalPros(pros, connection);
		
		dbo.closeConnection();
		
		return 1;
	}
	
	
	public List<SignalProbability> getAllSignalPros(){
		
		List<SignalProbability> signalPros = null;
		dbo = new DataBaseOption();
		Connection connection = dbo.getConnection();
		
		signalPros = dbo.selectAllSignalPro(connection);
		
		dbo.closeConnection();
		
		return signalPros;
	}
	
	
	public List<SignalProbability> getSignalProsBylocationType(String locationType){
		List<SignalProbability> pros = null;
		
		dbo = new DataBaseOption();
		Connection connection = dbo.getConnection();
		
		pros = dbo.selectSignalProsByLocationType(locationType, connection);
		
		dbo.closeConnection();
		
		return pros;
	}
	
	
	/**
	 *
	 * @param signalPros
	 * @return
	 */
	public Map<String, List<SignalProbability>> signalProsClassify(List<SignalProbability> signalPros){
		
		HashMap<String, List<SignalProbability>> signalProMap = new HashMap<String, List<SignalProbability>>();
		List<SignalProbability> classifyPros = null;
		
		for(int i = 0;i < signalPros.size();i++){
			
			
			if(signalProMap.containsKey(signalPros.get(i).getLocationType())){
				signalProMap.get(signalPros.get(i).getLocationType()).add(signalPros.get(i));
			}else {
				classifyPros = new ArrayList<SignalProbability>();
				classifyPros.add(signalPros.get(i));
				signalProMap.put(signalPros.get(i).getLocationType(),classifyPros);
			}
			
			/*
			if(signalProMap.containsKey(signalPros.get(i).getBssid())){
				signalProMap.get(signalPros.get(i).getBssid()).add(signalPros.get(i));
			}else {
				classifyPros = new ArrayList<SignalProbability>();
				classifyPros.add(signalPros.get(i));
				signalProMap.put(signalPros.get(i).getBssid(),classifyPros);
			}
			*/
		}
		
		
		return signalProMap;
		
	}
}
