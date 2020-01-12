
package org.libreoffice.example.comp;

import com.sun.star.comp.loader.FactoryHelper;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.lang.XSingleServiceFactory;
import com.sun.star.registry.XRegistryKey;

/**
 * This class basically represents the extension. Its __writeRegistryServiceInfo() method registers two services, the panel factory and the protocol handler, while its
 * __getServiceFactory() method is the factory mathod that creates objects of the two services.
 */
public class Component {
	public static XSingleServiceFactory __getServiceFactory(final String sImplementationName, final XMultiServiceFactory xFactory, final XRegistryKey xKey) {
		XSingleServiceFactory xResult = null;
		Log.Instance().println("looking up service factory for " + sImplementationName);
		if (sImplementationName.equals(PanelFactory.class.getName())) {
			xResult = FactoryHelper.getServiceFactory(PanelFactory.class, PanelFactory.__serviceName, xFactory, xKey);
		} else if (sImplementationName.equals(ProtocolHandler.class.getName())) {
			xResult = FactoryHelper.getServiceFactory(ProtocolHandler.class, ProtocolHandler.__serviceName, xFactory, xKey);
		}
		Log.Instance().println("    returning " + xResult);

		return xResult;
	}

	public static boolean __writeRegistryServiceInfo(final XRegistryKey xKey) {
		boolean bResult = true;
		try {
			Log.Instance().println("writing registry service info for WorkbenchPanelFactory");

			bResult &= FactoryHelper.writeRegistryServiceInfo(PanelFactory.class.getName(), PanelFactory.__serviceName, xKey);

			bResult &= FactoryHelper.writeRegistryServiceInfo(ProtocolHandler.class.getName(), ProtocolHandler.__serviceName, xKey);

			Log.Instance().println("    success");
		} catch (java.lang.Exception e) {
			Log.Instance().PrintStackTrace(e);
		}

		return bResult;
	}
}
