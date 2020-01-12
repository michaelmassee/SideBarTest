package org.libreoffice.example.comp;

import java.util.HashMap;
import java.util.Map;

import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.DispatchDescriptor;
import com.sun.star.frame.FeatureStateEvent;
import com.sun.star.frame.XDispatch;
import com.sun.star.frame.XDispatchProvider;
import com.sun.star.frame.XStatusListener;
import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.lib.uno.helper.ComponentBase;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.URL;

/**
 * We have to provide a protocol handler only so that we can show an options dialog. In the panel description in Sidebar.xcu there is a field "DefaultMenuCommand". Its value is a
 * UNO command name that is executed when the user clicks on the "more options" button in the panel title bar. We need the protocol handler to provide a new command
 * "ShowAnalogClockOptionsDialog" that, when executed, shows the Java dialog implemented by AnalogClockOptionsDialog.
 */
public class ProtocolHandler extends ComponentBase implements XDispatchProvider, XDispatch {
	public final static String __serviceName = "org.apache.openoffice.sidebar.ProtocolHandler";
	final static String msProtocol = "org.apache.openoffice.sidebar";
	final static String msShowCommand = "ShowAnalogClockOptionsDialog";

	private static final String IMPLEMENTATION_NAME = ProtocolHandler.class.getName();
	private static final String[] SERVICE_NAMES = { __serviceName };

	public ProtocolHandler(final XComponentContext xContext) {
		maListeners = new HashMap<>();
		Log.Instance().println("started ProtocolHandler");
	}

	// -----------------------------------------------------------------------------------------------
	/**
	 * kommt zuerst
	 *
	 * @param xRegistryKey
	 * @return
	 */
	public static boolean __writeRegistryServiceInfo(XRegistryKey xRegistryKey) {
		return Factory.writeRegistryServiceInfo(IMPLEMENTATION_NAME, SERVICE_NAMES, xRegistryKey);
	}

	/**
	 * Gives a factory for creating the service.<br>
	 * This method is called by the <code>JavaLoader</code><br>
	 *
	 * @return Returns a <code>XSingleServiceFactory</code> for creating the component.<br>
	 * @see com.sun.star.comp.loader.JavaLoader<br>
	 * @param sImplementationName The implementation name of the component.<br>
	 */

	public static XSingleComponentFactory __getComponentFactory(String sImplementationName) {
		Log.Instance().println("__getComponentFactory " + sImplementationName);

		XSingleComponentFactory xFactory = null;

		if (sImplementationName.equals(IMPLEMENTATION_NAME)) {
			xFactory = Factory.createComponentFactory(ProtocolHandler.class, SERVICE_NAMES);
		}
		return xFactory;
	}

	// -----------------------------------------------------------------------------------------------

	// ----- Implementation of UNO interface XDispatchProvider -----

	@Override
	public XDispatch queryDispatch(final URL aURL, final String sTarget, final int nFlags) throws RuntimeException {
		// Check the given URL to make sure that it
		// a) has the right protocol and
		// b) has the one command name that is supported.
		if (!aURL.Complete.startsWith(msProtocol))
			return null;
		else if (aURL.Complete.endsWith(msShowCommand))
			return this;
		else
			return null;
	}

	/**
	 * We only support one command but still have to implement this method.
	 */
	@Override
	public com.sun.star.frame.XDispatch[] queryDispatches(final DispatchDescriptor[] aRequests) throws RuntimeException {
		final int nDispatchCount = aRequests.length;
		final XDispatch[] aDispatches = new XDispatch[nDispatchCount];
		for (int nIndex = 0; nIndex < nDispatchCount; ++nIndex)
			aDispatches[nIndex] = queryDispatch(aRequests[nIndex].FeatureURL, aRequests[nIndex].FrameName, aRequests[nIndex].SearchFlags);
		return aDispatches;
	}

	// ----- Implementation of UNO interface XDispatch -----

	@Override
	public void addStatusListener(final XStatusListener xListener, final URL aURL) {
		maListeners.put(aURL, xListener);
		xListener.statusChanged(new FeatureStateEvent(this, aURL, "Feature Descriptor", true, false, false));
	}

	@Override
	public void removeStatusListener(final XStatusListener xListener, final URL aURL) {
		maListeners.remove(xListener);
	}

	@Override
	public void dispatch(final URL aURL, final PropertyValue[] aArguments) {
		if (aURL.Complete.endsWith(msShowCommand))
			AnalogClockOptionsDialog.Show();
	}

	private Map<URL, XStatusListener> maListeners;
}