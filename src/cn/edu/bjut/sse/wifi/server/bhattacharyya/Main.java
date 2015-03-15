package cn.edu.bjut.sse.wifi.server.bhattacharyya;

import java.util.List;
import java.util.Map;

import cn.edu.bjut.sse.wifi.server.calculateProbaility.SignalProbability;
import cn.edu.bjut.sse.wifi.server.dao.SignalProbabilityOption;

public class Main {
	public static void main(String[] args) {
		Map<String, List<SignalProbability>> offMap = null;
		List<SignalProbability> onList = null;
		List<SignalProbability> offList = null;
		Map<String, List<BhattacharyyaCoefficient>> bcMap = null;
		List<BhattacharyyaDistance> distance = null;
		SignalProbabilityOption spo = new SignalProbabilityOption();
		
		offList = spo.getAllSignalPros();
		offMap = spo.signalProsClassify(offList);
		
		for(String key : offMap.keySet()){
			System.out.println(key);
		}
		
		onList = offMap.get(offMap.keySet().iterator().next());
		
		Calculation cl = new Calculation();
		
		bcMap = cl.calculateBcMap(offMap, onList);
		
		distance = cl.calculateDistance(bcMap);
		
		for(int i = 0;i < distance.size();i++){
			System.out.println(distance.get(i).getLocationType() +","+distance.get(i).getDistance());
		}
		
	}
}
