package com.adsdk.sdk.nativeads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.adsdk.sdk.Const;
import com.adsdk.sdk.Log;
import com.adsdk.sdk.RequestException;
import com.adsdk.sdk.nativeads.NativeAd.ImageAsset;
import com.adsdk.sdk.nativeads.NativeAd.TextAsset;
import com.adsdk.sdk.nativeads.NativeAd.Tracker;

public class RequestNativeAd {

	public NativeAd sendRequest(NativeAdRequest request) throws RequestException {
		String url = request.toString();
		Log.d("Ad RequestPerform HTTP Get Url: " + url);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setSoTimeout(client.getParams(), Const.SOCKET_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(client.getParams(), Const.CONNECTION_TIMEOUT);
		HttpProtocolParams.setUserAgent(client.getParams(), request.getUserAgent());
		HttpGet get = new HttpGet(url);
		get.setHeader("User-Agent", System.getProperty("http.agent"));
		HttpResponse response;
		try {
			response = client.execute(get);
			int responseCode = response.getStatusLine().getStatusCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				return parse(response.getEntity().getContent());
			} else {
				throw new RequestException("Server Error. Response code:" + responseCode);
			}
		} catch (RequestException e) {
			throw e;
		} catch (ClientProtocolException e) {
			throw new RequestException("Error in HTTP request", e);
		} catch (IOException e) {
			throw new RequestException("Error in HTTP request", e);
		} catch (Throwable t) {
			throw new RequestException("Error in HTTP request", t);
		}
	}

	protected NativeAd parse(final InputStream inputStream) throws RequestException {

		final NativeAd response = new NativeAd();
		List<TextAsset> textAssets = new ArrayList<NativeAd.TextAsset>();
		List<ImageAsset> imageAssets = new ArrayList<NativeAd.ImageAsset>();
		List<Tracker> trackers = new ArrayList<NativeAd.Tracker>();

		// TODO: fill native ad fields
		try {
			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			String result = sb.toString();
			JSONObject mainObject = new JSONObject(result);
			JSONObject imageAssetsObject = mainObject.getJSONObject("imageassets");
			if(imageAssetsObject != null) {

			}
			
			

		} catch (UnsupportedEncodingException e) {
			throw new RequestException("Cannot parse Response", e);
		} catch (IOException e) {
			throw new RequestException("Cannot parse Response", e);
		} catch (JSONException e) {
			throw new RequestException("Cannot parse Response", e);
		}

		return response;
	}

}