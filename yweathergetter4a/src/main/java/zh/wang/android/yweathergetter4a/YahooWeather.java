/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2014 Zhenghong Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zh.wang.android.yweathergetter4a;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import zh.wang.android.yweathergetter4a.UserLocationUtils.LocationResult;

/**
 * A wrapper for accessing Yahoo weather informations. 
 * @author Zhenghong Wang
 */
public class YahooWeather implements LocationResult {

    public enum ErrorType {
        ConnectionFailed,
        NoLocationFound,
        ParsingFailed,
        NoLocationPermissionOrFunction,
		Unknown;
	}

    ErrorType mErrorType = null;

    public static final String YAHOO_WEATHER_ERROR = "Yahoo! Weather - Error";
    public static final int FORECAST_INFO_MAX_SIZE = 5;
    public static final int DEFAULT_CONNECTION_TIMEOUT = 5 * 1000;

	public enum SEARCH_MODE {
		GPS,
		PLACE_NAME,
		WOEID
	}
	
	public enum UNIT {
	    FAHRENHEIT,
	    CELSIUS,
	}

    private static final String YQL_WEATHER_ENDPOINT_AUTHORITY = "query.yahooapis.com";
    private static final String YQL_WEATHER_ENDPOINT_PATH = "/v1/public/yql";

	private YahooWeatherInfoListener mWeatherInfoResult;
	private boolean mNeedDownloadIcons;
	private SEARCH_MODE mSearchMode;
    private int mConnectionTimeout = DEFAULT_CONNECTION_TIMEOUT;

	// Use Metric units by default
	private UNIT mUnit = UNIT.CELSIUS;
	
	private Context mContext;
	private static YahooWeather mInstance = new YahooWeather();
	
	public SEARCH_MODE getSearchMode() {
		return mSearchMode;
	}

    // ==== Getter Setter ====

	public void setSearchMode(SEARCH_MODE searchMode) {
		mSearchMode = searchMode;
	}
	
	/**
	 * Necessary only if Imperial units to be used instead of Metric
	 * @param unit, should be {@link UNIT#CELSIUS} or {@link UNIT#FAHRENHEIT}. {@link UNIT#CELSIUS} in default.
	 * Units for temperature Fahrenheit or Celsius
	 * See {@link YahooWeather#turnCtoF(int)} and {@link YahooWeather#turnFtoC(int)}
     */
	public void setUnit(UNIT unit) {
        mUnit = unit;
	}
	
	public UNIT getUnit() {
		return mUnit;
	}

    // ==== Instance ====

	/**
	 * Get the YahooWeather instance.
	 * Use this to query weather information from Yahoo.
	 * @return YahooWeather instance
	 */
	public static YahooWeather getInstance() {
        getInstance(DEFAULT_CONNECTION_TIMEOUT);
		return mInstance;
	}
	
	/**
	 * Get the YahooWeather instance.
	 * Use this to query weather information from Yahoo.
	 * @param connectTimeout in milliseconds, 5 seconds in default
	 * @return YahooWeather instance
	 */
	public static YahooWeather getInstance(int connectTimeout) {
	    return getInstance(connectTimeout, false);
	}
	
	/**
	 * Get the YahooWeather instance.
	 * Use this to query weather information from Yahoo.
	 * @param connectTimeout in milliseconds, 5 seconds in default
	 * @param isDebuggable set if you want some debug log in Logcat
	 * @return YahooWeather instance
	 */
	public static YahooWeather getInstance(int connectTimeout, boolean isDebuggable) {
        mInstance.mConnectionTimeout = connectTimeout;
	    YahooWeatherLog.setDebuggable(isDebuggable);
		return mInstance;
	}
	
	/**
	 * Set it to true will enable downloading the default weather icons.
	 * The Default icons are too tiny, so in most cases, you don't need them.
	 * @param needDownloadIcons Weather it will enable downloading the default weather icons
	 */
	public void setNeedDownloadIcons(final boolean needDownloadIcons) {
		mNeedDownloadIcons = needDownloadIcons;
	}

