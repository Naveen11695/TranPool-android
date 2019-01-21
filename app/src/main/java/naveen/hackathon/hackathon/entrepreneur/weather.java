

package naveen.hackathon.hackathon.entrepreneur;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import naveen.hackathon.hackathon.R;
import zh.wang.android.yweathergetter4a.WeatherInfo;
import zh.wang.android.yweathergetter4a.YahooWeather;
import zh.wang.android.yweathergetter4a.YahooWeatherInfoListener;


public class weather extends Activity implements YahooWeatherInfoListener {

	private ImageView mIvWeather0;
	private TextView mTvWeather0;
	private TextView mTvErrorMessage;
	private TextView mTvTitle;
	private LinearLayout mCurrentWeatherInfoLayout;
	private LinearLayout mWeatherInfosLayout;

	private YahooWeather mYahooWeather = YahooWeather.getInstance(5000, true);

    private ProgressDialog mProgressDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        
    	mTvTitle = (TextView) findViewById(R.id.textview_title);
		mTvWeather0 = (TextView) findViewById(R.id.textview_weather_info_0);
		mTvErrorMessage = (TextView) findViewById(R.id.textview_error_message);
		mIvWeather0 = (ImageView) findViewById(R.id.imageview_weather_info_0);
		mCurrentWeatherInfoLayout = (LinearLayout) findViewById(R.id.current_weather_info);
        mWeatherInfosLayout = (LinearLayout) findViewById(R.id.weather_infos);
        
        this.searchByGPS();
    }
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		hideProgressDialog();
		mProgressDialog = null;
		super.onDestroy();
	}

	@Override
	public void gotWeatherInfo(final WeatherInfo weatherInfo, YahooWeather.ErrorType errorType) {
		// TODO Auto-generated method stub
        hideProgressDialog();
        if (weatherInfo != null) {
            setNormalLayout();
            mWeatherInfosLayout.removeAllViews();
            mTvTitle.setText(weatherInfo.getTitle());
            mTvWeather0.setText("====== CURRENT ======" + "\n" +
                    "date: " + weatherInfo.getCurrentConditionDate() + "\n" +
                    "weather: " + weatherInfo.getCurrentText() + "\n" +
                    "temperature in ºC: " + weatherInfo.getCurrentTemp() + "\n" +
                    "wind chill: " + weatherInfo.getWindChill() + "\n" +
                    "wind direction: " + weatherInfo.getWindDirection() + "\n" +
                    "wind speed: " + weatherInfo.getWindSpeed() + "\n" +
                    "Humidity: " + weatherInfo.getAtmosphereHumidity() + "\n" +
                    "Pressure: " + weatherInfo.getAtmospherePressure() + "\n" +
                    "Visibility: " + weatherInfo.getAtmosphereVisibility()
            );
            if (weatherInfo.getCurrentConditionIcon() != null) {
                mIvWeather0.setImageBitmap(weatherInfo.getCurrentConditionIcon());
            }
            for (int i = 0; i < YahooWeather.FORECAST_INFO_MAX_SIZE; i++) {
                final LinearLayout forecastInfoLayout = (LinearLayout)
                        getLayoutInflater().inflate(R.layout.forecastinfo, null);
                final TextView tvWeather = (TextView) forecastInfoLayout.findViewById(R.id.textview_forecast_info);
                final WeatherInfo.ForecastInfo forecastInfo = weatherInfo.getForecastInfoList().get(i);
                tvWeather.setText("====== FORECAST " + (i+1) + " ======" + "\n" +
                        "date: " + forecastInfo.getForecastDate() + "\n" +
                        "weather: " + forecastInfo.getForecastText() + "\n" +
                        "low  temperature in ºC: " + forecastInfo.getForecastTempLow() + "\n" +
                        "high temperature in ºC: " + forecastInfo.getForecastTempHigh() + "\n"
                );
                final ImageView ivForecast = (ImageView) forecastInfoLayout.findViewById(R.id.imageview_forecast_info);
                if (forecastInfo.getForecastConditionIcon() != null) {
                    ivForecast.setImageBitmap(forecastInfo.getForecastConditionIcon());
                }
                mWeatherInfosLayout.addView(forecastInfoLayout);
            }
        } else {
			setNoResultLayout(errorType.name());
        }
	}

	private void setNormalLayout() {
		mTvTitle.setVisibility(View.VISIBLE);
		mCurrentWeatherInfoLayout.setVisibility(View.VISIBLE);
		mWeatherInfosLayout.setVisibility(View.VISIBLE);
		mTvErrorMessage.setVisibility(View.INVISIBLE);
	}
	
	private void setNoResultLayout(String errorMsg) {
		mTvTitle.setVisibility(View.INVISIBLE);
        mWeatherInfosLayout.removeAllViews();
		mWeatherInfosLayout.setVisibility(View.INVISIBLE);
		mCurrentWeatherInfoLayout.setVisibility(View.INVISIBLE);
		mTvErrorMessage.setVisibility(View.VISIBLE);
		mTvErrorMessage.setText("Sorry, no result returned\n" + errorMsg);
	    mProgressDialog.cancel();
	}
	
	private void searchByGPS() {
		mYahooWeather.setNeedDownloadIcons(true);
		mYahooWeather.setUnit(YahooWeather.UNIT.CELSIUS);
		mYahooWeather.setSearchMode(YahooWeather.SEARCH_MODE.GPS);
		mYahooWeather.queryYahooWeatherByGPS(getApplicationContext(), this);
	}
	
	private void searchByPlaceName(String location) {
		mYahooWeather.setNeedDownloadIcons(true);
		mYahooWeather.setUnit(YahooWeather.UNIT.CELSIUS);
		mYahooWeather.setSearchMode(YahooWeather.SEARCH_MODE.PLACE_NAME);
		mYahooWeather.queryYahooWeatherByPlaceName(getApplicationContext(), location, weather.this);
	}
	
	private void showProgressDialog() {
      	if (mProgressDialog != null && mProgressDialog.isShowing()) {
      		mProgressDialog.cancel();
      	}
        mProgressDialog = new ProgressDialog(weather.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
	}
	
	private void hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.cancel();
		}
	}

}
