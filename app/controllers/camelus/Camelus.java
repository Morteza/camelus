package controllers.camelus;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Endpoint;
import org.apache.camel.Route;

import play.modules.camelus.CamelusPlugin;
import play.mvc.*;

public class Camelus extends Controller {

    public static void index() {
		List<Route> routes = CamelusPlugin.getCamelContext().getRoutes();
		List<Endpoint> endpoints = new ArrayList<Endpoint>(CamelusPlugin.getCamelContext().getEndpoints());
		List<String> components = CamelusPlugin.getCamelContext().getComponentNames();
		String version = CamelusPlugin.getCamelContext().getVersion();
		String uptime = CamelusPlugin.getCamelContext().getUptime();
		render(routes, endpoints, components, version, uptime);

    	render();
    }

}
