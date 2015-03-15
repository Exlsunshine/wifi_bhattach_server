package cn.edu.bjut.sse.wifi.server.bhattacharyya;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.bjut.sse.wifi.server.calculateProbaility.SignalProbability;

/**
 * 计算巴氏系数
 * @author rayjun
 *
 */
public class Calculation {
	
	
	
	/*
	public List<BhattacharyyaCoefficient> calculateBc(ArrayList<OffLinePro> offLineList,
			ArrayList<OnLinePro> onLineList){
		
		ArrayList<BhattacharyyaCoefficient> bcList = null;
		
		
		return bcList;
		
	}*/
	
	
	/*
	public List<BhattacharyyaCoefficient> calculateBc(Map<String, List<OffLinePro>> offLinePros,
			List<OnLinePro> onLinePros){
		ArrayList<BhattacharyyaCoefficient> bcList = null;
		
		
		return bcList;
		
	}*/
	
	
	/**
	 * 计算巴氏系数，将线上采集到的信号计算出概率存在一个list里面，然后将这个与线下库里面的系统的信号概率进行计算
	 * @param offLineProsMap 线下库的概率。string 是bssid
	 * @param onLinePros 实时采集到的信息计算出来的概率（）
	 * @return
	 */
	public Map<String, List<BhattacharyyaCoefficient>> calculateBcMap(Map<String, List<SignalProbability>> offLineProsMap,
			List<SignalProbability> onLinePros){
		HashMap<String, List<BhattacharyyaCoefficient>> bcMap = new HashMap<String, List<BhattacharyyaCoefficient>>();
		List<BhattacharyyaCoefficient> bcList = null;
		BhattacharyyaCoefficient bc = null;
		SignalProbability offLinePro = null;
		SignalProbability onLinePro = null;
		
		List<SignalProbability> offList = null;
		
		
		for(String locationType:offLineProsMap.keySet()){
			
			offList = offLineProsMap.get(locationType);
			
			List<BhattacharyyaCoefficient> results = this.calculateBcOfLocationType(offList, onLinePros);
			
			bcMap.put(locationType, results);
		}
		
		/*
		for(String key : offLineProsMap.keySet()){
			bcList = new ArrayList<BhattacharyyaCoefficient>();
			bc = new BhattacharyyaCoefficient();
			offList = offLineProsMap.get(key);
			
			for(int i = 0;i < onLinePros.size();i++){
				
				onLinePro = onLinePros.get(i);
				
				for(int j = 0;j < offList.size();j++){
					
					offLinePro = offList.get(j);
					
					if(onLinePro.getBssid().equals(offLinePro.getBssid())){
						
						if(bcList.size() == 0){
							bc.setBssid(onLinePro.getBssid());
							bc.setLocationType(key);
							bc.setbCoefficient(Math.sqrt(onLinePro.getProbability()*offLinePro.getProbability()));
						}else {
							bc.setbCoefficient(bc.getbCoefficient()+Math.sqrt(onLinePro.getProbability()*offLinePro.getProbability()));
						}
					}
					
				}
				bcList.add(bc);
				
			}
			bcMap.put(key, bcList);
			
		}
		*/
		
		
		return bcMap;
	}
	
	/**
	 * 利用各个巴氏系数来计算出所测点到各个采集点的巴士距离
	 * @param bcMap  巴氏系数的集合
	 * @return
	 */
	public List<BhattacharyyaDistance> calculateDistance(Map<String, List<BhattacharyyaCoefficient>> bcMap){
		
		ArrayList<BhattacharyyaDistance> distanceList = new ArrayList<BhattacharyyaDistance>();
		BhattacharyyaDistance bd = null;
		ArrayList<BhattacharyyaCoefficient> bcList = null;
		int count = 0;
		
		for(String key :bcMap.keySet()){
			bd = new BhattacharyyaDistance();
			bcList = (ArrayList<BhattacharyyaCoefficient>) bcMap.get(key);
			double bcSum = 0;
			
			for(int i = 0;i < bcList.size();i++){
				if(null != bcList.get(i)){
					bcSum += bcList.get(i).getbCoefficient();
					count++;
				}
			}
			
			if (bcSum > 0) {
				bcSum = bcSum/count;
				count= 0;
				bd.setDistance(-Math.log(bcSum));
			}else {
				bd.setDistance(-100);
			}
			bd.setLocationType(key);
			
			distanceList.add(bd);
		}
		
		
		
		return distanceList;
		
		
		
	}
	
	
	
