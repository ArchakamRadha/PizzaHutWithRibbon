package PizzaHut.PizzaHut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/pizzas")
public class StoreInfo {
	
	@Autowired
	ArrayList<Pizza> pizzas;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate rtemp=new RestTemplate();
		return rtemp;
	}
	
	@Bean
	public ArrayList<Pizza> pizzas(){
		ArrayList<Pizza> arr=new ArrayList<Pizza>();
		Pizza p=new Pizza();
		p.pizzaName="veg";
		p.pizzaType="medium";
		Pizza p1=new Pizza();
		p1.pizzaName="cheese";
		p1.pizzaType="Large";
		arr.add(p1);
		arr.add(p);
		return arr;
		
	}
	/*public StoreInfo() {
		pizzas=new ArrayList<Pizza>();
		Pizza p;
		p=new Pizza("veg","paneer pizza");
		p=new Pizza("veg","Corn Pizza");
		p=new Pizza("veg","Onion Cheese Pizza");
		p=new Pizza("non-veg","Chicken Pizza");
		p=new Pizza("non-veg","Chicken-Tikka Pizza");
		pizzas.add(p);
	}*/
	
	@RequestMapping(value="/info")
	public String getStoreInfo(HttpServletRequest  request, HttpServletResponse response)
	{
		/*String res="<html><body><B>Instance Name : " + request.getLocalName() + "<BR>";
		res += "<B>Port : </B>" +  + request.getLocalPort() + "<BR>";
		res += "<ol><li>Exotica</li><li>FarmHouse<li><li>Margherita</li>";
		res += "<li>Barbeque</li><li>PannerExotica</li><li>GarlicBread</li>";
		res += "</body></html>";
		System.out.println("Server running on"+request.getLocalPort());*/
		String r="";
		for(Pizza p:pizzas)
		{
		r+="<tr><td>"+p.getPizzaType()+"</td> : <td>"+p.getPizzaName()+"</td></tr>";
		}
		String res="<html><body><B>Instance Name : " + request.getLocalName() + "<BR>";
		res += "<B>Port : </B>" + + request.getLocalPort() + "<BR>";
		res += "<table border='1'><thead><th>Pizza Type</th><th>Pizza Name</th></thead>";
		res+= r;
		res += "</table></body></html>";
		System.out.println("sever running" + request.getLocalPort());
		return res;
	}
	
	@PostMapping(value="/add",consumes=MediaType.APPLICATION_JSON_VALUE)
	public String addPizza(@RequestBody Pizza p,HttpServletRequest  request, HttpServletResponse response) {
		pizzas.add(p);
		int localPort=request.getLocalPort();
		HashMap hmap=new HashMap();
		hmap.put("pizzaName",p.getPizzaName());
		hmap.put("pizzaType",	p.getPizzaType());
		System.out.println(hmap);
		String json=JSONObject.toJSONString(hmap);
		System.out.println("json value "+localPort);
		
		HttpHeaders httpHead=new HttpHeaders();
		httpHead.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> ent=new HttpEntity(json,httpHead);
		if(localPort==9002) {
			
			
			String res=restTemplate.exchange("http://localhost:9003/pizzas/add", 
					HttpMethod.POST,ent, java.lang.String.class).getBody();
			
			
			System.out.println("Send data to 9003");
		}else if(localPort==9003) {
			String res=restTemplate.exchange("http://localhost:9004/pizzas/add", 
					HttpMethod.POST,ent, java.lang.String.class).getBody();
			System.out.println("send data to 9004");
		}
		return "<HTML><BODY><B>ADDED THE PIZZA</B></BODY></HTML>";
	}
	
	@PutMapping(value="/update",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String updatePizza(Pizza formPizza,String oldname) {
		int c=0;
		for(Pizza pizza:pizzas) {
			if(pizza.getPizzaName().equalsIgnoreCase(oldname)) {
				pizza.setPizzaName(formPizza.getPizzaName());
				pizzas.set(c, pizza);
			}
			c++;
		}
		//pizzas.add(p);
		return "<html><body><p>updated</p></body></html>";
	}


	/*@GetMapping(value="/get",consumes=MediaType.APPLICATION_JSON_VALUE)
	public List<Pizza> getPizza() {
		return pizzas;
		
	}*/
}