	/**
	 * Use a name of place to query Yahoo weather apis for weather information. 
	 * Querying will be run on a separated thread to accessing Yahoo's apis.
	 * When it is completed, a callback will be fired.
	 * See {@link YahooWeatherInfoListener} for detail.
	 * @param context app's context
	 * @param cityAreaOrLocation A city name, like "Shanghai"; an area name, like "Mountain View";
	 * a pair of city and country, like "Tokyo, Japan"; a location or view spot, like "Eiffel Tower";
	 * Yahoo's apis will find a closest position for you.
	 * @param result A {@link WeatherInfo} instance.
	 */
	public void queryYahooWeatherByPlaceName(final Context context, final String cityAreaOrLocation, 
			final YahooWeatherInfoListener result) {
		YahooWeatherLog.d("query yahoo weather by name of place");
		mContext = context;
        if (!NetworkUtils.isConnected(context)) {
            mErrorType = ErrorType.ConnectionFailed;
        	return;
        }
        final String convertedlocation = AsciiUtils.convertNonAscii(cityAreaOrLocation);
		mWeatherInfoResult = result;
		final WeatherQueryByPlaceTask task = new WeatherQueryByPlaceTask();
		task.execute(new String[] {convertedlocation});
	}

	/**
	 * Use known WOEID to query Yahoo weather apis for weather information. 
	 * Querying will be run on a separated thread to accessing Yahoo's apis.
	 * When it is completed, a callback will be fired.
	 * See {@link YahooWeatherInfoListener} for detail.
	 * @param context app's context
	 * @param woeid WOEID;
	 * @param result A {@link WeatherInfo} instance.
	 */
	public void queryYahooWeatherByWOEID(final Context context, final String woeid, 
			final YahooWeatherInfoListener result) {
		YahooWeatherLog.d("query yahoo weather by WOEID");
		mContext = context;
        if (!NetworkUtils.isConnected(context)) {
            mErrorType = ErrorType.ConnectionFailed;
        	return;
        }
		mWeatherInfoResult = result;
		final WeatherQueryByWOEIDTask task = new WeatherQueryByWOEIDTask();
		task.execute(new String[] {woeid});
	}

	/** 
	 * Use lat and lon pair to query Yahoo weather apis for weather information.
	 * Querying will be run on a separated thread to accessing Yahoo's apis.
	 * When it is completed, a callback will be fired.
	 * See {@link YahooWeatherInfoListener} for detail.
	 * @param context app's context
	 * @param lat A string of latitude value
	 * @param lon A string of longitude value
	 * @param result A {@link WeatherInfo} instance
	 */
	public void queryYahooWeatherByLatLon(final Context context, final Double lat, final Double lon, 
			final YahooWeatherInfoListener result) {
		YahooWeatherLog.d("query yahoo weather by lat lon");
		mContext = context;
        if (!NetworkUtils.isConnected(context)) {
            mErrorType = ErrorType.ConnectionFailed;
        	return;
        }
		mWeatherInfoResult = result;
		final WeatherQueryByLatLonTask task = new WeatherQueryByLatLonTask();
		task.execute(new Double[]{lat, lon});
	}
	
	/**
	 * Use your device's GPS to automatically detect where you are, then query Yahoo weather apis
	 * for weather information.
	 * @param context app's context
	 * @param result A {@link WeatherInfo} instance
	 */
	public void queryYahooWeatherByGPS(final Context context, final YahooWeatherInfoListener result) {
		YahooWeatherLog.d("query yahoo weather by gps");
        if (!NetworkUtils.isConnected(context)) {
            mErrorType = ErrorType.ConnectionFailed;
        	return;
        }
		mContext = context;
		mWeatherInfoResult = result;
		(new UserLocationUtils()).findUserLocation(context, this);
	}

	@Override
	public void gotLocation(Location location, UserLocationUtils.UserLocationErrorType errorType) {
	    if (location == null) {
            if (errorType == UserLocationUtils.UserLocationErrorType.FIND_LOCATION_NOT_PERMITTED ||
                    errorType == UserLocationUtils.UserLocationErrorType.LOCATION_SERVICE_IS_NOT_AVAILABLE) {
                mErrorType = ErrorType.NoLocationPermissionOrFunction;
            } else {
                mErrorType = ErrorType.NoLocationFound;
            }
			mWeatherInfoResult.gotWeatherInfo(null, mErrorType);
	        return;
	    }
	    final Double lat = location.getLatitude();
	    final Double lon = location.getLongitude();
	    final WeatherQueryByLatLonTask task = new WeatherQueryByLatLonTask();
	    task.execute(new Double[] {lat, lon});
	}

