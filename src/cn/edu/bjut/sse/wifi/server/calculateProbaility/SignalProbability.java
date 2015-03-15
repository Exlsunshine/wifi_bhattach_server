package cn.edu.bjut.sse.wifi.server.calculateProbaility;

import java.util.Date;


/**
 * 每个信号的概率值
 * @author rayjun
 *
 */
public class SignalProbability {
	
	private int id;
	
	private String locationType;
	
	private String bssid;
	
	private double rss;
	
	private double probability;
	
	private Date genTime;
	
	public SignalProbability(int id, String locationType, String bssid,
			double rss, double probability, Date genTime) {
		super();
		this.id = id;
		this.locationType = locationType;
		this.bssid = bssid;
		this.rss = rss;
		this.probability = probability;
		this.genTime = genTime;
	}

	public SignalProbability() {
		super();
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getBssid() {
		return bssid;
	}

	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	public double getRss() {
		return rss;
	}

	public void setRss(double rss) {
		this.rss = rss;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public Date getGenTime() {
		return genTime;
	}

	public void setGenTime(Date genTime) {
		this.genTime = genTime;
	}
	
	
}
