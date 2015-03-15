package cn.edu.bjut.sse.wifi.server.calculateProbaility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.bjut.sse.wifi.server.dao.FingerPrintOption;
import cn.edu.bjut.sse.wifi.server.dao.SignalProbabilityOption;
import cn.edu.bjut.sse.wifi.server.model.LocationDetails;
import cn.edu.bjut.sse.wifi.server.model.WifiDetail;

/**
 * 将每个信号的值的概率值计算出来
 * @author rayjun
 *
 */
public class CalculationOfProbability {
	
	private FingerPrintOption fpo = null;  //指纹库的操作
	
	private SignalProbabilityOption spo = null; //概率库的操作
	
	
	
	
	public void calculateProByLocationType(){
		List<SignalProbability> pros = null;
		fpo = new FingerPrintOption();
		spo = new SignalProbabilityOption();
		
		//List<LocationDetails> locatios = fpo.getWifisFingerPrint();
		//List<String> types = fpo.getLocationType(locatios);
		
		List<String> types = fpo.getLocationType();  //取得所有的采集点的信息
		
		for(int i = 0;i < types.size();i++){
			//分别获取每个采集点所采集的样本
			List<LocationDetails> fps = fpo.getWifisFingerPrintByLocationType(types.get(i));
			pros = this.calculatePro(fps);
			spo.saveSignalPros(pros);
		}
		
	}
	
	/**
	 * 计算出每个位置的每个信号值的概率
	 * 
	 * 有的概率计算出来不为1是因为有的采集时这个ap点的信号值根本就没有采集到。
	 * 
	 * @return
	 */
	public List<SignalProbability> calculatePro(List<LocationDetails> lds){
		ArrayList<SignalProbability> pros = new ArrayList<SignalProbability>();
		//key为bssid
		HashMap<String, List<WifiRssType>> rssMap = new HashMap<String, List<WifiRssType>>();
		SignalProbability s = null;
		//对一个采集点的样本进行统计，统计是按ap点进行的，统计ap点发出的每一个信号在这个样本中出现的次数
		for(int i = 0;i < lds.size();i++){
			
			List<WifiDetail> wifis = lds.get(i).getWifiDetails();
			
			for(int j = 0;j < wifis.size();j++){
				
				if(rssMap.containsKey(wifis.get(j).getBSSID())){
					double currentRss = wifis.get(j).getRSS(); 
					List<WifiRssType> rssTypes = rssMap.get(wifis.get(j).getBSSID());
					boolean isContanin = false;
					for(int h = 0;h < rssTypes.size();h++){
						
						if(currentRss == rssTypes.get(h).getRss()){
							rssTypes.get(h).setCount(rssTypes.get(h).getCount()+1);
							isContanin = true;
							break;
						}
					}
					
					if(!isContanin){
						WifiRssType wifiRT = new WifiRssType();
						wifiRT.setBssid(wifis.get(j).getBSSID());
						wifiRT.setCount(1);
						wifiRT.setRss(currentRss);
						rssTypes.add(wifiRT);
					}else{
						isContanin = false;
					}
					
					
				}else {
					WifiRssType wifiRT = new WifiRssType();
					wifiRT.setBssid(wifis.get(j).getBSSID());
					wifiRT.setCount(1);
					wifiRT.setRss(wifis.get(j).getRSS());
					List<WifiRssType> wifiRssTypes = new ArrayList<CalculationOfProbability.WifiRssType>();
					wifiRssTypes.add(wifiRT);
					rssMap.put(wifis.get(j).getBSSID(), wifiRssTypes);
				}
				
			}
			
		}
		
		//根据上面所统计出来的次数除以样本的数量就是概率
		int totalSize = lds.size();
		String locationType = lds.get(0).getType();
		
		for(String key : rssMap.keySet()){
			List<WifiRssType> lists = rssMap.get(key);
			for(int i = 0;i < lists.size();i++){
				WifiRssType wifiType = lists.get(i);
				s = new SignalProbability();
				s.setBssid(wifiType.getBssid());
				s.setLocationType(locationType);
				s.setRss(wifiType.getRss());
				double pro = (double)wifiType.getCount()/totalSize;
				s.setProbability(pro);
				pros.add(s);
			}
		}
		
		
		return pros;
		
	} 
	
	/*
	private List<SignalProbability> calculEveryPro(LocationDetails ld){
		
		ArrayList<SignalProbability> signalPros = new ArrayList<SignalProbability>();
		SignalProbability s = null;
		
		WifiDetail wifi = null;
		
		HashMap<String, Integer>  rssSum = new HashMap<String, Integer>();
		
		ArrayList<WifiDetail> wifis = (ArrayList<WifiDetail>) ld.getWifiDetails();
		
		for(int i = 0; i < wifis.size();i++){
			wifi = wifis.get(i);
			
			if(rssSum.containsKey(wifi.getRSS()+"")){
				rssSum.put(wifi.getRSS()+"", rssSum.get(wifi.getRSS()+"")+1);
			}else {
				rssSum.put(wifi.getRSS()+"", 1);
			}
			
		}
		
		for(String key:rssSum.keySet()){
			s = new SignalProbability();
			//s.setBssid(bssid);
		}
		
		
		
		
		
		return signalPros;
		
	}
	*/
	
	/**
	 * 内部类，用来统计同一个位置的同一个ap点发出相等信号值的个数
	 * @author rayjun
	 *
	 */
	
	class WifiRssType{
		private double rss;
		
		private int count;
		
		private String bssid;

		public double getRss() {
			return rss;
		}

		public void setRss(double rss) {
			this.rss = rss;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String getBssid() {
			return bssid;
		}

		public void setBssid(String bssid) {
			this.bssid = bssid;
		}
		
		
	}
}
