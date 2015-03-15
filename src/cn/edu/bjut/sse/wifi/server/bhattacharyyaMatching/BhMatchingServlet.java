package cn.edu.bjut.sse.wifi.server.bhattacharyyaMatching;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.bjut.sse.wifi.server.bhattacharyya.BhattacharyyaDistance;
import cn.edu.bjut.sse.wifi.server.bhattacharyyaMatching.service.BhMatchingService;
import cn.edu.bjut.sse.wifi.server.model.LocationDetails;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author rayjun
 * 巴氏匹配的接入口
 * 用户发起定位请求之后,将采集到的信号值传到服务器端。这个 servlet 将处理这个请求。
 *
 */


public class BhMatchingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BhMatchingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		BhMatchingService bhservice = new BhMatchingService();
		
		response.setContentType("text/html");

        response.setCharacterEncoding("UTF-8");
        
        
        String wifiJsonObject = request.getParameter("wifiJson");
        
        //LocationDetails ld = (LocationDetails) JSONObject.parseObject(wifiJsonObject,LocationDetails.class);
        
        JSONArray locationArray = JSONObject.parseArray(wifiJsonObject);
        
        BhattacharyyaDistance bh = bhservice.matching(locationArray);
        
        LocationDetails opsition = null;
        
        
        PrintWriter out = response.getWriter();

        
        
        out.write("location is "+bh.getLocationType());
       // out.write(new String("location is: x is"+opsition.getX()+", y is"+opsition.getY()+"\n"));

        out.flush();

        out.close();
	}

}