	public static int turnFtoC(int tempF) {
		return (int) ((tempF - 32) * 5.0f / 9);
	}
	
	public static int turnCtoF(int tempC) {
	    return (int) (tempC * 9.0f / 5 + 32);
	}
	
	public static String addressToPlaceName(final Address address) {
	    String result = "";
	    if (address.getLocality() != null) {
	        result += address.getLocality();
	        result += " ";
	    }
	    if (address.getAdminArea() != null) {
	        result += address.getAdminArea();
	        result += " ";
	    }
	    if (address.getCountryName() != null) {
	        result += address.getCountryName();
	        result += " ";
	    }
	    return result;
	}

    private String getWeatherString(Context context, String placeName) {
        YahooWeatherLog.d("query yahoo weather with placeName : " + placeName);

        String qResult = "";

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority(YQL_WEATHER_ENDPOINT_AUTHORITY);
        builder.path(YQL_WEATHER_ENDPOINT_PATH);
        builder.appendQueryParameter("q", "select * from weather.forecast where woeid in " +
                        "(select woeid from geo.places(1) where text=\"" +
                        placeName +
                        "\")");
        String queryUrl = builder.build().toString();

        YahooWeatherLog.d("query url : " + queryUrl);

        try {
            URL url = new URL(queryUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(mConnectionTimeout);
            urlConnection.connect();
            switch (urlConnection.getResponseCode()) {
                case HttpURLConnection.HTTP_BAD_GATEWAY:
                    mErrorType = ErrorType.ConnectionFailed;
                    throw new Exception("HTTP_BAD_GATEWAY");
                case HttpURLConnection.HTTP_BAD_METHOD:
                    mErrorType = ErrorType.ConnectionFailed;
                    throw new Exception("HTTP_BAD_METHOD");
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    mErrorType = ErrorType.ConnectionFailed;
                    throw new Exception("HTTP_BAD_REQUEST");
                case HttpURLConnection.HTTP_CLIENT_TIMEOUT:
                    mErrorType = ErrorType.ConnectionFailed;
                    throw new Exception("HTTP_CLIENT_TIMEOUT");
                case HttpURLConnection.HTTP_CONFLICT:
                    mErrorType = ErrorType.ConnectionFailed;
                    throw new Exception("HTTP_CONFLICT");
                case HttpURLConnection.HTTP_ENTITY_TOO_LARGE:
                    mErrorType = ErrorType.ConnectionFailed;
                    throw new Exception("HTTP_ENTITY_TOO_LARGE");
                case HttpURLConnection.HTTP_FORBIDDEN:
                    mErrorType = ErrorType.ConnectionFailed;
                    throw new Exception("HTTP_FORBIDDEN");
				case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
					mErrorType = ErrorType.ConnectionFailed;
					throw new Exception("HTTP_GATEWAY_TIMEOUT");
				case HttpURLConnection.HTTP_UNAVAILABLE:
					mErrorType = ErrorType.ConnectionFailed;
					throw new Exception("HTTP_UNAVAILABLE");
                default:
                    break;
            }
            InputStream content = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String currLine = "";
            try {
                while ((currLine = buffer.readLine()) != null) {
                    qResult += currLine;
                }
            }
            finally {
                urlConnection.disconnect();
            }
        }
		catch (Exception e) {
            qResult = "";
        }

        return qResult;
    }

    private String getWeatherStringByWOEID(Context context, String woeid) {
        YahooWeatherLog.d("query yahoo weather with WOEID : " + woeid);

        String qResult = "";

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority(YQL_WEATHER_ENDPOINT_AUTHORITY);
        builder.path(YQL_WEATHER_ENDPOINT_PATH);
        builder.appendQueryParameter("q", "select * from weather.forecast where woeid=" + woeid);
        String queryUrl = builder.build().toString();

        YahooWeatherLog.d("query url : " + queryUrl);

        try {
            URL url = new URL(queryUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            switch (urlConnection.getResponseCode()) {
                case HttpURLConnection.HTTP_BAD_GATEWAY:
                    mErrorType = ErrorType.ConnectionFailed;
                    throw new Exception("HTTP_BAD_GATEWAY");
                case HttpURLConnection.HTTP_BAD_METHOD:
                    mErrorType = ErrorType.ConnectionFailed;
                    throw new Exception("HTTP_BAD_METHOD");
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    mErrorType = ErrorType.ConnectionFailed;
                    throw new Exception("HTTP_BAD_REQUEST");
                case HttpURLConnection.HTTP_CLIENT_TIMEOUT:
                    mErrorType = ErrorType.ConnectionFailed;
                    throw new Exception("HTTP_CLIENT_TIMEOUT");
                case HttpURLConnection.HTTP_CONFLICT:
                    mErrorType = ErrorType.ConnectionFailed;
                    throw new Exception("HTTP_CONFLICT");
                case HttpURLConnection.HTTP_ENTITY_TOO_LARGE:
                    mErrorType = ErrorType.ConnectionFailed;
                    throw new Exception("HTTP_ENTITY_TOO_LARGE");
                case HttpURLConnection.HTTP_FORBIDDEN:
                    mErrorType = ErrorType.ConnectionFailed;
                    throw new Exception("HTTP_FORBIDDEN");
				case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
					mErrorType = ErrorType.ConnectionFailed;
					throw new Exception("HTTP_GATEWAY_TIMEOUT");
				case HttpURLConnection.HTTP_UNAVAILABLE:
					mErrorType = ErrorType.ConnectionFailed;
					throw new Exception("HTTP_UNAVAILABLE");
                default:
                    break;
            }
            InputStream content = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String currLine = "";
            try {
                while ((currLine = buffer.readLine()) != null) {
                    qResult += currLine;
                }
            }
            finally {
                urlConnection.disconnect();
            }
        }
		catch (Exception e) {
            qResult = "";
        }

        return qResult;
    }

	private Document convertStringToDocument(Context context, String src) {
		if (src.length() == 0) return null;

		Document dest = null;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser;

		try {
			parser = dbFactory.newDocumentBuilder();
			dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
		} catch (ParserConfigurationException e) {
			YahooWeatherLog.printStack(e);
            mErrorType = ErrorType.ParsingFailed;
		} catch (SAXException e) {
			YahooWeatherLog.printStack(e);
            mErrorType = ErrorType.ParsingFailed;
		} catch (IOException e) {
			YahooWeatherLog.printStack(e);
            mErrorType = ErrorType.ParsingFailed;
		}

		return dest;
	}
	
	private WeatherInfo parseWeatherInfo(Context context, Document doc) {
        if (doc == null) return null;
		WeatherInfo weatherInfo = new WeatherInfo();
		try {
			
			Node titleNode = doc.getElementsByTagName("title").item(0);
			
			if(titleNode.getTextContent().equals(YAHOO_WEATHER_ERROR)) {
				return null;
			}
			
			weatherInfo.setTitle(titleNode.getTextContent());
			weatherInfo.setDescription(doc.getElementsByTagName("description").item(0).getTextContent());
			weatherInfo.setLanguage(doc.getElementsByTagName("language").item(0).getTextContent());
			weatherInfo.setLastBuildDate(doc.getElementsByTagName("lastBuildDate").item(0).getTextContent());
			
			Node locationNode = doc.getElementsByTagName("yweather:location").item(0);
			weatherInfo.setLocationCity(locationNode.getAttributes().getNamedItem("city").getNodeValue());
			weatherInfo.setLocationRegion(locationNode.getAttributes().getNamedItem("region").getNodeValue());
			weatherInfo.setLocationCountry(locationNode.getAttributes().getNamedItem("country").getNodeValue());
			
			Node windNode = doc.getElementsByTagName("yweather:wind").item(0);
			weatherInfo.setWindChill(windNode.getAttributes().getNamedItem("chill").getNodeValue());
			weatherInfo.setWindDirection(windNode.getAttributes().getNamedItem("direction").getNodeValue());
			weatherInfo.setWindSpeed(windNode.getAttributes().getNamedItem("speed").getNodeValue());
			
			Node atmosphereNode = doc.getElementsByTagName("yweather:atmosphere").item(0);
			weatherInfo.setAtmosphereHumidity(atmosphereNode.getAttributes().getNamedItem("humidity").getNodeValue());
			weatherInfo.setAtmosphereVisibility(atmosphereNode.getAttributes().getNamedItem("visibility").getNodeValue());
			weatherInfo.setAtmospherePressure(atmosphereNode.getAttributes().getNamedItem("pressure").getNodeValue());
			weatherInfo.setAtmosphereRising(atmosphereNode.getAttributes().getNamedItem("rising").getNodeValue());
			
			Node astronomyNode = doc.getElementsByTagName("yweather:astronomy").item(0);
			weatherInfo.setAstronomySunrise(astronomyNode.getAttributes().getNamedItem("sunrise").getNodeValue());
			weatherInfo.setAstronomySunset(astronomyNode.getAttributes().getNamedItem("sunset").getNodeValue());
			
			weatherInfo.setConditionTitle(doc.getElementsByTagName("title").item(2).getTextContent());
			weatherInfo.setConditionLat(doc.getElementsByTagName("geo:lat").item(0).getTextContent());
			weatherInfo.setConditionLon(doc.getElementsByTagName("geo:long").item(0).getTextContent());
			
			Node currentConditionNode = doc.getElementsByTagName("yweather:condition").item(0);
			weatherInfo.setCurrentCode(
					Integer.parseInt(
							currentConditionNode.getAttributes().getNamedItem("code").getNodeValue()
							));
			weatherInfo.setCurrentText(
					currentConditionNode.getAttributes().getNamedItem("text").getNodeValue());
			int curTempF = Integer.parseInt(currentConditionNode.getAttributes().getNamedItem("temp").getNodeValue());
			int curTempC = YahooWeather.turnFtoC(curTempF);
			weatherInfo.setCurrentTemp(mUnit == UNIT.CELSIUS ? curTempC : curTempF);
			weatherInfo.setCurrentConditionDate(
					currentConditionNode.getAttributes().getNamedItem("date").getNodeValue());
			
			if (mNeedDownloadIcons) {
				weatherInfo.setCurrentConditionIcon(ImageUtils.getBitmapFromWeb(
						weatherInfo.getCurrentConditionIconURL()));
			}
			
			for (int i = 0; i < FORECAST_INFO_MAX_SIZE; i++) {
				this.parseForecastInfo(weatherInfo.getForecastInfoList().get(i), doc, i);
			}

		} catch (NullPointerException e) {
		    YahooWeatherLog.printStack(e);
            mErrorType = ErrorType.ParsingFailed;
			weatherInfo = null;
		}
		
		return weatherInfo;
	}
	
	private void parseForecastInfo(final WeatherInfo.ForecastInfo forecastInfo, final Document doc, final int index) {
		Node forecast1ConditionNode = doc.getElementsByTagName("yweather:forecast").item(index);
		forecastInfo.setForecastCode(Integer.parseInt(
				forecast1ConditionNode.getAttributes().getNamedItem("code").getNodeValue()
				));
		forecastInfo.setForecastText(
				forecast1ConditionNode.getAttributes().getNamedItem("text").getNodeValue());
		forecastInfo.setForecastDate(
				forecast1ConditionNode.getAttributes().getNamedItem("date").getNodeValue());
		forecastInfo.setForecastDay(
				forecast1ConditionNode.getAttributes().getNamedItem("day").getNodeValue());
        int highF = Integer.parseInt(forecast1ConditionNode.getAttributes().getNamedItem("high").getNodeValue());
        int highC = YahooWeather.turnFtoC(highF);
		forecastInfo.setForecastTempHigh(mUnit == UNIT.CELSIUS ? highC : highF);
        int lowF = Integer.parseInt(forecast1ConditionNode.getAttributes().getNamedItem("low").getNodeValue());
        int lowC = YahooWeather.turnFtoC(lowF);
		forecastInfo.setForecastTempLow(mUnit == UNIT.CELSIUS ? lowC : lowF);
		if (mNeedDownloadIcons) {
			forecastInfo.setForecastConditionIcon(
					ImageUtils.getBitmapFromWeb(forecastInfo.getForecastConditionIconURL()));
		}
	}
	
	private class WeatherQueryByPlaceTask extends AsyncTask<String, Void, WeatherInfo> {
		@Override
		protected WeatherInfo doInBackground(String... placeName) {
			if (placeName == null || placeName.length > 1) {
				throw new IllegalArgumentException("Parameter of WeatherQueryByPlaceTask is illegal. "
                        + "No place name exists.");
			}
            String weatherString = getWeatherString(mContext, placeName[0]);
            Document weatherDoc = convertStringToDocument(mContext, weatherString);
            WeatherInfo weatherInfo = parseWeatherInfo(mContext, weatherDoc);
            return weatherInfo;
		}

		@Override
		protected void onPostExecute(WeatherInfo result) {
			super.onPostExecute(result);
            if (result == null && mErrorType == null) mErrorType = ErrorType.Unknown;
			mWeatherInfoResult.gotWeatherInfo(result, mErrorType);
			mContext = null;
		}
	}

	private class WeatherQueryByWOEIDTask extends AsyncTask<String, Void, WeatherInfo> {
		@Override
		protected WeatherInfo doInBackground(String... woeid) {
			if (woeid == null || woeid.length > 1) {
				throw new IllegalArgumentException("Parameter of WeatherQueryByWOEIDTask is illegal. "
                        + "No WOEID.");
			}
            String weatherString = getWeatherStringByWOEID(mContext, woeid[0]);
            Document weatherDoc = convertStringToDocument(mContext, weatherString);
            WeatherInfo weatherInfo = parseWeatherInfo(mContext, weatherDoc);
            return weatherInfo;
		}

		@Override
		protected void onPostExecute(WeatherInfo result) {
			super.onPostExecute(result);
            if (result == null && mErrorType == null) mErrorType = ErrorType.Unknown;
			mWeatherInfoResult.gotWeatherInfo(result, mErrorType);
			mContext = null;
		}
	}

	private class WeatherQueryByLatLonTask extends AsyncTask<Double, Void, WeatherInfo> {

        @Override
		protected WeatherInfo doInBackground(Double... params) {
			if (params == null || params.length != 2) {
				throw new IllegalArgumentException("Parameter of WeatherQueryByLatLonTask is illegal."
                        + "No Lat Lon exists.");
			}
			final Double lat = params[0];
			final Double lon = params[1];
			// Get city name or place name
			if (mContext != null) {
                final Geocoder geocoder = new Geocoder(mContext);
                try {
                    List<Address> addresses = geocoder.getFromLocation(lat.doubleValue(), lon.doubleValue(), 1);
                    for (Address address : addresses) {
                        YahooWeatherLog.d("latlon : " + lat + ", " + lon);
                        int n = address.getMaxAddressLineIndex();
                        for (int i = 0; i < n; i++) {
                            YahooWeatherLog.d("address line : " + address.getAddressLine(i));
                        }

                        YahooWeatherLog.d("adminarea : " + address.getAdminArea());
                        YahooWeatherLog.d("subAdminArea : " + address.getSubAdminArea());
                        YahooWeatherLog.d("countryName : " + address.getCountryName());
                        YahooWeatherLog.d("feature name : " + address.getFeatureName());
                        YahooWeatherLog.d("locality : " + address.getLocality());
                        YahooWeatherLog.d("sublocality : " + address.getSubLocality());
                        YahooWeatherLog.d("postCode : " + address.getPostalCode());
                        YahooWeatherLog.d("premises : " + address.getPremises());
                        YahooWeatherLog.d("thoroughfare : " + address.getThoroughfare());

                        String weatherString = getWeatherString(mContext, addressToPlaceName(address));
                        Document weatherDoc = convertStringToDocument(mContext, weatherString);
                        WeatherInfo weatherInfo = parseWeatherInfo(mContext, weatherDoc);
                        if(weatherInfo != null) weatherInfo.setAddress(address);
                        return weatherInfo;
                    }

                } catch (IOException e) {
                    YahooWeatherLog.printStack(e);
                }
			}
            return null;
		}

		@Override
		protected void onPostExecute(WeatherInfo result) {
			super.onPostExecute(result);
            if (result == null && mErrorType == null) mErrorType = ErrorType.Unknown;
			mWeatherInfoResult.gotWeatherInfo(result, mErrorType);
			mContext = null;
		}
	}

}
