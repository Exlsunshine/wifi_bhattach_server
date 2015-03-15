package cn.edu.bjut.sse.wifi.server.bhattacharyya;

/**
 * 通过线下库与线上定位采集的计算得出的巴氏系数
 * @author rayjun
 *
 */

public class BhattacharyyaCoefficient {	
	
	private String bssid;
	
	private String locationType;
	
	private double bCoefficient;

	public String getBssid() {
		return bssid;
	}

	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public double getbCoefficient() {
		return bCoefficient;
	}

	public void setbCoefficient(double bCoefficient) {
		this.bCoefficient = bCoefficient;
	}

	
	
}
