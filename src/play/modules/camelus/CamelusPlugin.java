package play.modules.camelus;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import play.Logger;
import play.PlayPlugin;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.Router;

public class CamelusPlugin extends PlayPlugin {

    private static DefaultCamelContext context;
    
    @Override
    public void onApplicationStart() {
    	try {
            if (context == null) {
                Logger.info("Starting Camelus ...");
                context = new DefaultCamelContext();
                context.setName("Camelus");

                context.start();
                Logger.info("Camelus started successfully.");
            }
    	} catch (Exception e) {
    		Logger.error("Error while starting Camelus.", e);
    	}
    }
    @Override
    public void onApplicationStop() {
        Logger.info("Stopping Camel Services...");
        try {
            context.shutdown();
            while (!context.isStopped()) {
                Thread.sleep(1000);
            }
            context = null;
            Logger.info("Camelus stopped successfully.");
        } catch (Exception e) {
        	Logger.error("Error while stopping Camelus.", e);
        }
    }

    public <T> T getBeanOfType(Class<T> clazz) {
        if (clazz.equals(CamelContext.class)) {
            Logger.info("Injecting %s... Done!", clazz.getName());
            return (T) context;
        }
        return null;
    }

    @Override
    public boolean rawInvocation(Request request, Response response) throws Exception {
        if ("/@camelus".equals(request.path)) {
            response.status = 302;
            response.setHeader("Location", "/@camelus/");
            return true;
        }
        return false;
    }

    @Override
    public void onRoutesLoaded() {
        Router.prependRoute("GET", "/@camelus/?", "controllers.camelus.Camelus.index");
    }

    public static CamelContext getCamelContext() {
        return context;
    }

}
