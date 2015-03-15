package cn.edu.bjut.sse.wifi.server.bhattacharyyaMatching.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.edu.bjut.sse.wifi.server.bhattacharyya.BhattacharyyaCoefficient;
import cn.edu.bjut.sse.wifi.server.bhattacharyya.BhattacharyyaDistance;
import cn.edu.bjut.sse.wifi.server.bhattacharyya.Calculation;
import cn.edu.bjut.sse.wifi.server.calculateProbaility.CalculationOfProbability;
import cn.edu.bjut.sse.wifi.server.calculateProbaility.SignalProbability;
import cn.edu.bjut.sse.wifi.server.dao.DataBaseOption;
import cn.edu.bjut.sse.wifi.server.dao.FingerPrintOption;
import cn.edu.bjut.sse.wifi.server.dao.SignalProbabilityOption;
import cn.edu.bjut.sse.wifi.server.model.LocationDetails;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BhMatchingService {
	private DataBaseOption dbo = null;
	
	private SignalProbabilityOption spo = null;
	
	private FingerPrintOption fpo = null;
	
	private List<BhattacharyyaDistance> distances = null;
	
	private List<SignalProbability> offList = null;
	
	private Map<String, List<SignalProbability>> offMap = null;
	
	private Calculation cl = null;
	
	private Map<String, List<BhattacharyyaCoefficient>> bcMap = null;
	
	private CalculationOfProbability cop = null;
	
	public List<SignalProbability> getOnLineSignalPros(JSONArray ldsArray){
		
		List<SignalProbability> list = null;
		List<LocationDetails> lds = new ArrayList<LocationDetails>();
		LocationDetails ld = null;
		
		for(int i = 0;i < ldsArray.size();i++){
			ld = (LocationDetails)JSONObject.parseObject(ldsArray.get(i).toString(),LocationDetails.class);
			
			lds.add(ld);
		}
		
		cop = new CalculationOfProbability();
		
		list = cop.calculatePro(lds);
				
		return list;
	}
	 
	public BhattacharyyaDistance matching(JSONArray ldsArray){
		List<SignalProbability> onLineSignalPro = null;
		spo = new SignalProbabilityOption();
		fpo = new FingerPrintOption();
		cl = new Calculation();
		
		onLineSignalPro = this.getOnLineSignalPros(ldsArray);
		
		/*
		offList = spo.getAllSignalPros();
		offMap = spo.signalProsClassify(offList);
		
		bcMap = cl.calculateBcMap(offMap, onLineSignalPro);
		
		distances = cl.calculateDistance(bcMap);
		*/
		
		List<String> types = fpo.getLocationType();
		
		for(int i = 0; i < types.size();i++){
			offList = spo.getSignalProsBylocationType(types.get(i));
			offMap = spo.signalProsClassify(offList);
			
			bcMap = cl.calculateBcMap(offMap, onLineSignalPro);
			distances.addAll(cl.calculateDistance(bcMap));
		}
		
		BhattacharyyaDistance bh = distances.get(0);
		
		for(int i = 0;i < distances.size();i++){
			
			if(distances.get(i).getDistance() < bh.getDistance()){
				bh = distances.get(i);
			}
			System.out.println(distances.get(i).getLocationType()+" distance is "+distances.get(i).getDistance());
		}
		System.out.println("\n");
		
		return bh;
	}
}
