package com.kna.dnstunlr;

import java.net.InetAddress;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final static String	DNS1	= "69.197.169.9";
	private final static String	DNS2	= "192.95.16.109";

	private String				ip;
	private String				netmask;
	private String				gateway;
	private String				dns1;
	private String				dns2;

	private Button				buttonDhcp;
	private Button				buttonStatic;

	private TextView			textViewDns1;
	private TextView			textViewDns2;

	private WifiConfiguration	_wifiConfigurationCurrent;
	private WifiManager			_wifiManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		buttonDhcp = (Button) findViewById(R.id.button1);
		buttonStatic = (Button) findViewById(R.id.button2);

		textViewDns1 = (TextView) findViewById(R.id.textView1);
		textViewDns2 = (TextView) findViewById(R.id.textView2);

		_wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo connectionInfo = _wifiManager.getConnectionInfo();
		List<WifiConfiguration> configuredNetworks = _wifiManager.getConfiguredNetworks();
		DhcpInfo dhcp = _wifiManager.getDhcpInfo();

		ip = Formatter.formatIpAddress(connectionInfo.getIpAddress());
		gateway = Formatter.formatIpAddress(dhcp.gateway);

		for (WifiConfiguration wifiConfiguration : configuredNetworks) {
			if (wifiConfiguration.networkId == connectionInfo.getNetworkId()) {
				_wifiConfigurationCurrent = wifiConfiguration;
				break;
			}
		}

	}

	@Override
	protected void onStart() {
		super.onStart();

		buttonDhcp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setDhcp();
			}
		});

		buttonStatic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setStatic();
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void setDhcp() {

		try {
			NetworkUtil.setIpAssignment("DHCP", _wifiConfigurationCurrent);
			_wifiManager.updateNetwork(_wifiConfigurationCurrent);
			_wifiManager.setWifiEnabled(false);
			_wifiManager.setWifiEnabled(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		finish();
		Toast.makeText(this, "Set DHCP", Toast.LENGTH_SHORT).show();
	}

	private void setStatic() {
		// TODO: set default values plus edited DNS from textview

		try {
			NetworkUtil.setIpAssignment("STATIC", _wifiConfigurationCurrent);
			// setting
			NetworkUtil.setIpAddress(InetAddress.getByName(ip), 24, _wifiConfigurationCurrent);
			NetworkUtil.setGateway(InetAddress.getByName(gateway), _wifiConfigurationCurrent);
			NetworkUtil.setDNS1(InetAddress.getByName(DNS1), InetAddress.getByName(DNS2), _wifiConfigurationCurrent);
			_wifiManager.updateNetwork(_wifiConfigurationCurrent);
			_wifiManager.setWifiEnabled(false);
			_wifiManager.setWifiEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		finish();
		Toast.makeText(this, "Set static", Toast.LENGTH_SHORT).show();
	}

}
