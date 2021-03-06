package com.kna.dnstunlr;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.ArrayList;

import android.net.wifi.WifiConfiguration;

public class NetworkUtil {

	public static void setIpAssignment(String assign, WifiConfiguration wifiConf) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		setEnumField(wifiConf, assign, "ipAssignment");
	}

	public static void setIpAddress(InetAddress addr, int prefixLength, WifiConfiguration wifiConf)
			throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException,
			NoSuchMethodException, ClassNotFoundException, InstantiationException, InvocationTargetException {
		Object linkProperties = getField(wifiConf, "linkProperties");
		if (linkProperties == null)
			return;
		Class laClass = Class.forName("android.net.LinkAddress");
		Constructor laConstructor = laClass.getConstructor(new Class[] { InetAddress.class, int.class });
		Object linkAddress = laConstructor.newInstance(addr, prefixLength);

		ArrayList mLinkAddresses = (ArrayList) getDeclaredField(linkProperties, "mLinkAddresses");
		mLinkAddresses.clear();
		mLinkAddresses.add(linkAddress);
	}

	public static void setGateway(InetAddress gateway, WifiConfiguration wifiConf) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException,
			NoSuchMethodException, InstantiationException, InvocationTargetException {
		Object linkProperties = getField(wifiConf, "linkProperties");
		if (linkProperties == null)
			return;
		Class routeInfoClass = Class.forName("android.net.RouteInfo");
		Constructor routeInfoConstructor = routeInfoClass.getConstructor(new Class[] { InetAddress.class });
		Object routeInfo = routeInfoConstructor.newInstance(gateway);

		ArrayList mRoutes = (ArrayList) getDeclaredField(linkProperties, "mRoutes");
		mRoutes.clear();
		mRoutes.add(routeInfo);
	}

	public static void setDNS1(InetAddress dns1, InetAddress dns2, WifiConfiguration wifiConf)
			throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		Object linkProperties = getField(wifiConf, "linkProperties");
		if (linkProperties == null)
			return;

		ArrayList<InetAddress> mDnses = (ArrayList<InetAddress>) getDeclaredField(linkProperties, "mDnses");
		mDnses.clear();
		mDnses.add(dns1);
		mDnses.add(dns2);
	}

	public static Object getField(Object obj, String name) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getField(name);
		Object out = f.get(obj);
		return out;
	}

	public static Object getDeclaredField(Object obj, String name) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getDeclaredField(name);
		f.setAccessible(true);
		Object out = f.get(obj);
		return out;
	}

	public static void setEnumField(Object obj, String value, String name) throws SecurityException,
			NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getField(name);
		f.set(obj, Enum.valueOf((Class<Enum>) f.getType(), value));
	}

}
