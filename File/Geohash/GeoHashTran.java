import java.util.HashMap;

public class GeoHashTran {

    public static final double MINLAT = -90;
    public static final double MAXLAT = 90;
    public static final double MINLNG = -180;
    public static final double MAXLNG = 180;

    private static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n',
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final HashMap<Character, Integer> CHARSMAP;

    static {
        CHARSMAP = new HashMap<Character, Integer>();
        for (int i = 0; i < CHARS.length; i++) {
            CHARSMAP.put(CHARS[i], i);
        }
    }

    private String getBase32BinaryString(int i) {
        if (i < 0 || i > 31) {
            return null;
        }
        String str = Integer.toBinaryString(i + 32);
        return str.substring(1);
    }

    private String getGeoHashBinaryString(String geoHash) {
        if (geoHash == null || "".equals(geoHash)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < geoHash.length(); i++) {
            char c = geoHash.charAt(i);
            if (CHARSMAP.containsKey(c)) {
                String cStr = getBase32BinaryString(CHARSMAP.get(c));
                if (cStr != null) {
                    sb.append(cStr);
                }
            }
        }
        return sb.toString();
    }

    public String getLocation(String geoHash) {
        String geoHashBinaryStr = getGeoHashBinaryString(geoHash);
        if (geoHashBinaryStr == null) {
            return null;
        }
        StringBuffer lat = new StringBuffer();
        StringBuffer lng = new StringBuffer();
        for (int i = 0; i < geoHashBinaryStr.length(); i++) {
            if (i % 2 != 0) {
                lat.append(geoHashBinaryStr.charAt(i));
            } else {
                lng.append(geoHashBinaryStr.charAt(i));
            }
        }
        double latValue = getGeoHashMid(lat.toString(), MINLAT, MAXLAT);
        double lngValue = getGeoHashMid(lng.toString(), MINLNG, MAXLNG);

        return latValue + "," + lngValue;
    }

    private double getGeoHashMid(String binaryStr, double min, double max) {
        if (binaryStr == null || binaryStr.length() < 1) {
            return (min + max) / 2.0;
        }
        if (binaryStr.charAt(0) == '1') {
            return getGeoHashMid(binaryStr.substring(1), (min + max) / 2.0, max);
        } else {
            return getGeoHashMid(binaryStr.substring(1), min, (min + max) / 2.0);
        }
    }

    public static void main(String[] args) {
        GeoHashTran g = new GeoHashTran();
        String geohash = "wx4gde2f";
        String bean = g.getLocation(geohash);
        System.out.println(bean);
    }
}
