package cn.edu.bjut.sse.wifi.server.bhattacharyya;


/**
 * 线下采集，
 * 一个特定点的一个ap点的信号值出现的概率。 
 * @author rayjun
 *
 */
public class OffLinePro {
	private String bssid;	
	
	private float rss;
	
	private float probability;
	
	private String locationType;
	

	public OffLinePro(String bssid, float rss, float probability,
			String locationType) {
		super();
		this.bssid = bssid;
		this.rss = rss;
		this.probability = probability;
		this.locationType = locationType;
	}

	public String getBssid() {
		return bssid;
	}

	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	public float getRss() {
		return rss;
	}

	public void setRss(float rss) {
		this.rss = rss;
	}

	public float getProbability() {
		return probability;
	}

	public void setProbability(float probability) {
		this.probability = probability;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	
	
}
