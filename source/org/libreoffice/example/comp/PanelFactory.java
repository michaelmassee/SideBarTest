package org.libreoffice.example.comp;

import com.sun.star.awt.XWindow;
import com.sun.star.beans.PropertyValue;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.rendering.XCanvas;
import com.sun.star.ui.XUIElement;
import com.sun.star.ui.XUIElementFactory;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.XComponentContext;

/**
 * This is the factory that creates the sidebar panel that displays an analog clock.
 */
public class PanelFactory implements XUIElementFactory, XServiceInfo {
	public static final String __serviceName = "org.apache.openoffice.sidebar.AnalogClockPanelFactory";
	private static final String msURLhead = "private:resource/toolpanel/AnalogClockPanelFactory";
	private static final String IMPLEMENTATION_NAME = PanelFactory.class.getName();
	private static final String[] SERVICE_NAMES = { __serviceName };

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
			xFactory = Factory.createComponentFactory(PanelFactory.class, SERVICE_NAMES);
		}
		return xFactory;
	}
	// ----------------------------------------------------------------------------------------------------------

	public PanelFactory(final XComponentContext xContext) {
		Log.Instance().println("WorkbenchPanelFactory constructor");
		mxContext = xContext;
	}

	/**
	 * The main factory method has two parts: - Extract and check some values from the given arguments - Check the sResourceURL and create a panel for it.
	 */
	@Override
	public XUIElement createUIElement(final String sResourceURL, final PropertyValue[] aArgumentList) throws NoSuchElementException, IllegalArgumentException {
		Log.Instance().println("createUIElement " + sResourceURL);

		// Reject all resource URLs that don't have the right prefix.
		if (!sResourceURL.startsWith(msURLhead)) {
			throw new NoSuchElementException(sResourceURL, this);
		}

		// Retrieve the parent window and canvas from the given argument list.
		XWindow xParentWindow = null;
		XCanvas xCanvas = null;
		Log.Instance().println("processing " + aArgumentList.length + " arguments");
		for (final PropertyValue aValue : aArgumentList) {
			Log.Instance().println("    " + aValue.Name + " = " + aValue.Value);
			if (aValue.Name.equals("ParentWindow")) {
				try {
					xParentWindow = (XWindow) AnyConverter.toObject(XWindow.class, aValue.Value);
				} catch (IllegalArgumentException aException) {
					Log.Instance().PrintStackTrace(aException);
				}
			} else if (aValue.Name.equals("Canvas")) {
				xCanvas = (XCanvas) AnyConverter.toObject(XCanvas.class, aValue.Value);
			}
		}
		// Check some arguments.
		if (xParentWindow == null) {
			throw new IllegalArgumentException("No parent window provided to the UIElement factory. Cannot create tool panel.", this, (short) 1);
		}

		// Create the panel.
		final String sElementName = sResourceURL.substring(msURLhead.length() + 1);
		if (sElementName.equals("AnalogClockPanel"))
			return new UIElement(sResourceURL, new AnalogClockPanel(xParentWindow, mxContext, xCanvas));
		else
			return null;
	}

	@Override
	public String getImplementationName() {
		return IMPLEMENTATION_NAME;
	}

	@Override
	public String[] getSupportedServiceNames() {
		return SERVICE_NAMES;
	}

	@Override
	public boolean supportsService(final String sServiceName) {
		for (final String sSupportedServiceName : SERVICE_NAMES)
			if (sSupportedServiceName.equals(sServiceName))
				return true;
		return false;
	}

	private final XComponentContext mxContext;
}