	/**
	 * 计算一个特定的位置的巴氏系数的集合
	 * @param offLine
	 * @param onLine
	 * @return
	 */
	private List<BhattacharyyaCoefficient> calculateBcOfLocationType(List<SignalProbability> offLine,List<SignalProbability> onLine){
		
		List<BhattacharyyaCoefficient> bcList = new ArrayList<BhattacharyyaCoefficient>();
		
		HashMap<String, List<SignalProbability>> classifyMap = new HashMap<String, List<SignalProbability>>();
		HashMap<String, List<SignalProbability>> onClassifyMap = new HashMap<String, List<SignalProbability>>();
		
		List<SignalProbability> mapList = null;
		
		for(int i = 0;i < offLine.size();i++){
			if(classifyMap.containsKey(offLine.get(i).getBssid())){
				classifyMap.get(offLine.get(i).getBssid()).add(offLine.get(i));
			}else {
				mapList = new ArrayList<SignalProbability>();
				mapList.add(offLine.get(i));
				classifyMap.put(offLine.get(i).getBssid(), mapList);
			}
			
		}
		
		for(int i = 0;i < onLine.size();i++){
			if(onClassifyMap.containsKey(onLine.get(i).getBssid())){
				onClassifyMap.get(onLine.get(i).getBssid()).add(onLine.get(i));
			}else {
				mapList = new ArrayList<SignalProbability>();
				mapList.add(onLine.get(i));
				onClassifyMap.put(onLine.get(i).getBssid(), mapList);
			}
			
		}
		
		for(String bssidString:onClassifyMap.keySet()){
			List<SignalProbability> onSignOfAp = onClassifyMap.get(bssidString);
			
			for(String offBssidString:classifyMap.keySet()){
				List<SignalProbability> offSignOfAp = classifyMap.get(offBssidString);
				
				if(offSignOfAp.get(0).getBssid().equals(onSignOfAp.get(0).getBssid())){
					BhattacharyyaCoefficient result = this.calculateBcOfAP(offSignOfAp, onSignOfAp);
					bcList.add(result);
				}
			}
			
		}		
		
		return bcList ;
	}
	
	/**
	 * 计算出某个位置一个ap的巴氏系数
	 * @param offL
	 * @param onL
	 * @return
	 */
	private BhattacharyyaCoefficient calculateBcOfAP(List<SignalProbability> offL,List<SignalProbability> onL){
		BhattacharyyaCoefficient bc = null;
		SignalProbability onSign = null;
		SignalProbability offSign = null;
		
		
		for(int i = 0;i < onL.size();i++){
			onSign = onL.get(i);
			
			for(int j = 0;j < offL.size();j++){
				offSign = offL.get(j);
				
				if(onSign.getBssid().equals(offSign.getBssid())&&(onSign.getRss() == offSign.getRss())){
					
					if(null == bc){
						bc = new BhattacharyyaCoefficient();
						bc.setBssid(onSign.getBssid());
						bc.setLocationType(onSign.getLocationType());
						bc.setbCoefficient(Math.sqrt(onSign.getProbability()*offSign.getProbability()));
					}else {
						bc.setbCoefficient(bc.getbCoefficient()+Math.sqrt(onSign.getProbability()*offSign.getProbability()));
					}
					
				}
			}
			
		}
		
		
		return bc;
		
		
	}
}
