package naveen.hackathon.hackathon;

import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Distance {
double longitude, latitude;
String longDir, latDir;
    Distance(double l1, double l2)
    {
        longitude = l1;
        latitude = l2;
    }

    Distance()
    {
        longitude = 0.0;
        latitude = 0.0;
    }
    
    Distance(double l1, double l2, String l3, String l4)
    {
        longitude = l1;
        latitude = l2;
        longDir = l3;
        latDir = l4;
    }
    
    double sameCountry(Distance D)
    {
        double d1,d2,d3,d4;
        d1 = pow((this.longitude - D.longitude),2);
        d2 = pow((this.latitude - D.latitude),2);
        d3 = sqrt(d1+d2);
        d4 = 111.2*d3;
        return d4;
    }
    double diffCountry(Distance D)
    {
        double d1=0.0,d2=0.0;
        if(longitude==D.longitude && longDir==D.longDir && latDir==D.latDir)
        {
            d1=latitude - D.latitude;
            d2=111.2*d1;
        }
        
        else if(longitude==D.longitude && longDir==D.longDir && latDir!=D.latDir)
        {
            d1=latitude+D.latitude;
            d2=111.2*d1;
        }
        
        else if(latitude==D.latitude && latDir==D.latDir && longDir==D.longDir)
        {
            d1=longitude-D.longitude;
            d2=111.2*d1*cos(latitude);
        }
        
        else if(latitude==D.latitude && latDir==D.latDir && longDir!=D.longDir && longitude!=D.longitude)
        {
            d1=longitude + D.longitude;
            d2=111.2*d1*cos(latitude);
        }
        
        else if(latitude==D.latitude && latDir==D.latDir && longitude==D.longitude && longDir!=D.longDir)
        {
            d1=360-(longitude+D.longitude);
            d2=111.2*d1*cos(latitude);
        }
        return d2;
    }

    int price(double distance)
    {
        int cost;
        if(distance<=500)
            cost=50;
        else if(distance>=501 && distance<=700)
            cost= (int) (50+(distance-500)*3);
        else
            cost= (int) (50+(50*2)+((distance-700)*3));

        return cost;
    }
    int price1(double distance)
    {
        int cost;
        if(distance<=5000)
            cost=1000;
        else if(distance>=5001 && distance<=10001)
            cost=(int)(1000+(distance-5000)*500);
        else
            cost=(int)(1000+(5000*500)+((distance-1000)*1000));
        return cost;
    }

}
